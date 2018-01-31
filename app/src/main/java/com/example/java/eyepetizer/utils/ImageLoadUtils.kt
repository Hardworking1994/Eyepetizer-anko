package com.example.java.eyepetizer.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.java.eyepetizer.R

/**
 * Created by Li on 2017/12/7.
 */
class ImageLoadUtils{
    companion object {
        fun display(context: Context,imageView: ImageView?,url:String){
            if (imageView==null){
                throw IllegalArgumentException("argument error")
            }
            Glide.with(context).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.ic_empty_picture)
                    .crossFade().into(imageView)
        }
        fun displayHigh(context: Context,imageView: ImageView?,url:String){
            if(imageView==null){
                throw IllegalArgumentException("argument error")
            }
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.drawable.ic_empty_picture)
                    .error(R.drawable.ic_empty_picture)
                    .into(imageView)


        }
    }
}