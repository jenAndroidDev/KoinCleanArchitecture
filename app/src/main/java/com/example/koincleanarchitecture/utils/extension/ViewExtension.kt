package com.example.koincleanarchitecture.utils.extension

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}


fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}


fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.setOnDebounceListener(onClick: (View) -> Unit) {

    val debounceOnClickListener = object : DebounceClickListener() {
        override fun onDebounceClick(view: View) {
            onClick(view)
        }
    }
    setOnClickListener(debounceOnClickListener)
}

fun View.setOnDebounceListenerShort(onClick: (View) -> Unit) {
    val debounceOnClickListener = object : CreatorsDebounceClickListener() {
        override fun onDebounceClick(view: View) {
            onClick(view)
        }
    }
    setOnClickListener(debounceOnClickListener)
}

fun View.addCornerRadius(radius: Float) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            view?.let {
                outline?.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
    }
    //set clip
    clipToOutline = true
}

fun View.fadeOutAnimation(
    duration: Long = 300,
    visibility: Int = View.INVISIBLE,
    completion: (() -> Unit)? = null,
) {
    if (isVisible) {
        animate().alpha(0f).setDuration(duration).withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }

        isEnabled = false
    }
}

fun View.fadeInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    if (!isVisible) {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).setDuration(duration).withEndAction {
            completion?.let {
                it()
            }
        }

        isEnabled = true
    }
}

fun TextView.setTextAnimation(
    text: String,
    duration: Long = 300,
    completion: (() -> Unit)? = null,
) {
    fadeOutAnimation(duration) {
        this.text = text
        fadeInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}


