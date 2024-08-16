package com.cuongngo.core_project.ui.add_request

import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.FragmentAddRequestBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.ui.search_form.ListFormActivity
import com.cuongngo.core_project.ui.search_form.form_adapter.FormAdapter
import com.cuongngo.core_project.utils.Constants.CategoryRequestDetail.Companion.ADD
import com.jakewharton.rxbinding3.widget.textChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AddRequestFragment : BaseFragmentMVVM<FragmentAddRequestBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private var keyword: String? = null

    private lateinit var formAdapter: FormAdapter
    override fun inflateLayout(): Int = R.layout.fragment_add_request

    companion object {
        val TAG = AddRequestFragment::class.java.simpleName
    }

    override fun setUp() {
        viewModel.getAllForm()
        setupFeatureSearch()
        setupRcvListForm()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupFeatureSearch() {
        compositeDisposable =
            binding.edtSearch.textChangeEvents()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    hideKeyboard()
                    if (keyword != it.text.trim().toString() && it.text.trim().toString().isNotEmpty()){
                        keyword = it.text.trim().toString()
                        keyword?.let {
                            if (keyword=="all"){
                                viewModel.getAllForm()
                            }
                            viewModel.searchForms(it)
                        }
                    }
                    WTF("testSearchKeyWord $keyword")
                    hideKeyboard()
                }
        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            text?.let { keySearch ->
                binding.ivClearSearch.isVisible = keySearch.isNotEmpty()
            }
        }
    }

    override fun setUpObserver() {

        observeLiveDataChanged(viewModel.upsertListFormToLocal){
            it.onResultReceived(
                onLoading = {
                    processSyncDialog.show()
                },
                onSuccess = {
                    processSyncDialog.hide()
                    WTF("testAPiForm upsert-OK")
                    viewModel.getCountRecord()
                },
                onError = {
                    processSyncDialog.hide()
                }
            )
        }
        observeLiveDataChanged(viewModel.checkCountRecord){
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testAPiForm countRecord ${it.data}")
                    AppPreferences.setCountRecordLocalForm(it.data ?:0)
                    viewModel.getAllForm()
                },
                onError = {}
            )
        }

        observeLiveDataChanged(viewModel.allForm) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { listForm ->
                        formAdapter.submitListForm(listForm)
                        WTF(ListFormActivity.TAG, "dataForm: ${listForm}")
                    }
                    syncForm()
                },
                onError = {
                    hideProgressDialog()
                    syncForm()
                }
            )
        }

        observeLiveDataChanged(viewModel.listFormRemote){
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testAPiForm countRecord ${it.data?.data}")
                    if (it.data?.data?.isNotEmpty() == true){
                        viewModel.upsertListForm(it.data?.data ?: arrayListOf())
                    }
                },
                onError = {
                    processSyncDialog.hide()
                }
            )
        }

        observeLiveDataChanged(viewModel.searchForms){
            it.onResultReceived(
                onLoading = {
                    binding.rvListForm.isVisible = false
                    binding.progressBar.isVisible = true
                },
                onSuccess = {
                    binding.rvListForm.isVisible = true
                    binding.progressBar.isVisible = false
                    formAdapter.submitListForm(it.data)
                    WTF("testSearchForm ${it.data}")
                },
                onError = {
                    binding.rvListForm.isVisible = true
                    binding.progressBar.isVisible = false
                }
            )
        }

    }
    private fun syncForm() = if(AppPreferences.getCountRecordLocalForm() == 0){
        viewModel.getListForm(true)
    }else{
        viewModel.getListForm(false)
    }

    private fun setupRcvListForm() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        formAdapter = FormAdapter(
            requireContext(),
            arrayListOf(),
            onItemClickListener = {
                setupShowDialogConfirm(it)
            }
        )
        binding.rvListForm.apply {
            adapter = formAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }
    }

    private fun setupShowDialogConfirm(form: FormEntity) {
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = "Tạo yêu cầu mới",
                subTitle = form.title,
                content = "Bạn muốn tạo một yêu cầu mới với mẫu form: \n${form.form_code} - ${form.name}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Tạo yêu cầu",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                startActivity(
                    RequestMasterDetailActivity.newIntent(
                        requireContext(),
                        category = ADD,
                        request = null,
                        form = form
                    )
                )
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

}