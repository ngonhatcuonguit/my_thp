package com.cuongngo.my_thp.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.databinding.ActivityMainBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ui.add_request.AddRequestFragment
import com.cuongngo.my_thp.ui.home.HomeFragment
import com.cuongngo.my_thp.ui.home.HomeViewModel
import com.cuongngo.my_thp.ui.list_request.ListRequestFragment
import com.cuongngo.my_thp.ui.login.UserViewModel
import com.cuongngo.my_thp.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppBaseActivityMVVM<ActivityMainBinding, UserViewModel>() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var navView: BottomNavigationView

    override val viewModel: UserViewModel by kodeinViewModel()
    override fun inflateLayout(): Int = R.layout.activity_main

    private var currentFragment = HomeFragment::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
//        if (AppPreferences.getUserAccessToken().isNotEmpty() && AppPreferences.getFcmToken().isNotEmpty()) {
//            viewModel.pushFcmToken(AppPreferences.getFcmToken())
//        }
        super.onCreate(savedInstanceState)
//        enableLightStatusBar()
        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.acb_primary)
        // Nếu đây là lần đầu tiên mở app, bạn có thể thêm Fragment ban đầu
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, HomeFragment()) // Thay thế với Fragment ban đầu
                .commit()
        }

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
                    val addRequestFragment =
                        fragmentManager.findFragmentByTag(HomeFragment.TAG)
                    val listRequestFragment =
                        fragmentManager.findFragmentByTag(HomeFragment.TAG)
                    if (profileFragment != null) transaction.remove(profileFragment)
                    if (addRequestFragment != null) transaction.remove(addRequestFragment)
                    if (listRequestFragment != null) transaction.remove(listRequestFragment)
                    transaction.commit()
                    currentFragment = HomeFragment.TAG
                }

//                R.id.navigation_list_form -> {
//                    val transaction = fragmentManager.beginTransaction()
//                    transaction.hide(homeFragment)
//                    val addRequestFragment =
//                        fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
//                    val profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG)
//                    val listRequestFragment =
//                        fragmentManager.findFragmentByTag(ListRequestFragment.TAG)
//
//                    if (profileFragment != null) transaction.remove(profileFragment)
//                    if (addRequestFragment != null) transaction.remove(addRequestFragment)
//
//                    if (listRequestFragment == null) {
//                        transaction.add(
//                            R.id.container,
//                            ListRequestFragment(),
//                            ListRequestFragment.TAG
//                        )
//                        transaction.commit()
//                        currentFragment = ListRequestFragment.TAG
//                    } else {
//
//                    }
//
//                }
//
//                R.id.navigation_add_form -> {
//                    val transaction = fragmentManager.beginTransaction()
//                    transaction.hide(homeFragment)
//                    val addRequestFragment =
//                        fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
//                    val profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG)
//                    val listRequestFragment =
//                        fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
//
//                    if (profileFragment != null) transaction.remove(profileFragment)
//                    if (listRequestFragment != null) transaction.remove(listRequestFragment)
//
//                    if (addRequestFragment == null) {
//                        transaction.add(
//                            R.id.container,
//                            AddRequestFragment(),
//                            AddRequestFragment.TAG
//                        )
//                        transaction.commit()
//                        currentFragment = AddRequestFragment.TAG
//                    } else {
//
//                    }
//
//                }
//
                R.id.navigation_profile -> {
                    val transaction = fragmentManager.beginTransaction()
                    transaction.hide(homeFragment)
                    val profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG)
                    val addRequestFragment =
                        fragmentManager.findFragmentByTag(AddRequestFragment.TAG)
                    val listRequestFragment =
                        fragmentManager.findFragmentByTag(ProfileFragment.TAG)

                    if (addRequestFragment != null) transaction.remove(addRequestFragment)
                    if (listRequestFragment != null) transaction.remove(listRequestFragment)
                    if (profileFragment == null) {
                        transaction.add(
                            R.id.container,
                            ProfileFragment(),
                            ProfileFragment.TAG
                        )
                        transaction.commit()
                        currentFragment = ProfileFragment.TAG
                    } else {

                    }
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        askNotificationPermission()

    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            //
        }
    }

    private fun askNotificationPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Do your task on permission granted
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        else{
            // Below Android 13 You don't need to ask for notification permission.
        }
    }

    // Hàm chuyển Fragment
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
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