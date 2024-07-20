package com.cuongngo.core_project.ui.list_request.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cuongngo.core_project.ui.list_request.ListRequestPagerFragment

class CatrgoryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val categories = listOf(MY_REQUEST, WAIT_FOR_ME, COMPLETED)
    override fun getItemCount() = categories.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ListRequestPagerFragment()
        fragment.arguments = Bundle().apply {
            putString("CATEGORY", categories[position])
        }
        return fragment
    }

    companion object {
        const val MY_REQUEST = "my_request"
        const val WAIT_FOR_ME = "wait_for_me"
        const val COMPLETED = "completed"
    }


}