package com.example.java.eyepetizer.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.adapter.DownLoadAdapter
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.utils.ObjectSaveUtils
import com.example.java.eyepetizer.utils.SPUtils
import com.gyf.barlibrary.ImmersionBar
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.recyclerview.v7.recyclerView
import zlc.season.rxdownload2.RxDownload

/**
 * Created by Administrator on 2018/1/26.
 */
class DownloadActivity :AppCompatActivity() {

    var mList=ArrayList<VideoBean>()
    lateinit var mAdapter:DownLoadAdapter
    private val mHandler:Handler= @SuppressLint("HandlerLeak")
    object:Handler(){
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
                list?.let { mList.addAll(list) }
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        DownloadUi().setContentView(this)
        DataAsyncTask(mHandler,this).execute()
        mAdapter=DownLoadAdapter(this,mList)
        mAdapter.setOnLongClickListener(object :DownLoadAdapter.OnLongClickListener{
            override fun onLongClick(position: Int) {
                addDialog(position)
            }
        })
        recyclerView?.adapter=mAdapter
    }

    private fun addDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        val dialog = builder.create()
        builder.setMessage("是否删除当前视频")

        builder.setNegativeButton("否",{
            dialog, which ->
            dialog.dismiss()
        })
        builder.setPositiveButton("是",{
            dialog, which ->
            deleteDownload(position)
        })
        builder.show()
    }

    private fun deleteDownload(position: Int) {
        RxDownload.getInstance(this).deleteServiceDownload(mList[position].playUrl,true).subscribe()
        SPUtils.getInstance(this,"downloads").put(mList[position].playUrl.toString(),"")
        var count=position+1
        ObjectSaveUtils.deleteFile("download$count",this)
        mList.removeAt(position)
        mAdapter.notifyDataSetChanged()
    }

    private class DataAsyncTask(handler: Handler,activity: DownloadActivity):AsyncTask<Void,Void,ArrayList<VideoBean>>(){
        var activity:DownloadActivity?=null
        var handler:Handler?=null
        init {
            this.activity=activity
            this.handler=handler
        }
        override fun doInBackground(vararg params: Void?): ArrayList<VideoBean> {
            var list=ArrayList<VideoBean>()
            var count:Int=SPUtils.getInstance(activity!!,"downloads").getInt("count")
            var i=1
            while (i <= count){
                var bean:VideoBean
                if(ObjectSaveUtils.getValue(activity!!,"download$i")== null){
                    continue
                }else{
                    bean=ObjectSaveUtils.getValue(activity!!,"download$i") as VideoBean
                }
                list.add(bean)
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
    var textView:TextView?=null
    var recyclerView:RecyclerView?=null
    class DownloadUi:AnkoComponent<DownloadActivity>{
        override fun createView(ui: AnkoContext<DownloadActivity>): View = with(ui){
            verticalLayout {
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
                        text="我的缓存"
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
                        layoutManager=LinearLayoutManager(ui.ctx)
                    }
                    ui.owner.textView=textView {
                        visibility=View.GONE
                        textSize=18f
                        textColorResource=R.color.black
                        text="暂无内容"
                        gravity=Gravity.CENTER
                    }.lparams(width= matchParent,height = matchParent)
                }.lparams(width= matchParent,height = matchParent)
            }
        }

    }

}