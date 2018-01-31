package com.example.java.eyepetizer.mvp.presenter

import android.content.Context
import com.example.java.eyepetizer.mvp.contract.HotContract
import com.example.java.eyepetizer.mvp.model.HotModel
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.utils.applySchedulers

/**
 * Created by Administrator on 2018/1/23.
 */
class HotPresenter(context: Context,view:HotContract.View):HotContract.Presenter{
    var mContext:Context?=null
    var mView:HotContract.View?=null
    val mModel:HotModel by lazy {
        HotModel()
    }
    init {
        mContext=context
        mView=view
    }

    override fun start() {
    }

    override fun requestData(strategy: String) {
        val observable = mContext?.let { mModel.loadData(mContext!!, strategy) }
        observable?.applySchedulers()?.subscribe {
            bean:HotBean->mView?.setData(bean)
        }

    }

}