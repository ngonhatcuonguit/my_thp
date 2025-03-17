package com.cuongngo.my_thp.ui.event_thp

import android.os.Handler
import android.os.Looper
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.dialog_fragment.ConfirmDialog
import com.cuongngo.my_thp.base.model.DialogModel
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.database.roomdb.entity.Option
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.databinding.ActivityTietMucDetailBinding
import com.cuongngo.my_thp.ext.observeLiveDataChanged
import com.cuongngo.my_thp.services.network.onResultReceived
import com.cuongngo.my_thp.ui.dropdown.onShowPopupOption
import com.cuongngo.my_thp.ui.event_thp.model.Exam
import com.cuongngo.my_thp.ui.login.UserViewModel

class TietMucDetailActivity : AppBaseActivityMVVM<ActivityTietMucDetailBinding, UserViewModel>() {

    override val viewModel: UserViewModel by kodeinViewModel()

    companion object {
        val TAG = DanhSachGKActivity::class.java.simpleName
    }

    override fun inflateLayout(): Int = R.layout.activity_tiet_muc_detail

    var singScore = 10.0
    var composeScore = 10.0
    var currentExamStatus: Int? = null
    var currentExamId: Int? = null
    var isLast: Boolean? = false
    val handler = Handler(Looper.getMainLooper())

    var scoreOptions = arrayListOf(
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
        with(binding) {

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
                        if (it.value == "10") {
                            tvSingExtraScore.text = "0"
                        }
                    })
            }
            clSingScoreExtra.setOnClickListener {
                val defaultSingScoreExtra = scoreExtraOptions.find {
                    tvSingExtraScore.text.toString() == it.value
                } ?: scoreExtraOptions.firstOrNull()

                if (tvSingScore.text.toString() != "10") {
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
                        if (it.value == "10") {
                            tvComposeExtraScore.text = "0"
                        }
                    })
            }
            clComposeScoreExtra.setOnClickListener {
                val defaultComposeScoreExtra = scoreExtraOptions.find {
                    tvComposeExtraScore.text.toString() == it.value
                } ?: scoreExtraOptions.lastOrNull()
                if (tvComposeScore.text.toString() != "10") {
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
        observeLiveDataChanged(viewModel.updateScore) {
            it.onResultReceived(
                onLoading = {
                    if (isLast == true){
                        processDoneEventDialog.show()
                    }else{
                        processNextMusicDialog.show()
                    }
                },
                onSuccess = {
                    if(isLast != true){
                        handler.postDelayed({
                            viewModel.getTietMuc()
                        }, 3000)
                    }

                },
                onError = {
                    //
                }
            )
        }

        observeLiveDataChanged(viewModel.exam) {
            it.onResultReceived(
                onLoading = {
                    if (currentExamStatus == null) {
                        showProgressDialog()
                    }
                },
                onSuccess = { dataTietMuc ->
                    hideProgressDialog()
                    currentExamStatus = dataTietMuc.data?.data?.Status

                    isLast = dataTietMuc.data?.data?.IsLast
                    with(binding) {
                        tvExamName.text = dataTietMuc.data?.data?.Name ?: "Tiết mục đang biểu diễn"
                        tvExaminerName.text = "Giám khảo: ${AppPreferences.getGKInfo()?.Name}"
                        tvSinger.text = "Biểu diễn: ${dataTietMuc.data?.data?.UserName}"
                        tvType.text = "Sáng tác: ${dataTietMuc.data?.data?.TheLoai}"
                        tvDepartment.text =
                            "Phòng ban: ${dataTietMuc.data?.data?.Department} - ${dataTietMuc.data?.data?.Division}"

                        btnSubmit.setOnClickListener {
                            setupShowDialogConfirm(dataTietMuc.data?.data)
                        }
                    }
                    if (dataTietMuc.data?.data?.Status == 1 && currentExamId != dataTietMuc.data.data?.Id) {
                        processNextMusicDialog.hide()
                        currentExamId = dataTietMuc.data.data?.Id
                    } else {
                        handler.postDelayed({
                            viewModel.getTietMuc()
                        }, 3000)
                    }
                },
                onError = {
                    hideProgressDialog()
                }
            )
        }
    }

    private fun setupShowDialogConfirm(exam: Exam?) {
        singScore = binding.tvSingScore.text.toString()
            .toDouble() + ("0." + binding.tvSingExtraScore.text.toString()).toDouble()
        composeScore = binding.tvComposeScore.text.toString()
            .toDouble() + ("0." + binding.tvComposeExtraScore.text.toString()).toDouble()
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