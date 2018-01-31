package com.example.java.eyepetizer.mvp.contract

import com.example.java.eyepetizer.base.BasePresenter
import com.example.java.eyepetizer.base.BaseView
import com.example.java.eyepetizer.mvp.model.bean.HotBean

/**
 * Created by lvruheng on 2017/7/5.
 */
interface FindDetailContract {
    interface View : BaseView<Presenter> {
        fun setData(bean: HotBean)
    }

    interface Presenter : BasePresenter {
        fun requestData(categoryName: String, strategy: String)
    }
}