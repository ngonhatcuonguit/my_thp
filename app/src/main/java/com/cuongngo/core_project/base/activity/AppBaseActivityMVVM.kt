package com.cuongngo.core_project.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.cuongngo.core_project.base.viewmodel.BaseViewModel

abstract class AppBaseActivityMVVM<DB: ViewDataBinding, VM: BaseViewModel>: BaseActivity<DB>() {

    abstract val viewModel: VM

    open fun onCreateX(savedInstanceState: Bundle?){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
        onCreateX(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    open fun showLoading(){

    }

    open fun hideLoading(){

    }
}