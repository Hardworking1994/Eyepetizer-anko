package com.example.java.eyepetizer.mvp.presenter

import android.content.Context
import com.example.java.eyepetizer.mvp.contract.ResultContract
import com.example.java.eyepetizer.mvp.model.ResultModel
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.utils.applySchedulers

/**
 * Created by Administrator on 2018/1/25.
 */
class ResultPresenter(context: Context,view:ResultContract.View):ResultContract.Presenter {

    var mContext:Context?=null
    var mView:ResultContract.View?=null

    private val mModel:ResultModel by lazy {
        ResultModel()
    }
    init {
        mContext=context
        mView=view
    }


    override fun requestData(query: String, start: Int) {
        val observable = mContext?.let { mModel.loadData(it, query, start) }
        observable?.applySchedulers()?.subscribe {
            bean:HotBean->mView?.setData(bean)
        }
    }

    override fun start() {
    }
}