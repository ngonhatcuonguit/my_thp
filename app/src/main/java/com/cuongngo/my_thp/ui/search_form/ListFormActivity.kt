package com.cuongngo.my_thp.ui.search_form

import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.my_thp.data.database.roomdb.entity.FormEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.generateRandomHeaderList
import com.cuongngo.my_thp.data.database.roomdb.entity.generateRandomProcessStep
import com.cuongngo.my_thp.data.database.roomdb.entity.generateRandomSheet
import com.cuongngo.my_thp.data.database.roomdb.entity.randomString
import com.cuongngo.my_thp.databinding.ActivityListFormBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.search_form.form_adapter.FormAdapter
import io.reactivex.disposables.Disposable
import kotlin.random.Random

class ListFormActivity : AppBaseActivityMVVM<ActivityListFormBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

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
//        syncForm()
        viewModel.getAllForm()
        binding.btnAddForm.setOnClickListener {
            addForm()
        }
        setupRecycleViewListForm()
    }

//    fun syncForm(){
//        if (AppPreferences.getCountRecordLocalForm() != 0){
//            viewModel.getListForm()
//        }else{
//            //
//        }
//    }

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
        val gridLayoutManager = GridLayoutManager(this, 2)
        formAdapter = FormAdapter(
            this,
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

    // Create a list of Field objects
    val headers = generateRandomHeaderList(6)
    val sheets = List(1){generateRandomSheet()}
    val processSteps = List(2) { generateRandomProcessStep() }

    fun addForm() {
        viewModel.upsertForm(
            FormEntity(
                form_id = Random.nextLong(1, 1000),
                name = listOf("HRM form test", "Factory form test", "Parameter form test", "Office form", "Other form").random(),
                status = listOf(1,2,3,4,5).random().toString(),
                form_code = randomString(10),
                schema_code = randomString(10),
                category = listOf("HRM", "Factory", "Parameter", "Office", "Other").random(),
                schema_name = randomString(10),
                list_body = sheets,
                list_header = headers,
                process_steps = processSteps
            )
        )

    }


}