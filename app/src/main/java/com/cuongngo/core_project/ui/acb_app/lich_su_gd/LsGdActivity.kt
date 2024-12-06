package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomUsers
import com.cuongngo.core_project.data.database.roomdb.entity.groupTransactionsByDate
import com.cuongngo.core_project.databinding.ActivityLsGdBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.acb_app.GdViewModel
import com.cuongngo.core_project.ui.search_form.ListFormActivity
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

class LsGdActivity : AppBaseActivityMVVM<ActivityLsGdBinding, GdViewModel>() {

    override val viewModel: GdViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_ls_gd

    private lateinit var gdAdapter: GdAdapter
    override fun setUp() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, Calendar.OCTOBER)  // Tháng 12 (sử dụng Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 11)           // Ngày 1
            set(Calendar.HOUR_OF_DAY, 15)           // 15 giờ
            set(Calendar.MINUTE, 30)                // 30 phút
        }
        viewModel.getAllGD()
        binding.btnAddGd.setOnClickListener {
            viewModel.upsetGD(
                GdEntity(
                    id = Random.nextLong(1000, 9999),
                    transactionCode = "TGC10002",
                    transactionType = "Rút tiền",
                    transactionAmount = 50000,
                    transactionContent = "Rút tiền ATM",
                    transactionDate = calendar
                ),

                )
//            viewModel.upsetGD(
//                GdEntity(
//                    transactionCode = "TGC10003",
//                    transactionType = "Chuyển khoản",
//                    transactionAmount = 200000,
//                    transactionContent = "Chuyển khoản bạn bè",
//                    transactionDate = Date(2024, 11, 2, 9, 0) // Ngày 2 tháng 12 năm 2024 lúc 9:00
//                ),
//            )
//            viewModel.upsetGD(
//                GdEntity(
//                    transactionCode = "TGC10004",
//                    transactionType = "Nạp tiền",
//                    transactionAmount = 150000,
//                    transactionContent = "Nạp tiền vào ví điện tử",
//                    transactionDate = Date(2024, 11, 3, 13, 45) // Ngày 3 tháng 12 năm 2024 lúc 13:45
//                ),
//            )
//            viewModel.upsetGD(
//                GdEntity(
//                    transactionCode = "TGC10005",
//                    transactionType = "Thanh toán",
//                    transactionAmount = 75000,
//                    transactionContent = "Thanh toán hoá đơn điện nước",
//                    transactionDate = Date(2024, 11, 3, 18, 20) // Ngày 3 tháng 12 năm 2024 lúc 18:20
//                )
//            )

        }
        setupRecycleViewListGD()
    }

    private fun setupRecycleViewListGD() {
        val gridLayoutManager = LinearLayoutManager(this)
        gdAdapter = GdAdapter(
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
        binding.rvListGd.apply {
            adapter = gdAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }

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
                        WTF(ListFormActivity.TAG, "dataForm: ${groupedTransactions}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }


}