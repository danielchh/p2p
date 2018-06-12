package com.dannie.p2p.other.extensions

import android.content.Context

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