package com.cuongngo.core_project.ui.add_request

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.databinding.ActivityRequestDetailBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.add_request.adapter.FieldAdapter
import com.cuongngo.core_project.ui.search_form.FormViewModel
import io.reactivex.disposables.Disposable

class RequestDetailActivity : AppBaseActivityMVVM<ActivityRequestDetailBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_request_detail

    companion object {
        val TAG = RequestDetailActivity::class.java.simpleName
        const val FORM_CODE_KEY = "FORM_CODE_KEY"
    }

    fun newIntent(
        context: Context,
        formCode: String
    ): Intent {
        return Intent(context, RequestDetailActivity::class.java).apply {
            putExtra(FORM_CODE_KEY, formCode)
        }
    }

    private val formCode by lazy { intent.getStringExtra(FORM_CODE_KEY) ?: "" }

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private lateinit var fieldAdapter: FieldAdapter

    override fun setUp() {
        viewModel.getFormByCode(formCode)
        setupRecycleViewListField()
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.form) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { form ->
                        fieldAdapter.submitListField(form.formSchema)
                        WTF(TAG, "dataFields ${form.formSchema}")
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
                //show detail
            }
        )
        binding.rvListField.apply {
            layoutManager = gridLayoutManager
            adapter = fieldAdapter
        }
    }

}