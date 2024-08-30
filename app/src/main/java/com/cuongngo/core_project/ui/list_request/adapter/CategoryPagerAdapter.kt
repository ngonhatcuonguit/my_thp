package com.cuongngo.core_project.ui.list_request.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cuongngo.core_project.ui.list_request.ListRequestPagerFragment

class CategoryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val categories = listOf(ALL_REQUEST, SENT_REQUEST, COMPLETED)
    override fun getItemCount() = categories.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ListRequestPagerFragment()
        fragment.arguments = Bundle().apply {
            putString(REQUEST_CATEGORY, categories[position])
        }
        return fragment
    }

    companion object {
        const val ALL_REQUEST = "all_request"
        const val SENT_REQUEST = "sent_request"
        const val COMPLETED = "completed"
        const val REQUEST_CATEGORY = "REQUEST_CATEGORY"
    }


}