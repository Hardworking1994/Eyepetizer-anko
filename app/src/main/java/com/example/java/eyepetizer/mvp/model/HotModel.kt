package com.example.java.eyepetizer.mvp.model

import android.content.Context
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.network.ApiService
import com.example.java.eyepetizer.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by Administrator on 2018/1/23.
 */
class HotModel {
    fun loadData(context: Context,strategy:String?): Observable<HotBean>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = retrofitClient.create(ApiService::class.java)
        return strategy?.let({ apiService?.getHotData(10, it, "26868b32e808498db32fd51fb422d00175e179df", 83) })
    }
}