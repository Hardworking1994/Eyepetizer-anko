package com.example.java.eyepetizer.network

/**
 * Created by Li on 2017/12/15.
 */
data class BodyResponse<T>(var code: Int, var message:String, var data:T)