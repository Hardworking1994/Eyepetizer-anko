package com.example.java.eyepetizer.mvp.presenter

import android.content.Context
import com.example.java.eyepetizer.mvp.contract.FindDetailContract
import com.example.java.eyepetizer.mvp.model.FindDetailModel
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.utils.applySchedulers

/**
 * Created by Li on 2018/1/19.
 */
class FindDetailPresenter(context: Context,view:FindDetailContract.View):FindDetailContract.Presenter {
    var mContext:Context?=null
    var mView:FindDetailContract.View?=null
    val mModel: FindDetailModel by lazy {
        FindDetailModel()
    }
    init {
        mContext=context
        mView=view
    }


    override fun start() {
    }

    override fun requestData(categoryName:String,strategy:String) {
        val observable = mContext?.let { mModel.loadData(mContext!!, categoryName, strategy) }
        observable?.applySchedulers()?.subscribe {
            hotBean:HotBean->mView?.setData(hotBean)
            println("hotbean:"+hotBean.toString())
        }
    }
    fun requestMoreData(start:Int,categoryName: String,strategy: String){
        val observable = mContext?.let { mModel.loadMoreData(mContext!!, start, categoryName, strategy) }
        observable?.applySchedulers()?.subscribe {
            hotBean:HotBean->mView?.setData(hotBean)
        }
    }

}