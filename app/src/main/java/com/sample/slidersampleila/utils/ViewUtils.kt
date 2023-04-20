package com.sample.slidersampleila.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.sample.slidersampleila.R
import kotlin.math.abs

fun ViewPager2.setCarouselEffects() {
    offscreenPageLimit = 1

    val nextItemVisiblePx = resources.getDimension(R.dimen.dp_5)
    val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.dp_15)
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
        page.alpha = 0.5f + (1 - abs(position))
    }
    setPageTransformer(pageTransformer)
}