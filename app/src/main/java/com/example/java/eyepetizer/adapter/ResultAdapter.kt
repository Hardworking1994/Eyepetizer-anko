package com.example.java.eyepetizer.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.ui.VideoDetailActivity
import com.example.java.eyepetizer.utils.ImageLoadUtils
import com.example.java.eyepetizer.utils.ObjectSaveUtils
import com.example.java.eyepetizer.utils.RealDurationUtils
import com.example.java.eyepetizer.utils.SPUtils
import org.jetbrains.anko.*
import java.text.SimpleDateFormat

/**
 * Created by Administrator on 2018/1/26.
 */
class ResultAdapter(context: Context, list: ArrayList<HotBean.ItemListBean.DataBean>) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    var mContext: Context? = null
    var mList: ArrayList<HotBean.ItemListBean.DataBean>? = null

    init {
        mContext = context
        mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResultViewHolder {
        return ResultViewHolder(createView(mContext!!))
    }

    private fun createView(context: Context): View {
        return with(context) {
            relativeLayout {
                lparams(width = matchParent, height = dip(100))
                imageView {
                    id = R.id.iv_result_photo
                    padding = dip(5)
                }.lparams(width = dip(180), height = matchParent) {
                    alignParentLeft()
                    centerHorizontally()
                }
                verticalLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    textView {
                        id=R.id.tv_result_title
                        maxLines = 2
                        padding = dip(5)
                        textSize = 14f
                        textColorResource = R.color.black
                        typeface = Typeface.createFromAsset(assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
                    }.lparams(width = matchParent, height = wrapContent)
                    textView {
                        id=R.id.tv_result_time
                        padding = dip(5)
                        textSize = 12f
                    }.lparams(width = matchParent, height = wrapContent)
                }.lparams(width = matchParent, height = matchParent) {
                    centerVertically()
                    rightOf(R.id.iv_result_photo)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ResultViewHolder?, position: Int) {
        val feed = mList?.get(position)?.cover?.feed
        feed?.let { ImageLoadUtils.display(mContext!!,holder?.photo,feed) }
        val title = mList?.get(position)?.title
        holder?.title?.text=title
        val category = mList?.get(position)?.category
        val duration = mList?.get(position)?.duration
        val releaseTime = mList?.get(position)?.releaseTime
        val simpleDateFormat = SimpleDateFormat("MM-dd")
        holder?.time?.text="$category/${RealDurationUtils.druation2RealTime(duration!!)}/${simpleDateFormat.format(releaseTime)}"
        holder?.itemView?.setOnClickListener {
            val description = mList?.get(position)?.description
            val playUrl = mList?.get(position)?.playUrl
            val blurred = mList?.get(position)?.cover?.blurred
            val collectionCount = mList?.get(position)?.consumption?.collectionCount
            val shareCount = mList?.get(position)?.consumption?.shareCount
            val replyCount = mList?.get(position)?.consumption?.replyCount
            val currentTimeMillis = System.currentTimeMillis()
            val videoBean=VideoBean(feed,title,description,duration,playUrl,category,blurred,collectionCount,shareCount,replyCount,currentTimeMillis)
            val url = SPUtils.getInstance(mContext!!, "beans").getString(playUrl!!)
            if(url==""){
                var count = SPUtils.getInstance(mContext!!, "beans").getInt("count")
                count = if(count!=-1){
                    count.inc()
                }else{
                    1
                }
                SPUtils.getInstance(mContext!!,"beans").put("count",count)
                SPUtils.getInstance(mContext!!,"beans").put(playUrl!!,playUrl)
                ObjectSaveUtils.saveObject(mContext!!,"bean$count",videoBean)
            }
            mContext?.startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
        }
    }


    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo:ImageView?=null
        var title:TextView?=null
        var time:TextView?=null
        init {
            photo=itemView?.find(R.id.iv_result_photo)
            title =itemView?.find(R.id.tv_result_title)
            time=itemView?.find(R.id.tv_result_time)
        }
    }

}