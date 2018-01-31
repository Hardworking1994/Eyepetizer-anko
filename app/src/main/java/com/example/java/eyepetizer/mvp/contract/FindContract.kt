package com.example.java.eyepetizer.mvp.contract

import com.example.java.eyepetizer.base.BasePresenter
import com.example.java.eyepetizer.base.BaseView
import com.example.java.eyepetizer.mvp.model.bean.FindBean

/**
 * Created by lvruheng on 2017/7/6.
 */
interface FindContract{
    interface View : BaseView<Presenter> {
        fun setData(beans : MutableList<FindBean>)
    }
    interface Presenter : BasePresenter {
        fun requestData()
    }
}