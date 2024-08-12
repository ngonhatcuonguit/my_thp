package com.cuongngo.core_project.ui.bottom_sheet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.bottom_sheet.FullHeightBottomSheet
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomUsers
import com.cuongngo.core_project.databinding.FragmentSearchFieldValueBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ui.bottom_sheet.adapter.UserSingleChoiceAdapter
import com.cuongngo.core_project.ui.form_schema.RequestViewModel
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.getScreenHeight
import com.cuongngo.core_project.utils.getScreenWidth
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

    private val viewModel: RequestViewModel by kodeinViewModel() //thay = userViewModel

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
        setupRecycleView(generateRandomUsers(10))
    }

    private fun setupView() {
        setupHeightRecycleView()
        binding.ivClose.setOnClickListener {           dismiss()
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
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    hideKeyboard()
                    //page=1
                    keyword = it.text.trim().toString()
                    //call query user like keyword
                    hideKeyboard()
                    WTF("testSearchKeyWord $keyword")
                }
        binding.edtSearchKeyword.doOnTextChanged { text, _, _, _ ->
            text?.let {keySearch ->
                binding.ivClearSearch.isVisible = keySearch.isNotEmpty()
            }
        }

    }

    private fun setupObserver() {
//        observeLiveDataChanged(viewModel.request) {
//            it.onResultReceived(
//                onLoading = {
//                    binding.progressBar.visibility = View.VISIBLE
//                },
//                onError = {
//                    binding.progressBar.visibility = View.GONE
//                },
//                onSuccess = {
//                    binding.progressBar.visibility = View.GONE
//                    it.data?.data?.apply {
//                        setupRecyclerView(this)
//                    }
//                }
//            )
//        }
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