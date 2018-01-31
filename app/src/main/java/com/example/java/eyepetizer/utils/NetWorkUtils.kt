package com.example.java.eyepetizer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by Li on 2017/12/6.
 */
object NetWorkUtils {
    fun isNetConnected(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo:NetworkInfo? = connectivityManager.activeNetworkInfo
        if(activeNetworkInfo==null){
            return false
        }else{
            return activeNetworkInfo.isAvailable&&activeNetworkInfo.isConnected
        }

    }
    fun isNetWorkConnected(context: Context,typeMobile:Int):Boolean{
        if(!isNetConnected(context)){
            return false
        }
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo:NetworkInfo = connectivityManager.getNetworkInfo(typeMobile)
        if(networkInfo==null){
            return false
        }else{
            return networkInfo.isConnected&&networkInfo.isAvailable
        }
    }
    fun isPhoneNetConnected(context: Context): Boolean {
        val typeMobile = ConnectivityManager.TYPE_MOBILE
        return isNetWorkConnected(context,typeMobile)
    }

    fun isWifiNetConnected(context: Context) : Boolean{
        val typeMobile = ConnectivityManager.TYPE_WIFI
        return  isNetWorkConnected(context,typeMobile)
    }


}