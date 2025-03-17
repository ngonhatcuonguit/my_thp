package com.cuongngo.my_thp.ui.event_thp

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.databinding.ActivityListGkBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.MainActivity
import com.cuongngo.my_thp.ui.event_thp.adapter.ExaminerAdapter
import com.cuongngo.my_thp.ui.event_thp.model.Examiner
import com.cuongngo.my_thp.ui.login.UserViewModel
import com.cuongngo.my_thp.utils.TFunc
import io.reactivex.disposables.Disposable

class DanhSachGKActivity : AppBaseActivityMVVM<ActivityListGkBinding, UserViewModel>() {

    override val viewModel: UserViewModel by kodeinViewModel()

    companion object {
        val TAG = DanhSachGKActivity::class.java.simpleName
    }

    override fun inflateLayout(): Int = R.layout.activity_list_gk

    private var onGKSelected: TFunc<Examiner?>? = null
    private var keyword: String? = null
    private var compositeDisposable: Disposable? = null
    private lateinit var examinerAdapter: ExaminerAdapter

    override fun setUp() {
        viewModel.getListGK()
        with(binding) {

        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.listExaminer) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.data?.let { it1 -> setupRecycleView(it1) }
                    WTF("listGK ${it.data?.data}")
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupRecycleView(listData: List<Examiner>) {
        examinerAdapter = ExaminerAdapter(
            listData,
            null,
            onGKSelected = {
                AppPreferences.saveGKInfo(it)
                gotoTietMucDetail()
            }
        )
        binding.rcvOption.apply {
            layoutManager = LinearLayoutManager(this@DanhSachGKActivity)
            adapter = examinerAdapter
        }
    }

    private fun gotoMain() {
        Intent(this, MainActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        //
    }
}