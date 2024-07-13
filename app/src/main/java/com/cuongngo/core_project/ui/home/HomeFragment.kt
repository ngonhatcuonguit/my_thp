package com.cuongngo.core_project.ui.home
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.search_form.ListFormActivity
import com.cuongngo.core_project.ui.search_form.form_adapter.FormAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerHelper
import io.reactivex.disposables.Disposable

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by kodeinViewModel()

    override fun inflateLayout() = R.layout.fragment_home

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var compositeDisposable: Disposable? = null

    private var currentKeyword: String? = null
    private var totalPages: Int = 1
    private var isMore: Boolean = true

    private lateinit var formAdapter: FormAdapter

    override fun setUp() {
        setupRcvListPopularForm()
        with(binding) {
            tvHintSearch.setOnClickListener {
                //
            }
            ivSearch.setOnClickListener {

            }
            tvPopularTitle.setOnClickListener{
                viewModel.getAllForm()
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

        observeLiveDataChanged(viewModel.allForm) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    it.data?.let { listForm ->
                        formAdapter.submitListForm(listForm)
                        WTF(ListFormActivity.TAG, "dataForm: ${listForm}")
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupRcvListPopularForm() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        formAdapter = FormAdapter(
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
        binding.rcvListPopularForm.apply {
            adapter = formAdapter
            layoutManager = gridLayoutManager
//            addOnScrollListener(scrollListener)
        }
    }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

}