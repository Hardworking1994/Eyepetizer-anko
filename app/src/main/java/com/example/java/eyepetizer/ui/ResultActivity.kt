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
import com.example.java.eyepetizer.adapter.ResultAdapter
import com.example.java.eyepetizer.mvp.contract.ResultContract
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.mvp.presenter.ResultPresenter
import com.gyf.barlibrary.ImmersionBar
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by Administrator on 2018/1/25.
 */
class ResultActivity:AppCompatActivity(),ResultContract.View {

    lateinit var keyWord:String
    lateinit var mPresenter:ResultPresenter
    lateinit var mAdapter:ResultAdapter
    var mIsRefresh:Boolean=false
    var mList=ArrayList<HotBean.ItemListBean.DataBean>()
    var start:Int=10

    override fun setData(bean: HotBean) {
        if(mIsRefresh){
            mIsRefresh=false
            find<SwipeRefreshLayout>(R.id.sr_result).isRefreshing=false
            if(mList.size>0){
                mList.clear()
            }
        }

        bean.itemList?.forEach {
            if(it.type=="video")
            it.data?.let { it1->mList.add(it1) }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        keyWord=intent.getStringExtra("keyWord")
        mPresenter= ResultPresenter(this,this)
        mPresenter.requestData(keyWord,start)
        mAdapter=ResultAdapter(this,mList)
        ResultUI().setContentView(this)
    }

    class ResultUI:AnkoComponent<ResultActivity>{
        override fun createView(ui: AnkoContext<ResultActivity>): View = with(ui){
            verticalLayout{
                lparams(width= matchParent,height = matchParent)
                toolbar {
                    backgroundColorResource= R.color.white
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
                        textSize = 16f
                        typeface = Typeface.createFromAsset(ui.owner.ctx.assets, "fonts/Lobster-1.4.otf")
                        text="${ui.owner.keyWord}相关"
                    }.lparams {
                        gravity = Gravity.CENTER
                    }
                }.lparams(width= matchParent,height = CommonUtil.getActionBarHeight(ui.owner))
                view {
                    backgroundColorResource=R.color.textbg
                }.lparams(width= matchParent,height = dip(0.5f))
                swipeRefreshLayout{
                    id=R.id.sr_result
                    onRefresh {
                        if(!ui.owner.mIsRefresh){
                            ui.owner.mIsRefresh=true
                            ui.owner.start=10
                            ui.owner.mPresenter.requestData(ui.owner.keyWord,ui.owner.start)
                        }
                    }
                    recyclerView {
                        lparams(width= matchParent,height = matchParent)
                        layoutManager=LinearLayoutManager(ui.ctx)
                        adapter=ui.owner.mAdapter
                        addOnScrollListener(object:RecyclerView.OnScrollListener(){
                            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                                super.onScrollStateChanged(recyclerView, newState)
                                val linearLayoutManager = layoutManager as LinearLayoutManager
                                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItemPosition==ui.owner.mList.size-1){
                                    ui.owner.start=ui.owner.start.plus(10)
                                    ui.owner.mPresenter.requestData(ui.owner.keyWord,ui.owner.start)
                                }
                            }
                        })
                    }
                }.lparams(width= matchParent,height = matchParent)
            }
        }

    }
}