package com.cuongngo.core_project.ui.list_request

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.FragmentListRequestBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.add_request.RequestDetailActivity
import com.cuongngo.core_project.ui.add_request.adapter.FieldAdapter
import com.cuongngo.core_project.ui.list_request.adapter.CatrgoryPagerAdapter
import com.cuongngo.core_project.ui.list_request.adapter.CatrgoryPagerAdapter.Companion.COMPLETED
import com.cuongngo.core_project.ui.list_request.adapter.CatrgoryPagerAdapter.Companion.MY_REQUEST
import com.cuongngo.core_project.ui.list_request.adapter.CatrgoryPagerAdapter.Companion.WAIT_FOR_ME
import com.cuongngo.core_project.ui.list_request.adapter.RequestAdapter
import com.cuongngo.core_project.ui.search_form.FormViewModel
import com.google.android.material.tabs.TabLayoutMediator

class ListRequestFragment : BaseFragmentMVVM<FragmentListRequestBinding, FormViewModel>() {

    override val viewModel: FormViewModel by kodeinViewModel()

    override fun inflateLayout() = R.layout.fragment_list_request

    companion object {
        val TAG = ListRequestFragment::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPager()
    }

    override fun setUp() {
        //
    }

    private fun setupPager() {

        var adapter = CatrgoryPagerAdapter(this)
        binding.viewpagerRequest.adapter = adapter

        TabLayoutMediator(binding.tabRequest, binding.viewpagerRequest) { tab, position ->
            tab.text = when (position) {
                0 -> "Yêu cầu của tôi"
                1 -> "Chờ tôi xử lý"
                2 -> "Đã xử lý"
                else -> null
            }
        }.attach()

        binding.viewpagerRequest.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {

                override fun onViewAttachedToWindow(p0: View?) {
                    //
                }

                override fun onViewDetachedFromWindow(p0: View?) {
                    //
                }

            })

    }

    override fun setUpObserver() {
        //
    }


}