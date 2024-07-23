package com.cuongngo.core_project.ui.add_request

import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.databinding.FragmentAddRequestBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.request_detail.RequestDetailActivity
import com.cuongngo.core_project.ui.search_form.ListFormActivity
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.cuongngo.core_project.ui.search_form.form_adapter.FormAdapter
import io.reactivex.disposables.Disposable

class AddRequestFragment : BaseFragmentMVVM<FragmentAddRequestBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private lateinit var formAdapter: FormAdapter
    override fun inflateLayout(): Int = R.layout.fragment_add_request

    companion object {
        val TAG = AddRequestFragment::class.java.simpleName
    }

    override fun setUp() {
        viewModel.getAllForm()
        setupRcvListForm()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setUpObserver() {
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
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupRcvListForm() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        formAdapter = FormAdapter(
            arrayListOf(),
            onItemClickListener = {
                startActivity(
                    RequestDetailActivity().newIntentAdd(
                        requireContext(),
                        it.formCode
                    )
                )
            }
        )
        binding.rvListForm.apply {
            adapter = formAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }
    }

}