package com.example.java.eyepetizer.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.R.color
import com.example.java.eyepetizer.adapter.RecordAdapter
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.utils.ObjectSaveUtils
import com.example.java.eyepetizer.utils.SPUtils
import com.gyf.barlibrary.ImmersionBar
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by Administrator on 2018/1/30.
 */
class RecordActivity: AppCompatActivity() {

    var mList=ArrayList<VideoBean>()
    lateinit var mAdapter:RecordAdapter

    private var handler:Handler= @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val list = msg?.data?.getParcelableArrayList<VideoBean>("beans")
            if(list?.isEmpty()!!){
                textView?.visibility=View.VISIBLE
            }else{
                textView?.visibility=View.GONE
                if(mList.size>0){
                    mList.clear()
                }
                list?.let { mList.addAll(it) }
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        RecordUi().setContentView(this)
        DataAsyncTask(handler,this).execute()
        mAdapter= RecordAdapter(this,mList)
        recyclerView?.adapter=mAdapter
    }

    private class DataAsyncTask(handler: Handler,activity: RecordActivity): AsyncTask<Void, Void, ArrayList<VideoBean>>() {
        var activity:RecordActivity?=null
        var handler:Handler?=null
        init {
            this.activity=activity
            this.handler=handler
        }
        override fun doInBackground(vararg params: Void?): ArrayList<VideoBean> {
            var list= ArrayList<VideoBean>()
            var count:Int=SPUtils.getInstance(activity!!,"beans").getInt("count")
            var i=1
            while (i <= count){
                val videoBean = ObjectSaveUtils.getValue(activity!!, "bean$i") as VideoBean
                list.add(videoBean)
                i++
            }
            return list
        }

        override fun onPostExecute(result: ArrayList<VideoBean>?) {
            super.onPostExecute(result)
            val message = handler?.obtainMessage()
            val bundle = Bundle()
            bundle.putParcelableArrayList("beans",result)
            message?.data=bundle
            handler?.sendMessage(message)
        }

    }

    var recyclerView:RecyclerView?=null
    var textView:TextView?=null
    class RecordUi:AnkoComponent<RecordActivity>{
        override fun createView(ui: AnkoContext<RecordActivity>): View = with(ui){
            verticalLayout {
                lparams(width= matchParent,height = matchParent)
                toolbar {
                    backgroundColorResource= color.white
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
                        text="观看记录"
                        typeface = Typeface.createFromAsset(ui.owner.ctx.assets, "fonts/Lobster-1.4.otf")
                    }.lparams {
                        gravity = Gravity.CENTER
                    }
                }.lparams(width= matchParent,height = CommonUtil.getActionBarHeight(ui.owner))
                view {
                    backgroundColorResource=R.color.textbg
                }.lparams(width= matchParent,height = dip(0.5f))

                frameLayout {
                    ui.owner.recyclerView=recyclerView {
                        layoutManager= LinearLayoutManager(ui.ctx)
                    }
                    ui.owner.textView=textView {
                        visibility=View.GONE
                        textSize=18f
                        textColorResource=R.color.black
                        text="暂无内容"
                        gravity=Gravity.CENTER
                    }.lparams(width= matchParent,height = matchParent)
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}