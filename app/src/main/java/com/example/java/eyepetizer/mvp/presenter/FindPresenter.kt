package com.example.java.eyepetizer.mvp.presenter

import android.content.Context
import com.example.java.eyepetizer.mvp.contract.FindContract
import com.example.java.eyepetizer.mvp.model.FindModel
import com.example.java.eyepetizer.mvp.model.bean.FindBean
import com.example.java.eyepetizer.network.ErrorHandler
import com.example.java.eyepetizer.utils.applySchedulers
import com.example.java.eyepetizer.utils.showToast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Li on 2018/1/19.
 */
class FindPresenter (context: Context,view:FindContract.View):FindContract.Presenter {
    var mContext:Context?=null
    var mView:FindContract.View?=null
    private val model:FindModel by lazy { FindModel() }
    init {
        mContext=context
        mView=view
    }
    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable:Observable<MutableList<FindBean>> = mContext?.let { model.loadData(mContext!!) }!!
        observable?.applySchedulers()?.subscribe(subscriber)
    }
    private val subscriber = object : Observer<MutableList<FindBean>> {



        override fun onSubscribe(d: Disposable?) {

        }

        override fun onError(t: Throwable?) {
            val bodyResponse = t?.let { ErrorHandler.handle(it) }
            mContext!!.showToast("请检查网络连接:${bodyResponse?.code}")

        }

        override fun onComplete() {
        }

        override fun onNext(t: MutableList<FindBean>?) {
            if (t != null) {
                mView?.setData(t)
            }
        }

    }
}