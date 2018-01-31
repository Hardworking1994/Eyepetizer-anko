package com.example.java.eyepetizer.mvp.presenter

import android.content.Context
import com.example.java.eyepetizer.mvp.contract.HomeContract
import com.example.java.eyepetizer.mvp.model.HomeModel
import com.example.java.eyepetizer.mvp.model.bean.HomeBean
import com.example.java.eyepetizer.network.ErrorHandler
import com.example.java.eyepetizer.utils.applySchedulers
import com.example.java.eyepetizer.utils.showToast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Li on 2017/12/6.
 */
class HomePresenter(context: Context, view: HomeContract.View) : HomeContract.Presenter {
    var mContext: Context? = null
    var mView: HomeContract.View? = null
    val mModel: HomeModel by lazy {
        HomeModel()
    }
    private val subscriber = object : Observer<HomeBean> {

        override fun onSubscribe(d: Disposable?) {

        }

        override fun onError(t: Throwable?) {
            val bodyResponse = t?.let { ErrorHandler.handle(it) }
            mContext!!.showToast("请检查网络连接:${bodyResponse?.code}")
            if (mView?.isRefresh()!!) {
                mView?.cancleRefresh()
            }
        }

        override fun onComplete() {
        }

        override fun onNext(t: HomeBean?) {
            mView?.setData(t)
        }

    }

    init {
        mView = view
        mContext = context

    }

    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable: Observable<HomeBean>? = mContext?.let {
            mModel.loadData(it, true, "0")
        }

        observable?.applySchedulers()?.subscribe(subscriber)
    }

    fun moreData(data: String) {
        val observable: Observable<HomeBean>? = mContext?.let {
            mModel.loadData(it, false, data)
        }
        observable?.applySchedulers()?.subscribe(subscriber)
    }

}