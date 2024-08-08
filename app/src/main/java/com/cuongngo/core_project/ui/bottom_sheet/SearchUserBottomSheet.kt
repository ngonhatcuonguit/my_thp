package com.cuongngo.core_project.ui.bottom_sheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.bottom_sheet.FullHeightBottomSheet
import com.cuongngo.core_project.base.model.WrapperModel
import com.cuongngo.core_project.data.database.roomdb.entity.RequestEntity
import com.cuongngo.core_project.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.core_project.databinding.FragmentSearchUserBinding
import com.cuongngo.core_project.ext.nullableCast
import com.cuongngo.core_project.utils.TFunc
import com.cuongngo.core_project.utils.getScreenHeight

class SearchUserBottomSheet : FullHeightBottomSheet<FragmentSearchUserBinding>() {

    companion object {
        const val DEFAULT_LIST_USER = "DEFAULT_LIST_USER"
        const val REQUEST_DATA = "REQUEST_DATA"
        const val BOTTOM_SHEET_HEIGHT_VALUE = "BOTTOM_SHEET_HEIGHT_VALUE"
        var DEFAULT_HEIGHT = (getScreenHeight() * 0.95).toInt()
        operator fun invoke(listSelected: List<UserTHPEntity>?, requestEntity: RequestEntity, heightValue: Int? = DEFAULT_HEIGHT): SearchUserBottomSheet {
            return SearchUserBottomSheet().apply {
                arguments = bundleOf(
                    DEFAULT_LIST_USER to WrapperModel(listSelected),
                    REQUEST_DATA to requestEntity,
                    BOTTOM_SHEET_HEIGHT_VALUE to heightValue
                )
            }
        }
    }

    private var onConfirmSelected: TFunc<List<UserTHPEntity>?>? = null
    private var heightValue: Int = SelectFieldValueBottomSheet.DEFAULT_HEIGHT
    private var requestEntity : RequestEntity? = null
    private var listSelected : List<UserTHPEntity>? = null

    private val listUserSelected by lazy {
        (arguments?.getSerializable(DEFAULT_LIST_USER).nullableCast<WrapperModel<List<UserTHPEntity>>>())?.data?: emptyList()
    }
    override fun inflateLayout(): Int = R.layout.fragment_search_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.listSelected = listUserSelected
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.getAllUser
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            onConfirmSelected?.invoke(listSelected)
            dismiss()
        }
    }

    private fun setupObservers() {
        //
    }

    private fun setupRecyclerView(listSelected: List<UserTHPEntity>?) {
        binding.rvListResult.also { recyclerView->
            recyclerView.layoutManager = LinearLayoutManager(activity ?: return)
            recyclerView.adapter =
//                ColorPickerMultiChoiceAdapter(
//                    listColors,
//                    listColorsDefault
//                ) { listColors ->
//                    this.listColors = listColors
//                }
        }
    }

}