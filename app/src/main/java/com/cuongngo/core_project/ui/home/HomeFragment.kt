package com.cuongngo.core_project.ui.home
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.fragment.BaseFragmentMVVM
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.common.collection.EndlessRecyclerViewScrollListener
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomHeaderList
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomProcessStep
import com.cuongngo.core_project.data.database.roomdb.entity.generateRandomSheet
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.FragmentHomeBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.ui.search_form.form_adapter.FormAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerAdapter
import com.cuongngo.core_project.ui.view_pager.ViewPagerHelper
import com.cuongngo.core_project.utils.Constants
import io.reactivex.disposables.Disposable
import kotlin.random.Random

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
        WTF("testApiUser token ${AppPreferences.getUserAccessToken()}")
        syncUser()
        setupRcvListPopularForm()
        binding.apply {
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
    val headers = generateRandomHeaderList(6)
    val sheets = List(1){ generateRandomSheet() }
    val processSteps = List(2) { generateRandomProcessStep() }
    fun addForm() {
        viewModel.insertForm(
            FormEntity(
                formID = Random.nextLong(1, 1000),
                name = listOf("HRM form test", "Factory form test", "Parameter form test", "Office form", "Other form").random(),
                status = listOf(1,2,3,4,5).random().toString(),
                form_code = randomString(10),
                schema_code = randomString(10),
                category = listOf("HRM", "Factory", "Parameter", "Office", "Other").random(),
                schema_name = randomString(10),
                list_body = sheets,
                list_header = headers,
                process_steps = processSteps
            )
        )

    }
    private fun syncUser() = if(AppPreferences.getCountRecordLocalUser() == 0){
        viewModel.getListUser(true)
    }else{
        viewModel.getListUser(false)
    }

    override fun onResume() {
        super.onResume()
    }

    private val sliderRunnable = Runnable {
        binding.vpTopViewpager.currentItem += 1
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.getListUser){
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testApiUser getRemote ${it.data?.data}")
                    if(it.data?.data?.isEmpty() != true){
                        it.data?.data.let{
                            viewModel.addListUser(
                                it ?: arrayListOf()
                            )
                        }
                    }
                },
                onError = {
                    processSyncDialog.hide()
                }
            )
        }

        observeLiveDataChanged(viewModel.checkUserTable){
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    WTF("testApiUser count ${it.data}")
                    AppPreferences.setCountRecordLocalUser(it.data ?: 0)
                },
                onError = {}
            )
        }

        observeLiveDataChanged(viewModel.insertListUserToLocal){
            it.onResultReceived(
                onLoading = {
                    processSyncDialog.show()
                },
                onSuccess = {
                    processSyncDialog.hide()
                    WTF("testApiUser -----------------sync ok")
                    viewModel.getCountUserLocal()
                },
                onError = {
                    processSyncDialog.hide()
                }
            )
        }


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
                    }
//                    if ((it.data?.size ?: 0) < 3){
//                        addForm()
//                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }

        observeLiveDataChanged(viewModel.formId) {
            it.onResultReceived(
                onLoading = {},
                onSuccess = {
                    hideProgressDialog()
                    viewModel.getAllForm()
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }

    }

    private fun setupShowDialogConfirm(form: FormEntity) {
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = "Tạo yêu cầu mới",
                subTitle = form.title,
                content = "Bạn muốn tạo một yêu cầu mới với mẫu form: \n${form.form_code} - ${form.name}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Tạo yêu cầu",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                startActivity(
                    RequestMasterDetailActivity.newIntent(
                        requireContext(),
                        category = Constants.CategoryRequestDetail.ADD,
                        request = null,
                        form = form
                    )
                )
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    private fun setupRcvListPopularForm() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        formAdapter = FormAdapter(
            requireContext(),
            arrayListOf(),
            onItemClickListener = {
                setupShowDialogConfirm(it)
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