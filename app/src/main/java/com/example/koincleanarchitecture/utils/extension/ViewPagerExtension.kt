package com.example.koincleanarchitecture.utils.extension

import android.os.CountDownTimer
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.enableAutoScroll(interval: Long) {

    var countDownTimer: CountDownTimer? = null

    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {

            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(interval, interval) {

                override fun onTick(millisUntilFinished: Long) = Unit

                override fun onFinish() {
                    enableTrendingBannerAutoScroll(this@enableAutoScroll)
                }
            }.start()
        }
    })
}

private fun enableTrendingBannerAutoScroll(viewPager: ViewPager2) {

    viewPager.apply {
        val totalItems = adapter?.itemCount ?: 0
        val lastIndex = if (totalItems > 0) totalItems - 1 else 0
        val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1
        setCurrentItem(nextItem, true)
    }
}