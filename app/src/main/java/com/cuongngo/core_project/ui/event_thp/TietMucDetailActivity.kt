package com.cuongngo.core_project.ui.event_thp

import android.os.Handler
import android.os.Looper
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.dialog_fragment.ConfirmDialog
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.FormEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.data.local.AppPreferences
import com.cuongngo.core_project.databinding.ActivityTietMucDetailBinding
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.dropdown.onShowPopupOption
import com.cuongngo.core_project.ui.event_thp.model.Exam
import com.cuongngo.core_project.ui.login.UserViewModel
import com.cuongngo.core_project.ui.request_detail.RequestMasterDetailActivity
import com.cuongngo.core_project.utils.Constants

class TietMucDetailActivity : AppBaseActivityMVVM<ActivityTietMucDetailBinding, UserViewModel>() {

    override val viewModel: UserViewModel by kodeinViewModel()

    companion object {
        val TAG = DanhSachGKActivity::class.java.simpleName
    }

    override fun inflateLayout(): Int = R.layout.activity_tiet_muc_detail

    var singScore = 10.0;
    var composeScore = 10.0;

    var scoreOptions = arrayListOf(
        Option(
            0,
            "0"
        ),
        Option(
            1,
            "1"
        ),
        Option(
            2,
            "2"
        ),
        Option(
            3,
            "3"
        ),
        Option(
            4,
            "4"
        ),
        Option(
            5,
            "5"
        ),
        Option(
            6,
            "6"
        ),
        Option(
            7,
            "7"
        ),
        Option(
            8,
            "8"
        ),
        Option(
            9,
            "9"
        ),
        Option(
            10,
            "10"
        ),
    )

    var scoreExtraOptions = arrayListOf(
        Option(
            0,
            "0"
        ),
        Option(
            25,
            "25"
        ),
        Option(
            5,
            "5"
        ),
        Option(
            75,
            "75"
        )
    )
    override fun setUp() {
        viewModel.getTietMuc()
        with(binding){

            clSingScore.setOnClickListener {
                val defaultSingScore = scoreOptions.find {
                    tvSingScore.text.toString() == it.value
                } ?: scoreOptions.lastOrNull()
                onShowPopupOption(
                    this@TietMucDetailActivity,
                    view = it,
                    listOption = scoreOptions,
                    optionDefault = defaultSingScore,
                    onSelectedListener = {
                        tvSingScore.text = it.value
                        if (it.value == "10"){
                            tvSingExtraScore.text = "0"
                        }
                    })
            }
            clSingScoreExtra.setOnClickListener {
                val defaultSingScoreExtra = scoreExtraOptions.find {
                    tvSingExtraScore.text.toString() == it.value
                } ?: scoreExtraOptions.firstOrNull()

                if(tvSingScore.text.toString() != "10"){
                    onShowPopupOption(
                        this@TietMucDetailActivity,
                        view = it,
                        listOption = scoreExtraOptions,
                        optionDefault = defaultSingScoreExtra,
                        onSelectedListener = {
                            tvSingExtraScore.text = it.value
                        })
                }
            }
            clComposeScore.setOnClickListener {
                val defaultComposeScore = scoreOptions.find {
                    tvComposeScore.text.toString() == it.value
                } ?: scoreOptions.lastOrNull()
                onShowPopupOption(
                    this@TietMucDetailActivity,
                    view = it,
                    listOption = scoreOptions,
                    optionDefault = defaultComposeScore,
                    onSelectedListener = {
                        tvComposeScore.text = it.value
                        if (it.value == "10"){
                            tvComposeExtraScore.text = "0"
                        }
                    })
            }
            clComposeScoreExtra.setOnClickListener {
                val defaultComposeScoreExtra = scoreExtraOptions.find {
                    tvComposeExtraScore.text.toString() == it.value
                } ?: scoreExtraOptions.lastOrNull()
                if (tvComposeScore.text.toString() != "10"){
                    onShowPopupOption(
                        this@TietMucDetailActivity,
                        view = it,
                        listOption = scoreExtraOptions,
                        optionDefault = defaultComposeScoreExtra,
                        onSelectedListener = {
                            tvComposeExtraScore.text = it.value
                        })
                }
            }
        }
    }

    override fun setUpObserver() {

        observeLiveDataChanged(viewModel.updateScore){
            it.onResultReceived(
                onLoading = {
                    processSendFileDialog.show()
                },
                onSuccess = {
                    processSendFileDialog.hide()
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        viewModel.getTietMuc()
                    }, 3000)

                },
                onError = {
                    processSendFileDialog.hide()
                }
            )
        }

        observeLiveDataChanged(viewModel.exam) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = { dataTietMuc ->
                hideProgressDialog()
                    with(binding){
                        tvExamName.text = dataTietMuc.data?.data?.Name ?: "Tiết mục đang biểu diễn"
                        tvExaminerName.text = "Giám khảo: ${AppPreferences.getGKInfo()?.Name}"
                        tvSinger.text = "Biểu diễn: ${dataTietMuc.data?.data?.UserName}"
                        tvType.text = "Sáng tác: ${dataTietMuc.data?.data?.TheLoai}"
                        tvDepartment.text = "Phòng ban: ${dataTietMuc.data?.data?.Department} - ${dataTietMuc.data?.data?.Division}"

                        btnSubmit.setOnClickListener {
                            setupShowDialogConfirm(dataTietMuc.data?.data)
                        }
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupShowDialogConfirm(exam: Exam?) {
        singScore = binding.tvSingScore.text.toString().toDouble() + ("0."+ binding.tvSingExtraScore.text.toString()).toDouble()
        composeScore = binding.tvComposeScore.text.toString().toDouble() + ("0."+ binding.tvComposeExtraScore.text.toString()).toDouble()
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = "Xác nhận chấm điểm cho tiết mục",
                subTitle = "",
                content = "Tiết mục: ${exam?.Name.toString()} - ${exam?.UserName.toString()} \nPhòng ban: ${exam?.Department} - ${exam?.Division}\nĐiểm biểu diễn: ${singScore.toString()}     Điểm sáng tác: ${composeScore.toString()}",
                leftButtonTitle = "Huỷ bỏ",
                rightButtonTitle = "Đồng ý",
                isSingle = false
            )
        ).apply {
            onRightButtonClick {
                viewModel.updateScore(
                    examinerId = AppPreferences.getGKInfo()?.Id ?: return@onRightButtonClick,
                    examId = exam?.Id ?: return@onRightButtonClick,
                    score = singScore,
                    composingScore = composeScore
                )
                dismiss()
            }
            onLeftButtonClick {
                dismiss()
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    override fun onBackPressed() {
        //
    }
}