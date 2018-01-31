package com.example.java.eyepetizer.mvp.model

import android.content.Context
import com.example.java.eyepetizer.mvp.model.bean.FindBean
import com.example.java.eyepetizer.network.ApiService
import com.example.java.eyepetizer.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by Li on 2018/1/19.
 */
class FindModel {
    fun loadData(context: Context): Observable<MutableList<FindBean>>? {
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = retrofitClient.create(ApiService::class.java)
        return apiService?.getFindData()
    }
}