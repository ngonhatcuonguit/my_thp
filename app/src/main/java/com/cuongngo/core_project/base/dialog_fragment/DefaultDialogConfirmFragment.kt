package com.cuongngo.core_project.base.dialog_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.model.DialogModel
import com.cuongngo.core_project.databinding.DialogConfirmDefaultBinding
import com.cuongngo.core_project.utils.Func

class DefaultDialogConfirmFragment : DialogFragment() {

    private lateinit var binding: DialogConfirmDefaultBinding

    private var onLeftButtonClick: Func? = null
    private var onRightButtonClick: Func? = null

    private val dialogData: DialogModel? by lazy {
        arguments?.getSerializable(KEY_DIALOG_DATA) as DialogModel?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_confirm_default, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        binding.data = dialogData
        binding.apply {
            btnLeft.setOnClickListener {
                onLeftButtonClick?.invoke()
                dismiss()
            }
            btnRight.setOnClickListener {
                onRightButtonClick?.invoke()
                dismiss()
            }
        }
    }
    fun onLeftButtonClick(func: Func?): DefaultDialogConfirmFragment {
        this.onLeftButtonClick = func
        return this
    }
    fun onRightButtonClick(func: Func?): DefaultDialogConfirmFragment {
        this.onLeftButtonClick = func
        return this
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return object : BottomSheetDialog(requireContext(), theme){
//            override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//                val view: View? = currentFocus
//                val ret = super.dispatchTouchEvent(ev)
//                if (view is EditText) {
//                    currentFocus?.let {
//                        val w: View = it
//                        val scrcoords = IntArray(2)
//                        w.getLocationOnScreen(scrcoords)
//                        val x: Float = ev.rawX + w.left - scrcoords[0]
//                        val y: Float = ev.rawY + w.top - scrcoords[1]
//                        if (ev.action == MotionEvent.ACTION_UP
//                            && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
//                        ) {
//                            view.let {
//                                val inputMethodManager = requireContext().getSystemService(
//                                    Context.INPUT_METHOD_SERVICE
//                                ) as InputMethodManager
//                                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
//                            }
//                            when(currentFocus?.id){
//                                R.id.edt_content_feedback -> {
//                                    validateContent()
//                                }
//                            }
//                        }
//                    }
//                }
//                return ret
//            }
//        }
//    }

    companion object {
        val TAG = DefaultDialogConfirmFragment::class.simpleName
        const val KEY_DIALOG_DATA = "KEY_DIALOG_DATA"

        operator fun invoke(
            dialogData: DialogModel?
        ): DefaultDialogConfirmFragment = DefaultDialogConfirmFragment().apply {
            return DefaultDialogConfirmFragment().apply {
                arguments = bundleOf().apply {
                    putSerializable(KEY_DIALOG_DATA, dialogData)
                }
            }
        }

        fun newInstance(dialogData: DialogModel?): DefaultDialogConfirmFragment {
            val bundle = Bundle()
            bundle.apply {
                putSerializable(KEY_DIALOG_DATA, dialogData)
            }
            DefaultDialogConfirmFragment().apply {
                arguments = bundle
            }.also {
                return it
            }
        }

    }


}