package com.cuongngo.my_thp.base.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.dialog_fragment.ConfirmDialog
import com.cuongngo.my_thp.base.model.DialogModel
import com.cuongngo.my_thp.base.view.BaseView
import com.cuongngo.my_thp.base.view.ProgressDialog
import com.cuongngo.my_thp.ui.event_thp.DanhSachGKActivity
import com.cuongngo.my_thp.ui.event_thp.TietMucDetailActivity
import com.cuongngo.my_thp.ui.login.LoginMethodActivity
import com.skydoves.transformationlayout.onTransformationStartContainer
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein


abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity(), KodeinAware, BaseView {
    open lateinit var binding: DB
    override val kodein by kodein()

    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    val processSyncDialog by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_process_sync_data, null)
        )
    }

    val processUploadDialog by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_process_upload_data, null)
        )
    }
    val processSendFileDialog by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_process_send_file, null)
        )
    }

    val processNextMusicDialog by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_next_music, null)
        )
    }
    val processDoneEventDialog by lazy {
        ProgressDialog(
            this,
            view = LayoutInflater.from(this).inflate(R.layout.dialog_done_animation, null)
        )
    }

    @LayoutRes
    abstract fun inflateLayout(): Int

    override fun provideLoading(): ProgressDialog = progressDialog

    override fun provideContext(): Context? = this

    override fun provideRootView(): View? = binding.root

    open fun getStatusBarColor() = R.color.dark

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, inflateLayout())
        hideSystemUI()
        setStatusBarColor(getStatusBarColor())
        setUp()
        setUpObserver()
    }

    open fun setDefaultStatusBarColor() {
        window.also {
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.statusBarColor = ContextCompat.getColor(applicationContext, R.color.dark)
        }
    }

    open fun enableLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = window
            val decorView: View = window.decorView
            WindowInsetsControllerCompat(window, decorView).also { wic ->
                wic.isAppearanceLightStatusBars = true // true or false as desired.
            }
            window.statusBarColor = customStatusBarColor
        } else {
            setDefaultStatusBarColor()
        }


        /** DEPRECATED -> not use it, just for learning something old
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
         **/
    }

    @ColorInt
    open val customStatusBarColor: Int = Color.TRANSPARENT

    protected fun setStatusBarColor(@DrawableRes id: Int) {
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * If increase version code to 30 (R) , remove brackets
     * */
    private fun hideSystemUI() {
        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
              window.setDecorFitsSystemWindows(false)
              window.insetsController?.let {
                  it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                  it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
              }
          } else {*/
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        /*}*/
    }

    fun isShowKeyBoard(): Boolean {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
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

    open fun setupShowDialogResult(
        isSuccess: Boolean,
        errorCode: Int? = null,
        showContent: String? = null
    ) {
        var title = "Thành Công"
        var content = showContent
        var btnContent = "Đồng ý"
        if (!isSuccess) {
            when (errorCode) {
                401 -> {
                    title = "Phiên đăng nhập hết hạn"
                    content = "Phiên đăng nhập của bạn đã quá hạn, vui lòng đăng nhập lại!"
                    btnContent = "Đăng nhập"
                }

                404 -> {
                    title = "Lỗi 404"
                    content = "Đã có lỗi xảy ra, vui lòng kiểm tra lại!"
                    btnContent = "Đồng ý"
                }

                else -> {
                    if (isNetworkAvailable(this)) {
//                        showMessageToast(
//                            this,
//                            true,
//                            contentDone = "Thiết bị đã được kết nối internet",
//                            contentFail = ""
//                        )
                        title = "Lỗi $errorCode"
                        content = "Đã có lỗi xảy ra, vui lòng kiểm tra lại!"
                        btnContent = "Đồng ý"
                    } else {
                        title = "Chú ý"
                        content = "Thiết bị chưa được kết nối mạng. Vui lòng kết nối mạng trước khi đồng bộ dữ liệu!"
                        btnContent = "Đồng ý"
                    }
                }
            }
        } else {
            title = "Thành công"
            content = showContent
            btnContent = "Đồng ý"
        }
        val confirmDialog = ConfirmDialog(
            DialogModel(
                title = title,
                subTitle = "",
                content = content,
                leftButtonTitle = btnContent,
                rightButtonTitle = "",
                isSingle = true
            ),
            margins = 90f
        ).apply {
            onRightButtonClick {
                dismiss()
            }
            onLeftButtonClick {
                if (!isSuccess) {
                    when (errorCode) {
                        401 -> {
                            gotoLoginMethod()
                        }

                        404 -> {
                            //
                            dismiss()
                        }

                        else -> {
                            dismiss()
                            //
                        }
                    }
                } else {
                    dismiss()
                }
            }
        }
        confirmDialog.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    open fun gotoLoginMethod() {
        Intent(this, LoginMethodActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }

    open fun gotoListGK() {
        Intent(this, DanhSachGKActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }
    open fun gotoTietMucDetail() {
        Intent(this, TietMucDetailActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }

}