package com.example.java.eyepetizer.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.java.eyepetizer.adapter.HomeAdatper
import com.example.java.eyepetizer.mvp.contract.HomeContract
import com.example.java.eyepetizer.mvp.model.bean.HomeBean
import com.example.java.eyepetizer.mvp.presenter.HomePresenter
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Li on 2017/12/5.
 */
class HomeFragment : BaseFragment(), HomeContract.View {
    override fun isRefresh(): Boolean? {
        return refreshLayout?.isRefreshing
    }

    override fun cancleRefresh() {
        refreshLayout?.isRefreshing = false
        mIsRefresh = false
    }

    var mIsRefresh: Boolean = false
    var mPresenter: HomePresenter? = null
    var mList = ArrayList<HomeBean.IssueListBean.ItemListBean>()
    var mAdapter: HomeAdatper? = null
    var data: String? = null
    var refreshLayout: SwipeRefreshLayout? = null
    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter = HomePresenter(context, this)
        mPresenter?.start()
        mAdapter = HomeAdatper(context, mList)
        return UI {
            refreshLayout = swipeRefreshLayout {
                setOnRefreshListener {
                    moreData()
                }
                recyclerView = recyclerView {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = mAdapter
                    addOnScrollListener(onScrollListener())
                }
            }
        }.view
    }

    override fun setData(bean: HomeBean?) {
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean?.nextPageUrl)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()
        if (mIsRefresh) {
            mIsRefresh = false
            refreshLayout?.isRefreshing = false
            if (mList.size > 0) {
                mList.clear()
            }
        }
        bean?.issueList!!
                .flatMap { it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { mList.add(it) }
        mAdapter?.notifyDataSetChanged()
    }


    override fun initView() {

//        recyclerView?.layoutManager = LinearLayoutManager(context)
//        recyclerView?.setHasFixedSize(true)
//        recyclerView?.adapter = mAdapter
//        refreshLayout?.setOnRefreshListener(this)
//        recyclerView?.addOnScrollListener(object : OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                var layoutManager: LinearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
//                var lastPositon = layoutManager.findLastVisibleItemPosition()
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPositon == mList.size - 1) {
//                    if (data != null) {
//                        mPresenter?.moreData(data!!)
//
//                    }
//
//                }
//            }
//        })

    }

    inner class onScrollListener : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var layoutManager: LinearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
            var lastPositon = layoutManager.findLastVisibleItemPosition()
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPositon == mList.size - 1) {
                if (data != null) {
                    mPresenter?.moreData(data!!)

                }

            }
        }
    }

    fun moreData() {

        if (!mIsRefresh) {
            mIsRefresh = true
            if (data == null) {
                data = "0"
            }
            mPresenter?.moreData(data!!)!!
        }
    }
}