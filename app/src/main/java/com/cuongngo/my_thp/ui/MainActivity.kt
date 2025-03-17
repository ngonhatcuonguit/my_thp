package com.cuongngo.my_thp.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.activity.AppBaseActivityMVVM
import com.cuongngo.my_thp.base.viewmodel.kodeinViewModel
import com.cuongngo.my_thp.databinding.ActivityMainBinding
import com.cuongngo.my_thp.ext.WTF
import com.cuongngo.my_thp.ui.add_request.AddRequestFragment
import com.cuongngo.my_thp.ui.home.HomeFragment
import com.cuongngo.my_thp.ui.home.HomeViewModel
import com.cuongngo.my_thp.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppBaseActivityMVVM<ActivityMainBinding, HomeViewModel>() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var navView: BottomNavigationView

    override val viewModel: HomeViewModel by kodeinViewModel()
    override fun inflateLayout(): Int = R.layout.activity_main

    private var currentFragment = HomeFragment::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Lấy token thành công
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
        }

//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                WTF("Fetching FCM registration token failed -- ${task.exception}")
//                return@addOnCompleteListener
//            }
//
//            // Lấy FCM token
//            val token = task.result
//            WTF("FCM Token: $token")
//        }
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "Default Channel"
//            val descriptionText = "Channel for notifications"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel("default_channel", name, importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }

        binding.clHome.setOnClickListener {
            handleNavBottom(1)
            switchFragment(HomeFragment())
        }
        binding.clQr.setOnClickListener {
            handleNavBottom(2)
//            switchFragment(AddRequestFragment())
        }
        binding.clChuyenTien.setOnClickListener {
            handleNavBottom(3)
//            switchFragment(ListRequestFragment())
            switchFragment(AddRequestFragment())
        }
        binding.clFabChuyenTien.setOnClickListener {
            handleNavBottom(3)
//            switchFragment(ListRequestFragment())
            switchFragment(AddRequestFragment())
        }
        binding.clThanhToan.setOnClickListener {
            handleNavBottom(4)
//            switchFragment(ListRequestFragment())
        }
        binding.clThem.setOnClickListener {
            handleNavBottom(5)
            switchFragment(ProfileFragment())
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
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Hàm chuyển Fragment
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
    private fun handleNavBottom(index: Int) {
        with(binding) {
            when (index) {
                1 -> {
                    setSateNavItem(true, 1)
                    //
                    setSateNavItem(false, 2)
                    setSateNavItem(false, 3)
                    setSateNavItem(false, 4)
                    setSateNavItem(false, 5)
                }

                2 -> {
                    setSateNavItem(true, 2)
                    //
                    setSateNavItem(false, 1)
                    setSateNavItem(false, 3)
                    setSateNavItem(false, 4)
                    setSateNavItem(false, 5)
                }

                3 -> {
                    setSateNavItem(true, 3)
                    //
                    setSateNavItem(false, 2)
                    setSateNavItem(false, 1)
                    setSateNavItem(false, 4)
                    setSateNavItem(false, 5)
                }

                4 -> {
                    setSateNavItem(true, 4)
                    //
                    setSateNavItem(false, 2)
                    setSateNavItem(false, 3)
                    setSateNavItem(false, 1)
                    setSateNavItem(false, 5)
                }

                5 -> {
                    setSateNavItem(true, 5)
                    //
                    setSateNavItem(false, 2)
                    setSateNavItem(false, 3)
                    setSateNavItem(false, 4)
                    setSateNavItem(false, 1)
                }

                else -> {
                    setSateNavItem(true, 1)
                    //
                    setSateNavItem(false, 2)
                    setSateNavItem(false, 3)
                    setSateNavItem(false, 4)
                    setSateNavItem(false, 5)
                }
            }
        }
    }

    private fun setSateNavItem(isActive: Boolean, index: Int) {
        with(binding) {
            when (index) {
                1 -> {
                    if (isActive) {
                        ivHome.setImageResource(R.drawable.ic_home_active)
                        tvHome.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_primary_2nd
                            )
                        )
                    } else {
                        ivHome.setImageResource(R.drawable.ic_home_inactive)
                        tvHome.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_gray_6e
                            )
                        )
                    }
                }

                2 -> {
                    if (isActive) {
                        ivQr.setImageResource(R.drawable.ic_qr)
                        tvQr.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_primary_2nd
                            )
                        )
                    } else {
                        ivQr.setImageResource(R.drawable.ic_qr)
                        tvQr.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_gray_6e
                            )
                        )
                    }
                }

                3 -> {
                    if (isActive) {
                        ivChuyenTien.setImageResource(R.drawable.ic_chuyen_tien_active)
                        tvChuyenTien.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_primary_2nd
                            )
                        )
                    } else {
                        ivChuyenTien.setImageResource(R.drawable.ic_chuyen_tien)
                        tvChuyenTien.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_gray_6e
                            )
                        )
                    }
                }

                4 -> {
                    if (isActive) {
                        ivThanhToan.setImageResource(R.drawable.ic_vi_active)
                        tvThanhToan.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_primary_2nd
                            )
                        )
                    } else {
                        ivThanhToan.setImageResource(R.drawable.ic_vi)
                        tvThanhToan.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_gray_6e
                            )
                        )
                    }
                }

                5 -> {
                    if (isActive) {
                        ivThem.setImageResource(R.drawable.ic_them_active)
                        tvThem.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_primary_2nd
                            )
                        )
                    } else {
                        ivThem.setImageResource(R.drawable.ic_them)
                        tvThem.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_gray_6e
                            )
                        )
                    }
                }

                else -> {
                    if (isActive) {
                        ivHome.setImageResource(R.drawable.ic_home_active)
                        tvHome.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_primary_2nd
                            )
                        )
                    } else {
                        ivHome.setImageResource(R.drawable.ic_home_inactive)
                        tvHome.setTextColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.acb_gray_6e
                            )
                        )
                    }
                }

            }
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