package com.example.java.eyepetizer.mvp.model

import android.content.Context
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.network.ApiService
import com.example.java.eyepetizer.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by Administrator on 2018/1/25.
 */
class ResultModel {
    fun loadData(context: Context,query:String,start:Int): Observable<HotBean>? {
        val instance = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = instance.create(ApiService::class.java)
        return apiService?.getSearchData(10,query,start)
    }
}