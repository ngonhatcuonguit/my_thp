package com.cuongngo.core_project.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.BaseActivity
import com.cuongngo.core_project.databinding.ActivityMainBinding
import com.cuongngo.core_project.ui.add_request.AddRequestFragment
import com.cuongngo.core_project.ui.home.HomeFragment
import com.cuongngo.core_project.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var navView: BottomNavigationView

    override fun inflateLayout(): Int = R.layout.activity_main

    private var currentFragment = HomeFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLightStatusBar()
    }

    override fun setUp() {
        navView = findViewById(R.id.nav_bottom)
        val fragmentManager: FragmentManager = supportFragmentManager
        homeFragment = HomeFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.frame_container, homeFragment, HomeFragment.TAG).commit()

        navView.setOnNavigationItemSelectedListener {
            if (navView.selectedItemId == it.itemId) {
                if (it.itemId == R.id.navigation_home) {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.commit()
                }
                return@setOnNavigationItemSelectedListener false
            }
            when (it.itemId) {
                R.id.navigation_home -> {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.show(homeFragment)
                    val profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG)
                    val addRequestFragment = fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
                    if (profileFragment != null) transaction.remove(profileFragment)
                    if (addRequestFragment != null) transaction.remove(addRequestFragment)
                    transaction.commit()
                    currentFragment = HomeFragment.TAG
                }
                R.id.navigation_add_form -> {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.hide(homeFragment)
                    val addRequestFragment = fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
                    val profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG)
                    if (profileFragment != null) transaction.remove(profileFragment)
                    if (addRequestFragment == null){
                        transaction.add(
                            R.id.container,
                            AddRequestFragment(),
                            AddRequestFragment.TAG
                        )
                        transaction.commit()
                        currentFragment = AddRequestFragment.TAG
                    }else {

                    }

                }
                R.id.navigation_profile -> {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.hide(homeFragment)
                    val profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG)
                    val addRequestFragment = fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
                    if (addRequestFragment != null) transaction.remove(addRequestFragment)
                    if (profileFragment == null){
                        transaction.add(
                            R.id.container,
                            ProfileFragment(),
                            ProfileFragment.TAG
                        )
                        transaction.commit()
                        currentFragment = ProfileFragment.TAG
                    }else {

                    }
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        //do nothing
    }

    override fun setUpObserver() {

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