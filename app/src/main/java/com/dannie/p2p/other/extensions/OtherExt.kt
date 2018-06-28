package com.dannie.p2p.other.extensions

import android.support.annotation.DrawableRes
import android.util.Log
import android.widget.ImageView
import com.dannie.p2p.R
import com.squareup.picasso.Picasso

fun ImageView.setImageUriPicasso(uriString: String){
    Picasso.get()
            .load(uriString).fit().centerCrop()
            .placeholder(R.drawable.avatar_placeholder)
            .into(this)
}

fun <T> T.log(message: String){
    Log.d("MyLog", message)
}