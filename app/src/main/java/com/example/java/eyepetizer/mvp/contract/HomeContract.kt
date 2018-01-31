package com.example.java.eyepetizer.mvp.contract

import com.example.java.eyepetizer.base.BasePresenter
import com.example.java.eyepetizer.base.BaseView
import com.example.java.eyepetizer.mvp.model.bean.HomeBean

/**
 * Created by lvruheng on 2017/7/5.
 */
interface HomeContract{
    interface View : BaseView<Presenter> {
        fun setData(bean : HomeBean?)
        fun isRefresh(): Boolean?
        fun cancleRefresh()
    }
    interface Presenter : BasePresenter {
        fun requestData()
    }
}