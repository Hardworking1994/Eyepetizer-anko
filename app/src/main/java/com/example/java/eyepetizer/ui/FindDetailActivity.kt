package com.example.java.eyepetizer.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.adapter.RankAdapter
import com.example.java.eyepetizer.mvp.contract.FindDetailContract
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.mvp.presenter.FindDetailPresenter
import com.gyf.barlibrary.ImmersionBar
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Li on 2018/1/19.
 */
class FindDetailActivity : AppCompatActivity(), FindDetailContract.View {


    var data: String? = null
    var mPresenter: FindDetailPresenter? = null
    var mAdapter: RankAdapter? = null
    var mIsRefresh: Boolean = false
    var mList: ArrayList<HotBean.ItemListBean.DataBean> = ArrayList()
    var mStart: Int = 10
    lateinit var name: String

    override fun setData(bean: HotBean) {
        val regEx = "[^0-9]"
        val pattern = Pattern.compile(regEx)
        val matcher = pattern.matcher(bean.nextPageUrl as CharSequence)
        data = matcher.replaceAll("").subSequence(1, matcher.replaceAll("").length - 1).toString()
        if (mIsRefresh) {
            mIsRefresh = false
            find<SwipeRefreshLayout>(R.id.sr_findDetail).isRefreshing = false
            if (mList.size > 0) {
                mList.clear()
            }
        }
        bean.itemList?.forEach {
            if (it.type=="video"){
                it.data?.let { it1 -> mList.add(it1) }
            }
        }
        mAdapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra("name")?.let {
            name = intent.getStringExtra("name")
        }
        mPresenter = FindDetailPresenter(this, this)
        mPresenter?.requestData(name, "date")
        mAdapter = RankAdapter(this, mList)
        FindDetailUi().setContentView(this)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()

    }


    class FindDetailUi : AnkoComponent<FindDetailActivity> {
        override fun createView(ui: AnkoContext<FindDetailActivity>): View = with(ui) {
            relativeLayout {
                lparams(width = matchParent, height = matchParent)
                toolbar {
                    imageView {
                        imageResource = R.drawable.ic_back_24dp
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        alpha = 0.6f
                        setOnClickListener {
                            ui.owner.onBackPressed()
                        }
                    }.lparams(width = dip(30), height = dip(30)) {
                        gravity = Gravity.LEFT
                    }
                    textView {
                        text = ui.owner.name
                        textSize = 16f
                        typeface = Typeface.createFromAsset(ui.owner.ctx.assets, "fonts/Lobster-1.4.otf")
                    }.lparams {
                        gravity = Gravity.CENTER
                    }
                    id = R.id.tb_find
                    backgroundColorResource = R.color.white
                }.lparams(width = matchParent, height = CommonUtil.getActionBarHeight(ui.owner)) {
                    alignParentTop()
                }
                swipeRefreshLayout {
                    id = R.id.sr_findDetail
                    recyclerView {
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ui.owner, LinearLayoutManager.VERTICAL, false)
                        id = R.id.rl_find_detail
                        adapter = ui.owner.mAdapter
                        addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                val linearLayoutManager = recyclerView?.layoutManager as LinearLayoutManager
                                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == ui.owner.mList.size - 1) {
                                    if (ui.owner.data != null) {
                                        ui.owner.mPresenter?.requestMoreData(ui.owner.mStart, ui.owner.name, "date")
                                        ui.owner.mStart = ui.owner.mStart.plus(10)
                                    }
                                }
                            }
                        })
                    }
                    setOnRefreshListener {
                        if (!ui.owner.mIsRefresh) {
                            ui.owner.mIsRefresh = true
                            ui.owner.mPresenter?.requestMoreData(ui.owner.mStart, ui.owner.name, "date")
                            ui.owner.mStart = ui.owner.mStart.plus(10)
                        }
                    }

                }.lparams(width = matchParent, height = matchParent) {
                    below(R.id.tb_find)
                }
            }
        }

    }

}