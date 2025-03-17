package com.cuongngo.my_thp.ui.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.bottom_sheet.FullHeightBottomSheet
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.database.roomdb.entity.RequestEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.my_thp.databinding.FragmentSearchFieldValueBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.bottom_sheet.adapter.UserSingleChoiceAdapter
import com.cuongngo.my_thp.ui.login.UserViewModel
import com.cuongngo.my_thp.utils.TFunc
import com.cuongngo.my_thp.utils.getScreenHeight
import com.cuongngo.my_thp.utils.getScreenWidth
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchUserBottomSheet : FullHeightBottomSheet<FragmentSearchFieldValueBinding>() {
    companion object {
        const val DEFAULT_LIST_USER = "DEFAULT_LIST_USER"
        const val REQUEST_DATA = "REQUEST_DATA"
        const val BOTTOM_SHEET_HEIGHT_VALUE = "BOTTOM_SHEET_HEIGHT_VALUE"
        var DEFAULT_HEIGHT = (getScreenHeight() * 0.95).toInt()
        operator fun invoke(
            requestData: RequestEntity?,
            heightValue: Int? = DEFAULT_HEIGHT
        ): SearchUserBottomSheet {
            return SearchUserBottomSheet().apply {
                arguments = bundleOf(
                    REQUEST_DATA to requestData,
                    BOTTOM_SHEET_HEIGHT_VALUE to heightValue
                )
            }
        }
    }

    private val viewModel: UserViewModel by kodeinViewModel()

    private var onUserSelected: TFunc<UserTHPEntity?>? = null
    private var heightValue: Int = DEFAULT_HEIGHT
    private var listUserDefault: UserTHPEntity? = null
    private var requestData: RequestEntity? = null

    private var keyword: String? = null

    private var compositeDisposable: Disposable? = null
    private lateinit var userAdapter: UserSingleChoiceAdapter
    override fun inflateLayout(): Int = R.layout.fragment_search_field_value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            heightValue =
                it.getInt(BOTTOM_SHEET_HEIGHT_VALUE) ?: SingleChoiceOptionBottomSheet.DEFAULT_HEIGHT
            requestData = it.getSerializable(REQUEST_DATA) as RequestEntity?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupFeatureSearch()
        setupObserver()
    }

    private fun setupView() {
        viewModel.getAllUserLocal()
        setupHeightRecycleView()
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvTitle.text = "Tìm kiếm user"
        binding.ivClearSearch.setOnClickListener {
            binding.edtSearchKeyword.text?.clear()
        }
    }

    private fun setupFeatureSearch() {
        compositeDisposable =
            binding.edtSearchKeyword.textChangeEvents()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    hideKeyboard()
                    if (keyword != it.text.trim().toString()){
                        keyword = it.text.trim().toString()
                        if (keyword.isNullOrEmpty()){
                            viewModel.getAllUserLocal()
                        }else{
                            viewModel.searchUsers(it.text.trim().toString())
                        }
                    }
                    WTF("testSearchKeyWord $keyword")
                    hideKeyboard()
                }
        binding.edtSearchKeyword.doOnTextChanged { text, _, _, _ ->
            text?.let { keySearch ->
                binding.ivClearSearch.isVisible = keySearch.isNotEmpty()
            }
        }
        binding.ivClearSearch.setOnClickListener {
            keyword = ""
            binding.edtSearchKeyword.text?.clear()
            viewModel.getAllUserLocal()
        }
    }

    private fun setupObserver() {
        observeLiveDataChanged(viewModel.getAllUserLocal) {
            it.onResultReceived(
                onLoading = {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rcvOption.isVisible = false
                },
                onError = {
                    binding.progressBar.visibility = View.GONE
                    binding.rcvOption.isVisible = true
                },
                onSuccess = {
                    binding.progressBar.visibility = View.GONE
                    binding.rcvOption.isVisible = true
                    it.data?.apply {
                        if (it.data.isEmpty()){
                            binding.layoutEmptyList.isVisible = true
                            binding.rcvOption.isVisible = false
                        }else{
                            binding.layoutEmptyList.isVisible = false
                            binding.rcvOption.isVisible = true
                        }
                        setupRecycleView(this)
                    }
                }
            )
        }
    }

    private fun setupRecycleView(listData: List<UserTHPEntity>) {
        userAdapter = UserSingleChoiceAdapter(
            listData,
            null,
            onUserSelected = {
                onUserSelected?.invoke(it)
                dismiss()
            }
        )
        binding.rcvOption.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }
    }

    private fun onSearchChange() {
        binding.apply {
            binding.rcvOption.isVisible = userAdapter.itemCount > 0
//            llEmptyData.isVisible = locationAdapter.itemCount <= 0
        }
    }

    private fun setupHeightRecycleView() {
        binding.root.layoutParams.run {
            width = getScreenWidth()
            height = heightValue
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.dispose()
    }

    fun setOnUserSelected(onUserSelected: TFunc<UserTHPEntity?>): SearchUserBottomSheet {
        this.onUserSelected = onUserSelected
        return this
    }

}