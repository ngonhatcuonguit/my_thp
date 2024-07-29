package com.cuongngo.core_project.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cuongngo.core_project.base.view.BaseView
import com.cuongngo.core_project.base.view.ProgressDialog
import com.cuongngo.core_project.utils.convertDpToPixel
import com.cuongngo.core_project.utils.getScreenWidth
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseDialog<DB : ViewDataBinding> : DialogFragment(), KodeinAware, BaseView {
    open lateinit var binding: DB

    override val kodein by kodein()

    private val appProgressDialog by lazy {
        ProgressDialog(requireContext())
    }

    private var showAsSystemDialog: Boolean = false

    @LayoutRes
    abstract fun inflateLayout(): Int

    override fun provideLoading(): ProgressDialog = appProgressDialog

    override fun provideContext(): Context? = requireContext()

    override fun provideRootView(): View? = view

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = getScreenWidth() - convertDpToPixel(80f, activity ?: return).toInt()
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, inflateLayout(), container, false)
        dialog?.window?.also {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        setUpObserver()
    }

    open fun showMatchParent() = false

    open fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, this::class.java.simpleName)
    }
}