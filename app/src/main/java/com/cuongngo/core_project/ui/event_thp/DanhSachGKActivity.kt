package com.cuongngo.core_project.ui.event_thp

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityListGkBinding
import com.cuongngo.core_project.databinding.ActivityLoginByUserIdBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.MainActivity
import com.cuongngo.core_project.ui.bottom_sheet.SearchUserBottomSheet
import com.cuongngo.core_project.ui.event_thp.adapter.ExaminerAdapter
import com.cuongngo.core_project.ui.event_thp.model.Examiner
import com.cuongngo.core_project.ui.login.UserViewModel
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.toast.showMessageToast
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