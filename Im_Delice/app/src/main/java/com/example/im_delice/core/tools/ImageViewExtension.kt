package com.example.im_delice.core.tools

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.desdeUrl(url:String){
    Picasso.get().load(url).into(this)
}