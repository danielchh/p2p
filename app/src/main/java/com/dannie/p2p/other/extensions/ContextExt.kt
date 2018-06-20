package com.dannie.p2p.other.extensions

import android.content.Context
import android.content.res.Resources
import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.util.DisplayMetrics
import android.util.TypedValue


fun Context.getNavBarHeight(): Int{
    return getHeight("navigation_bar_height", this)
}

fun Context.getStatusBarHeight(): Int {
    return getHeight("status_bar_height", this)
}

private fun getHeight(itemName: String, context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier(itemName, "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.dp2px(@DimenRes resDp: Int) : Float{
    val dp = resources.getDimension(resDp)
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.getFloatDimen(@DimenRes resId: Int) : Float{
    val out = TypedValue()
    resources.getValue(resId, out, true)
    return out.float
}