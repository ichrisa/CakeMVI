package com.chrisa.cakeapp.presentation.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.load(uri: String) {
    Glide.with(this.context)
        .load(uri)
        .placeholder(ColorDrawable(Color.GRAY))
        .thumbnail(0.1f)
        .into(this)
}