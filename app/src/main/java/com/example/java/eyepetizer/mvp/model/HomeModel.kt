package com.example.java.eyepetizer.mvp.model

import android.content.Context
import com.example.java.eyepetizer.mvp.model.bean.HomeBean
import com.example.java.eyepetizer.network.ApiService
import com.example.java.eyepetizer.network.RetrofitClient
import io.reactivex.Observable

/**
 * Created by Li on 2017/12/5.
 */
class HomeModel {
    fun loadData(context:Context,isFirst:Boolean,data:String?):Observable<HomeBean>?{
        val retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        val apiService = retrofitClient.create(ApiService::class.java)
        when(isFirst){
            true->return apiService?.getHomeData()
            false->return apiService?.getHomeMoreData(data.toString(),"2")
        }

    }
}