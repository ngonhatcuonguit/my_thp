package com.cuongngo.my_thp.ui.acb_app.lich_su_gd

import android.content.Intent
import androidx.core.view.isVisible
import com.cuongngo.my_thp.App
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.view.date_time_picker.DatePickerDialog
import com.cuongngo.my_thp.base.view.date_time_picker.Listener
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.database.roomdb.entity.gdFilter
import com.cuongngo.my_thp.databinding.ActivityFilterGdBinding
import com.cuongngo.my_thp.ui.acb_app.GdViewModel
import java.util.Calendar

class FilterLsGdActivity : AppBaseActivityMVVM<ActivityFilterGdBinding, GdViewModel>() {
    override val viewModel: GdViewModel by kodeinViewModel()
    override fun inflateLayout(): Int = R.layout.activity_filter_gd

    var filter = gdFilter(
        soNgay = 30,
        startDate = Calendar.getInstance(),
        endDate = Calendar.getInstance(),
    )
    override fun setUp() {
        with(binding) {
            loAppBar.ivBack.setOnClickListener {
                finish()
            }
            loAppBar.tvTitle.text = "Lịch sử giao dịch"
            loAppBar.tvTitle.setTextColor(App.getResources().getColor(R.color.black_1c))
            loAppBar.clFilter.setBackgroundColor(App.getResources().getColor(R.color.white))
            loAppBar.ivFilter.setImageResource(R.drawable.ic_close_black)
            loAppBar.ivFilter.setOnClickListener {
                finish()
            }

            tv30Day.setOnClickListener {
                tv30Day.setTextColor(App.getResources().getColor(R.color.white))
                tv30Day.setBackgroundColor(App.getResources().getColor(R.color.acb_primary))

                tv7day.setTextColor(App.getResources().getColor(R.color.acb_like_black_text))
                tv7day.setBackgroundColor(App.getResources().getColor(R.color.white))
                tvTuyChon.setTextColor(App.getResources().getColor(R.color.acb_like_black_text))
                tvTuyChon.setBackgroundColor(App.getResources().getColor(R.color.white))

                clStart.isVisible = false
                clEnd.isVisible = false

                filter = gdFilter(
                    soNgay = 30,
                    startDate = Calendar.getInstance(),
                    endDate = Calendar.getInstance(),
                )
            }
            tv7day.setOnClickListener {
                tv7day.setTextColor(App.getResources().getColor(R.color.white))
                tv7day.setBackgroundColor(App.getResources().getColor(R.color.acb_primary))

                tv30Day.setTextColor(App.getResources().getColor(R.color.acb_like_black_text))
                tv30Day.setBackgroundColor(App.getResources().getColor(R.color.white))
                tvTuyChon.setTextColor(App.getResources().getColor(R.color.acb_like_black_text))
                tvTuyChon.setBackgroundColor(App.getResources().getColor(R.color.white))

                clStart.isVisible = false
                clEnd.isVisible = false

                filter = gdFilter(
                    soNgay = 7,
                    startDate = Calendar.getInstance(),
                    endDate = Calendar.getInstance(),
                )
            }
            tvTuyChon.setOnClickListener {
                tvTuyChon.setTextColor(App.getResources().getColor(R.color.white))
                tvTuyChon.setBackgroundColor(App.getResources().getColor(R.color.acb_primary))

                tv7day.setTextColor(App.getResources().getColor(R.color.acb_like_black_text))
                tv7day.setBackgroundColor(App.getResources().getColor(R.color.white))
                tv30Day.setTextColor(App.getResources().getColor(R.color.acb_like_black_text))
                tv30Day.setBackgroundColor(App.getResources().getColor(R.color.white))

                filter = gdFilter(
                    soNgay = 0,
                    startDate = Calendar.getInstance(),
                    endDate = Calendar.getInstance(),
                )
                clStart.isVisible = true
                clEnd.isVisible = true
            }

            tvStartDate.setOnClickListener {
                showDatePickerDialog(0)
            }

            tvEndDate.setOnClickListener {
                showDatePickerDialog(1)
            }

            tvUpdateFilter.setOnClickListener {
                val resultIntent = Intent().apply {
                    putExtra("filter", filter)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }

        }
    }

    private fun showDatePickerDialog(type: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.MINUTE, 5)
        // Minimum date is 8 years ago from today
        val minDateCal: Calendar = Calendar.getInstance()
        minDateCal.add(Calendar.YEAR, -8)

        DatePickerDialog(
            calendar = cal,
            context = this,
            listener = object : Listener {
                override fun onDateSelected(calendar: Calendar) {
                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                    val monthOfYear = calendar.get(Calendar.MONTH) + 1
                    val year = calendar.get(Calendar.YEAR)

                    val dayStr = if (dayOfMonth < 10) "0${dayOfMonth}" else "$dayOfMonth"
                    val monthStr = if (monthOfYear < 10) "0${monthOfYear}" else "$monthOfYear"
                    if (type == 0) {
                        filter = filter.copy(
                            soNgay = 0,
                            startDate = calendar
                        )
                        binding.tvStartDate.text = "$dayStr/$monthStr/$year"
                    } else {
                        filter = filter.copy(
                            soNgay = 0,
                            endDate = calendar
                        )
                        binding.tvEndDate.text = "$dayStr/$monthStr/$year"
                    }
                }
            },
            maxDate = cal.timeInMillis,
            minDate = minDateCal.timeInMillis,
            isCancelable = true
        ).show()
    }

    override fun setUpObserver() {

    }

}