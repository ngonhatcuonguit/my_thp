package com.cuongngo.core_project.ui.test_room_db
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.AppBaseActivityMVVM
import com.cuongngo.core_project.base.viewmodel.kodeinViewModel
import com.cuongngo.core_project.databinding.ActivityRecordProcessBinding
import com.cuongngo.core_project.ext.WTF
import com.cuongngo.core_project.data.database.roomdb.entity.RecordProcessEntity
import com.cuongngo.core_project.ext.observeLiveDataChanged
import com.cuongngo.core_project.services.network.onResultReceived
import kotlinx.coroutines.launch

class RecordProcessActivity : AppBaseActivityMVVM<ActivityRecordProcessBinding, RecordProcessViewModel>() {

    override val viewModel: RecordProcessViewModel by kodeinViewModel()
    private var recordProcessEntity: RecordProcessEntity? = null

    companion object  {
        val TAG= RecordProcessActivity::class.java.simpleName

    }

    override fun inflateLayout(): Int = R.layout.activity_record_process

    override fun onResume() {
        super.onResume()
        viewModel.getRecordProcess("nhiet do")
    }

    override fun setUp() {
        if (recordProcessEntity == null){
            viewModel.getRecordProcess("nhiet do")
        }
        binding.bthSave.setOnClickListener {
            upsertRecordProcess()
            finish()
        }
    }

    override fun onBackPressed() {
        upsertRecordProcess()
        super.onBackPressed()
    }
    override fun setUpObserver() {
        observeLiveDataChanged(viewModel.recordProcess){
            it.onResultReceived(
                onLoading = {
                    showProgressDialog()
                },
                onSuccess = {
                    hideProgressDialog()
                    binding.edtTemperature.setText(it.data?.value.toString().orEmpty())
                    recordProcessEntity = it.data
                    WTF("testLocalDB ${it.data}")
                },
                onError = {
                    hideProgressDialog()
                }

            )
        }
    }

    fun upsertRecordProcess(){
        recordProcessEntity?.let {
            WTF("testOnback1 $recordProcessEntity")
            viewModel.upsertRecordProcess(
                RecordProcessEntity(
                    id = it.id,
                    name = it.name,
                    value = binding.edtTemperature.text.toString().toDouble(),
                    note = it.note
                )
            )
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view: View? = currentFocus
        val ret = super.dispatchTouchEvent(event)
        if (view is EditText) {
            currentFocus?.let {
                val w: View = it
                val scrcoords = IntArray(2)
                w.getLocationOnScreen(scrcoords)
                val x: Float = event.rawX + w.left - scrcoords[0]
                val y: Float = event.rawY + w.top - scrcoords[1]
                if (event.action == MotionEvent.ACTION_UP
                    && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
                ) {
                    hideKeyboard()

                }
            }
        }
        return ret
    }

}