package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.data.database.roomdb.entity.groupTransactionsByDate
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityLsGdBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.acb_app.GdViewModel
import com.cuongngo.core_project.ui.home.HomeFragment
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.search_form.ListFormActivity
import com.cuongngo.core_project.utils.Constants
import com.cuongngo.core_project.utils.number.formatNumberWithDots
import com.cuongngo.core_project.utils.toast.showMessageToast
import java.util.Calendar
import kotlin.random.Random

class LsGdActivity : AppBaseActivityMVVM<ActivityLsGdBinding, GdViewModel>() {

    override val viewModel: GdViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_ls_gd

    private lateinit var gdAdapter: GdAdapter
    override fun setUp() {
        viewModel.getAllGD()
        with(binding){
            loAppBar.ivBack.setOnClickListener {
                finish()
            }
            loAppBar.tvTitle.text = "Lịch sử giao dịch"
            loAppBar.tvTitle.setTextColor(App.getResources().getColor(R.color.black_1c))
            loAppBar.clFilter.setBackgroundColor(App.getResources().getColor(R.color.white))
            loAppBar.ivFilter.setImageResource(R.drawable.ic_filter_gd)

            btnAddGd.setOnClickListener {
                Intent(applicationContext, AddGdActivity::class.java).apply {
                    startActivity(this)
                }
            }

            if (AppPreferences.getACBInfo(HomeFragment.KEY_STK).isNotEmpty()){
                btnAddGd.text = AppPreferences.getACBInfo(HomeFragment.KEY_STK)
            }

            btnAddGd.setOnLongClickListener {
                setupShowDialogChangeValue(AppPreferences.KEY_STK, btnAddGd)
                true
            }

        }

        setupRecycleViewListGD()
    }

    override fun onResume() {
        viewModel.getAllGD()
        super.onResume()
    }

    override fun onRestart() {
        viewModel.getAllGD()
        super.onRestart()
    }

    private fun setupRecycleViewListGD() {
        val gridLayoutManager = LinearLayoutManager(this)
        gdAdapter = GdAdapter(
            arrayListOf(),
            onItemClickListener = {
                //show detail
            },
            onItemLongClickListener = {
                dialogConfirmDeleteGD(it)
            }
        )
//        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                viewModel.loadMoreSearch(totalPages)
//            }
//        }
        binding.rvListGd.apply {
            adapter = gdAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }

    }

    private fun dialogConfirmDeleteGD(gdEntity: GdEntity) {
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = "Xoá giao dịch khỏi danh sách",
                subTitle = "",
                content = "Bạn muốn xoá gd ${gdEntity.transactionContent}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Xoá gd",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                //xoá
                gdAdapter.removeItem(gdEntity)
                viewModel.deleteGD(gdEntity)
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.allGD) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { transactions ->
                        // Sắp xếp giao dịch theo ngày và giờ
                        val groupedTransactions = groupTransactionsByDate(transactions)
                        gdAdapter.submitListDayTransaction(groupedTransactions)
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
        observeLiveDataChanged(viewModel.deleteGD) {
            it.onResultReceived(
                onLoading = {
                    //
                },
                onSuccess = {
                    hideProgressDialog()
                    showMessageToast(
                        this,
                        true,
                        contentFail = "",
                        contentDone = "Xoá gd thành công!"
                    )
                },
                onError = {
                    hideProgressDialog()
                    showMessageToast(
                        this,
                        false,
                        contentFail = "Đã có lỗi xảy ra!",
                        contentDone = ""
                    )
                }
            )
        }
    }

}