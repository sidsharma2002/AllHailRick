package com.example.celebtalks.ui

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.celebtalks.R


fun View.slideUp(context: Context, animTime: Long, startOffset: Long) {
    val slideUp = loadAnimation(context, R.anim.slideup).apply {
        duration = animTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideUp)
}

fun slideUpViews(context: Context, vararg views: View, animTime: Long = 300L, delay: Long = 150L) {
    for(i in views.indices) {
        views[i].slideUp(context, animTime, delay * i)
    }
}