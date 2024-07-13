package com.cuongngo.core_project.ui.home
import androidx.lifecycle.lifecycleScope
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.view_pager.ViewPagerAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerHelper

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()

    override fun inflateLayout() = R.layout.fragment_home

    private var totalPages: Int = 1
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun setUp() {
        with(binding) {
            tvHintSearch.setOnClickListener {
                //
            }
            ivSearch.setOnClickListener {

            }
        }

    }

    private val sliderRunnable = Runnable {
        binding.vpTopViewpager.currentItem += 1
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.hotNew){
            it.onResultReceived(
                onLoading = {

                },
                onSuccess = {
                    if (it.data?.data?.isNotEmpty() == true){
                       ViewPagerHelper(
                           viewPager2 = binding.vpTopViewpager,
                           defaultPos = 0,
                           viewPagerAdapter = ViewPagerAdapter(
                               data = it.data.data ?: return@onResultReceived,
                               viewPager2 = binding.vpTopViewpager,
                               onItemClick = {
                                   //set event on click
                               }
                           ),
                           onPageChanged = {}
                       ).autoScroll(lifecycleScope, 3000).execute()
                    }
                },
                onError = {

                }
            )
        }
    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

}