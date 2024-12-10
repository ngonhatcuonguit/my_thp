package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.text.InputType
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.view.date_time_picker.DateTimePickerDialog
import com.cuongngo.core_project.base.view.date_time_picker.ListenerDateTime
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.GdEntity
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.data.database.roomdb.entity.randomString
import com.cuongngo.core_project.databinding.ActivityAddGdBinding
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import com.cuongngo.core_project.ui.acb_app.GdViewModel
import com.cuongngo.core_project.ui.dropdown.onShowPopupOption
import com.cuongngo.core_project.utils.toast.showMessageToast
import java.util.Calendar
import kotlin.random.Random

class AddGdActivity : AppBaseActivityMVVM<ActivityAddGdBinding, GdViewModel>() {
    override val viewModel: GdViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_add_gd
    var dateTime: Calendar = Calendar.getInstance()
    var type: String? = null

    var typeOptions = arrayListOf(
        Option(
            0,
            "Chuyen tien"
        ),
        Option(
            1,
            "Nhan tien"
        )
    )

    override fun setUp() {
        with(binding) {
            loAppBar.tvTitle.text = "Thêm lịch sử giao dịch"
            edtSoTien.tvTitle.text = "Nhập số tiền giao dịch"
            edtSoTien.edtValue.hint = "Vui lòng nhập số tiền giao dịch"
            edtSoTien.edtValue.inputType = InputType.TYPE_CLASS_NUMBER
            edtNoiDung.tvTitle.text = "Nhập Nội dung giao dịch"
            edtNoiDung.edtValue.hint = "Vui lòng nhập nội dung"
            tvTypeGd.setOnClickListener {
                val defaultType = typeOptions.find {
                    tvTypeGd.text.toString() == it.value
                } ?: typeOptions.lastOrNull()
                onShowPopupOption(
                    this@AddGdActivity,
                    view = it,
                    listOption = typeOptions,
                    optionDefault = defaultType,
                    onSelectedListener = {
                        type = it.value
                        tvTypeGd.text = it.value
                    })
            }
            tvTime.setOnClickListener {
                val dateAdd: Calendar = Calendar.getInstance()
                dateAdd.add(Calendar.MINUTE, 5)
                // Minimum date is 80 years ago from today
                val minDateCal: Calendar = Calendar.getInstance()
                minDateCal.add(Calendar.YEAR, -8)
                DateTimePickerDialog(
                    this@AddGdActivity,
                    listener = object : ListenerDateTime {
                        override fun onDateTimeSelected(calendar: Calendar) {
                            dateTime = calendar
                            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                            val monthOfYear = calendar.get(Calendar.MONTH) + 1
                            val year = calendar.get(Calendar.YEAR)
                            val hour = calendar.get(Calendar.HOUR_OF_DAY)
                            val minute = calendar.get(Calendar.MINUTE)

                            val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                            val monthStr =
                                if (monthOfYear < 10) "0${monthOfYear}" else "$monthOfYear"
                            tvTime.text = "$hour:$minute $dayStr/$monthStr/$year"
                        }
                    },
                    maxDate = dateAdd.timeInMillis,
                    minDate = minDateCal.timeInMillis,
                    isCancelable = true
                ).show()
            }
            btnAddGd.setOnClickListener {
                if (type.isNullOrEmpty() || edtSoTien.edtValue.text.isNullOrEmpty() || edtNoiDung.edtValue.text.isNullOrEmpty()) {
                    showMessageToast(
                        this@AddGdActivity,
                        false,
                        "",
                        "Chưa chọn/điền đủ thông tin giao dịch"
                    )
                } else {
                    viewModel.upsetGD(
                        GdEntity(
                            id = Random.nextLong(1, 99999),
                            transactionCode = randomString(10),
                            transactionType = type.toString(),
                            transactionAmount = edtSoTien.edtValue.text.toString(),
                            transactionContent = edtNoiDung.edtValue.text.toString(),
                            transactionDate = dateTime
                        )
                    )
                }
            }
        }
    }

    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.gdId) {
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    showMessageToast(
                        this@AddGdActivity,
                        true,
                        "Thêm thành công",
                        ""
                    )
                },
                onError = {
                    hideProgressDialog()
                    showMessageToast(
                        this@AddGdActivity,
                        false,
                        "",
                        "Lỗi"
                    )
                }
            )
        }
    }

}