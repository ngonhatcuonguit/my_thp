package com.cuongngo.core_project.ui.acb_app.lich_su_gd

import android.text.InputType
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.view.date_time_picker.DatePickerDialog
import com.cuongngo.core_project.base.view.date_time_picker.DateTimePickerDialog
import com.cuongngo.core_project.base.view.date_time_picker.Listener
import com.cuongngo.core_project.base.view.date_time_picker.ListenerDateTime
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.data.database.roomdb.entity.Field
import com.cuongngo.core_project.data.database.roomdb.entity.Option
import com.cuongngo.core_project.databinding.ActivityAddGdBinding
import com.cuongngo.core_project.ui.acb_app.GdViewModel
import com.cuongngo.core_project.ui.dropdown.onShowPopupOption
import java.util.Calendar

class AddGdActivity : AppBaseActivityMVVM<ActivityAddGdBinding, GdViewModel>() {
    override val viewModel: GdViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.activity_add_gd
    var dateTime: Calendar? = null

    var typeOptions = arrayListOf(
        Option(
            0,
            "Chuyển tiền"
        ),
        Option(
            1,
            "Nhận tiền"
        )
    )
    override fun setUp() {
        with(binding){
            loAppBar.tvTitle.text = "Thêm lịch sử giao dịch"
            edtSoTien.tvTitle.text = "Nhập số tiền giao dịch"
            edtSoTien.tvTitle.inputType = InputType.TYPE_CLASS_NUMBER
            edtNoiDung.tvTitle.text = "Nhập Nội dung giao dịch"
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
                        tvTypeGd.text = it.value
                    })
            }
            tvTime.setOnClickListener {
                val dateAdd: Calendar = Calendar.getInstance()
                dateAdd.add(Calendar.MINUTE, 5)
                // Minimum date is 80 years ago from today
                val minDateCal: Calendar = Calendar.getInstance()
                minDateCal.add(Calendar.YEAR, -80)
                DateTimePickerDialog(
                    this@AddGdActivity,
                    listener = object : ListenerDateTime {
                        override fun onDateTimeSelected(calendar: Calendar) {
                            dateTime = calendar
                            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                            val monthOfYear = calendar.get(Calendar.MONTH) + 1
                            val year = calendar.get(Calendar.YEAR)

                            val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                            val monthStr = if (monthOfYear < 10) "0${monthOfYear}" else "$monthOfYear"
                            tvTime.text = "$dayStr/$monthStr/$year"
                        }
                    },
                    maxDate = dateAdd.timeInMillis,
                    minDate = minDateCal.timeInMillis,
                    isCancelable = true
                ).show()
            }
        }
    }

    override fun setUpObserver() {
        //
    }

}