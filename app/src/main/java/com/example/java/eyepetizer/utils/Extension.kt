package com.example.java.eyepetizer.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.ViewManager
import android.widget.Toast
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.custom.ankoView

/**
 * Created by Li on 2017/12/6.
 */
fun Context.showToast(message:String):Toast{
    var toast:Toast=Toast.makeText(this,message,Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.BOTTOM,0,60)
    toast.show()
    return toast
}
inline fun <reified T:Activity> Activity.newIntent(){
    val intent=Intent(this,T::class.java)
    startActivity(intent)
}
fun <T>Observable<T>.applySchedulers():Observable<T>{
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

inline fun ViewManager.video()=video(){}
inline fun ViewManager.video(init:StandardGSYVideoPlayer.()->Unit):StandardGSYVideoPlayer{
    return ankoView({StandardGSYVideoPlayer(it)},theme = 0,init = init)
}


