package com.cuongngo.core_project.ui.onboard

import android.content.Intent
import androidx.viewpager2.widget.ViewPager2
import com.cuongngo.core_project.data.local.AppPreferences.setShowOnBoard
import com.cuongngo.core_project.R
import com.cuongngo.core_project.base.activity.BaseActivity
import com.cuongngo.core_project.databinding.ActivityOnboardBinding
import com.cuongngo.core_project.ui.MainActivity

class OnBoardActivity : BaseActivity<ActivityOnboardBinding>() {

    override fun inflateLayout(): Int = R.layout.activity_onboard

    override fun setUp() {

        binding.vpOnBoard.apply {
            val listOnBoard = listOf(
                OnBoardingModel(R.drawable.image_onboard_step1),
                OnBoardingModel(R.drawable.image_onboard_step2),
                OnBoardingModel(R.drawable.image_onboard_step3)
            )
            adapter = OnBoardAdapter(listOnBoard)
        }

        binding.flSkip.setOnClickListener {
            setShowOnBoard(true)
            gotoMain()
        }

        binding.btnNext.setOnClickListener {
            val current = binding.vpOnBoard.currentItem
            val total = binding.vpOnBoard.adapter?.itemCount ?: 3
            if (current < total-1) {
                binding.vpOnBoard.currentItem = current + 1
                when (current) {
                    2 -> {
                        binding.tvOnboardSlogan.text = "Create and Submit Requests"
                        binding.tvContent.text =
                            "Quickly create new approval requests using our simple form. Fill in the necessary details, attach relevant documents, and submit for review."
                    }
                    3 -> {
                        binding.tvOnboardSlogan.text =
                            "Security and Compliance"
                        binding.tvContent.text =
                            "We prioritize your data security and ensure compliance with industry standards. Your information is encrypted and securely stored"
                    }
                }
            } else {
                setShowOnBoard(true)
                gotoMain()
            }
        }

        binding.dotsIndicator.attachTo(binding.vpOnBoard)

        binding.vpOnBoard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.tvOnboardSlogan.text =
                            "Welcome to E-Approval!"
                        binding.tvContent.text =
                            "Welcome to E-Approval, your go-to app for streamlining approval workflows. Get started quickly and manage your approvals efficiently"
                    }
                    1 -> {
                        binding.tvOnboardSlogan.text = "Create and Submit Requests"
                        binding.tvContent.text =
                            "Quickly create new approval requests using our simple form. Fill in the necessary details, attach relevant documents, and submit for review."
                    }
                    2 -> {
                        binding.tvOnboardSlogan.text =
                            "Security and Compliance"
                        binding.tvContent.text =
                            "We prioritize your data security and ensure compliance with industry standards. Your information is encrypted and securely stored"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

        })
    }

    private fun gotoMain() {
        Intent(this, MainActivity::class.java).apply {
        }.also {
            finish()
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        //do nothing
    }

    override fun setUpObserver() {

    }
}