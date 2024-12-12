package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.content.Intent
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.data.database.roomdb.entity.gdFilter
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

    var filter: gdFilter? = null

    private lateinit var gdAdapter: GdAdapter
    override fun setUp() {
        viewModel.getAllGD()

        // Khởi tạo ActivityResultLauncher
        val filterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultFilter = result.data?.getSerializableExtra("filter") as? gdFilter
                if (resultFilter != null) {
                    filter = resultFilter
                    WTF("testFilter $resultFilter")
                    handleGetLsGD(resultFilter)
                }
            }
        }

        // Mở màn hình bộ lọc
        binding.loAppBar.clFilter.setOnClickListener {
            val intent = Intent(this, FilterLsGdActivity::class.java)
            intent.putExtra("currentFilter", filter)
            filterLauncher.launch(intent)
        }

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

    private fun handleGetLsGD(filter: gdFilter?){

        when(filter?.soNgay){

            0 -> {
                viewModel.getTransactionsByDateRange(
                    startDate = filter.startDate ?: return,
                    endDate = filter.endDate ?: return
                )
                val dayOfMonth = filter.startDate?.get(Calendar.DAY_OF_MONTH) ?: 0
                val monthOfYear = filter.startDate?.get(Calendar.MONTH)?.plus(1) ?: 1
                val year = filter.startDate?.get(Calendar.YEAR) ?: 0
                val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                val monthStr = if (monthOfYear < 10) "0${monthOfYear}" else "$monthOfYear"

                val dayOfMonth2 = filter.endDate?.get(Calendar.DAY_OF_MONTH) ?: 0
                val monthOfYear2 = filter.endDate?.get(Calendar.MONTH)?.plus(1) ?: 1
                val year2 = filter.endDate?.get(Calendar.YEAR) ?: 0
                val dayStr2 = if (dayOfMonth2 < 10) "0${dayOfMonth2}" else "$dayOfMonth2"
                val monthStr2 = if (monthOfYear2 < 10) "0${monthOfYear2}" else "$monthOfYear2"

                binding.tvTitleFilter.text = "$dayStr/$monthStr/$year đến $dayStr2/$monthStr2/$year2"
            }

            30 -> {
                val calendarStart = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -30)
                }
                viewModel.getLastNDaysTransactions(calendarStart)
                binding.tvTitleFilter.text = "30 ngày gần nhất"
            }

            7 -> {
                val calendarStart = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }
                viewModel.getLastNDaysTransactions(calendarStart)
                binding.tvTitleFilter.text = "7 ngày gần nhất"
            }

            else -> {
                viewModel.getAllGD()
            }

        }

    }

    override fun onResume() {
//        viewModel.getAllGD()
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
                    if (it.data.isNullOrEmpty()){
                        binding.rvListGd.isVisible = false
                        binding.tvEmptyListGd.isVisible = true
                    }else{
                        binding.rvListGd.isVisible = true
                        binding.tvEmptyListGd.isVisible = false
                    }
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