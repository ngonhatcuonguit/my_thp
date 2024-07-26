package com.cuongngo.core_project.ui.request_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.databinding.ActivityRequestDetailBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.add_request.adapter.FieldAdapter
import com.cuongngo.core_project.ui.search_form.FormViewModel
import io.reactivex.disposables.Disposable
import kotlin.random.Random

class RequestDetailActivity : AppBaseActivityMVVM<ActivityRequestDetailBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_request_detail

    companion object {
        val TAG = RequestDetailActivity::class.java.simpleName
        const val FORM_CODE_KEY = "FORM_CODE_KEY"
        const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
        const val REQUEST_ID = "REQUEST_ID"
    }

    fun newIntentAdd(
        context: Context,
        formCode: String
    ): Intent {
        return Intent(context, RequestDetailActivity::class.java).apply {
            putExtra(FORM_CODE_KEY, formCode)
        }
    }

    fun newIntentDetail(
        context: Context,
        requestID: Long
    ): Intent {
        return Intent(context, RequestDetailActivity::class.java).apply {
            putExtra(REQUEST_ID, requestID)
        }
    }

    private val formCode by lazy { intent.getStringExtra(FORM_CODE_KEY) ?: "" }
    private val requestCode by lazy { intent.getStringExtra(REQUEST_CODE_KEY) ?: "" }
    private val requestID by lazy { intent.getLongExtra(REQUEST_ID, -1) }
    private var formEntity: FormEntity? = null

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private lateinit var fieldAdapter: FieldAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableLightStatusBar()
    }

    override fun setUp() {
        viewModel.getRequestByID(requestID)
        setupRecycleViewListField()
        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.request) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { request ->
                        binding.apply {
                            tvFormTitle.text = request.requestName.toString()
                        }
                        fieldAdapter.submitListField(request.formBody)
                        WTF(TAG, "requestForm: ${request.requestCode}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }

    }

    private fun setupRecycleViewListField() {
        val gridLayoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        fieldAdapter = FieldAdapter(
            arrayListOf(),
            onItemClickListener = {
                //
            }
        )
        binding.rvListField.apply {
            layoutManager = gridLayoutManager
            adapter = fieldAdapter
        }
    }

}