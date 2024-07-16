package com.cuongngo.core_project.ui.search_form

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.FormSchemaEntity
import com.cuongngo.core_project.databinding.ActivityListFormBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.search_form.form_adapter.FormAdapter
import io.reactivex.disposables.Disposable

class ListFormActivity : AppBaseActivityMVVM<ActivityListFormBinding, ListFormViewModel>() {

    override val viewModel: ListFormViewModel by kodeinViewModel()

    companion object {
        val TAG = ListFormActivity::class.java.simpleName
    }

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private lateinit var formAdapter: FormAdapter

    override fun inflateLayout(): Int = R.layout.activity_list_form
    override fun onResume() {
        super.onResume()
    }

    override fun setUp() {
        viewModel.getAllForm()
        binding.btnAddForm.setOnClickListener {
            addForm()
        }
        setupRecycleViewListForm()
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
                        WTF(TAG, "dataForm: ${listForm}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupRecycleViewListForm() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        formAdapter = FormAdapter(
            arrayListOf(),
            onItemClickListener = {
                //show detail
            }
        )
//        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                viewModel.loadMoreSearch(totalPages)
//            }
//        }
        binding.rvListForm.apply {
            adapter = formAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }
    }

    fun addForm() {
        viewModel.insertFormSchema(
            FormSchemaEntity(
                id = 123,
                formCode = "form_code_123",
                code = "schema_code_123",
                schemaName = "schema_name_123"
            )
        )
        viewModel.upsertForm(
            FormEntity(
                id = 123,
                title = "Giấy phép vào khu vực nhạy cảm-12345-THP",
                status = 1,
                code = "form_code_123",
                type = "type1"
            )
        )
    }


}