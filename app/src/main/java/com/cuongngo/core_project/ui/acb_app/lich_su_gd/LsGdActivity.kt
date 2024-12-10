package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.data.database.roomdb.entity.groupTransactionsByDate
import com.cuongngo.core_project.databinding.ActivityLsGdBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.acb_app.GdViewModel
import com.cuongngo.core_project.ui.search_form.ListFormActivity
import java.util.Calendar
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
            Intent(applicationContext, AddGdActivity::class.java).apply {
                startActivity(this)
            }
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