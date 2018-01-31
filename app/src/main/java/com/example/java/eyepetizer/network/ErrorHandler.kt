package com.example.java.eyepetizer.network

import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Li on 2017/12/15.
 */

object ErrorHandler {
    fun handle(throwable: Throwable): BodyResponse<*>? {
        if (throwable is HttpException) {
            try {
                return BodyResponse(throwable.code(),throwable.message(),throwable.response())
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            throwable.printStackTrace()
        }
        return BodyResponse<String>(UNKNOW, "未知错误","位置错误")
    }

}
