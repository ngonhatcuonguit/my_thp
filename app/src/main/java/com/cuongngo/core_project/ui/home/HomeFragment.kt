package com.cuongngo.core_project.ui.home

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.data.database.roomdb.entity.convertRequestEntityToString
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomHeaderList
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomProcessStep
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomSheet
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.response.news.News
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.list_request.adapter.RequestHorizontalAdapter
import com.cuongngo.core_project.ui.onboard.OnBoardActivity
import com.cuongngo.core_project.ui.request_detail.PushRequestCodeBody
import com.cuongngo.core_project.ui.request_detail.RequestBodyPush
import com.cuongngo.core_project.ui.request_detail.RequestCodeResult
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.search_form.form_adapter.FormHorizontalAdapter
import com.cuongngo.core_project.ui.the_acb.TheAcbActivity
import com.cuongngo.core_project.ui.view_pager.ViewPagerAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerHelper
import com.cuongngo.core_project.utils.Constants
import com.cuongngo.core_project.utils.toast.showMessageOnSyncDataSuccess
import com.cuongngo.core_project.utils.toast.showMessageToast
import io.reactivex.disposables.Disposable
import kotlin.random.Random

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()
    override fun inflateLayout() = R.layout.fragment_home

    override fun setUp() {
        binding.apply {
//            tvName.text =
//                "Hello, ${AppPreferences.getUserInfo()?.first_name ?: ""} ${AppPreferences.getUserInfo()?.last_name ?: ""}"

            clThe.setOnClickListener {
                Intent(context, TheAcbActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }
    }
    override fun setUpObserver() {

    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

}