package com.cuongngo.core_project.ui.list_request

import android.os.Bundle
import android.view.View
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.FragmentListRequestBinding
import com.cuongngo.core_project.ui.list_request.adapter.CategoryPagerAdapter
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

        var adapter = CategoryPagerAdapter(this)
        binding.viewpagerRequest.adapter = adapter

        TabLayoutMediator(binding.tabRequest, binding.viewpagerRequest) { tab, position ->
            tab.text = when (position) {
                0 -> "Tất cả"
                1 -> "Đã trình ký"
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