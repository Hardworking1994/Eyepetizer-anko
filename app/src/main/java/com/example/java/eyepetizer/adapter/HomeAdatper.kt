package com.example.java.eyepetizer.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.mvp.model.bean.HomeBean
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.ui.VideoDetailActivity
import com.example.java.eyepetizer.utils.ImageLoadUtils
import com.example.java.eyepetizer.utils.ObjectSaveUtils
import com.example.java.eyepetizer.utils.SPUtils
import org.jetbrains.anko.*

/**
 * Created by Li on 2017/12/7.
 */
class HomeAdatper(context: Context, list: MutableList<HomeBean.IssueListBean.ItemListBean>?) : RecyclerView.Adapter<HomeAdatper.HomeViewHolder>() {

    var context: Context? = null
    var list: MutableList<HomeBean.IssueListBean.ItemListBean>? = null
//    var inflater: LayoutInflater? = null

    init {
        this.context = context
        this.list = list
//        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder {
//        inflater?.inflate(R.layout.item_home,parent,false)
        return HomeViewHolder(creatItemView(context!!))
    }

    private fun creatItemView(context: Context): View {
        return with(context) {
            relativeLayout {
                lparams(width = matchParent, height = dip(250))
                imageView {
                    id = R.id.iv_photo
                }.lparams(width = matchParent, height = dip(200))
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    imageView {
                        id = R.id.iv_user
                        padding = dip(10)
                    }.lparams(width = dip(50), height = dip(50)) {
                        gravity = Gravity.CENTER
                    }
                    relativeLayout {
                        textView {
                            id = R.id.tv_title
                            typeface = Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
                            maxLines = 1
                            textSize = 15f
                            padding = dip(5)
                        }.lparams(width = wrapContent, height = wrapContent) {
                            alignParentTop()
                        }
                        textView {
                            id = R.id.tv_detail
                            padding = dip(5)
                        }.lparams(width = wrapContent, height = wrapContent) {
                            alignParentBottom()
                        }
                    }.lparams(width = wrapContent, height = wrapContent) {
                        leftMargin = dip(15)
                    }

                }.lparams(width = matchParent, height = dip(50)) {
                    below(R.id.iv_photo)
                }

            }
        }
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        var bean = list?.get(position)
        var title = bean?.data?.title
        var category = bean?.data?.category
        var minute = bean?.data?.duration?.div(60)
        var second = bean?.data?.duration?.minus((minute?.times(60)) as Long)
        var realMinute: String
        var realSecond: String
        if (minute!! < 10) {
            realMinute = "0" + minute
        } else {
            realMinute = minute.toString()
        }
        if (second!! < 10) {
            realSecond = "0" + second
        } else {
            realSecond = second.toString()
        }
        var playUrl = bean?.data?.playUrl
        var photo = bean?.data?.cover?.feed
        var author = bean?.data?.author
        ImageLoadUtils.display(context!!, holder?.iv_photo, photo as String)
        holder?.tv_title?.text = title
        holder?.tv_detail?.text = "发布于 $category/$realMinute:$realSecond"
        if (author != null) {
            ImageLoadUtils.display(context!!, holder?.iv_user, author.icon)
        } else {
            holder?.iv_user?.visibility = View.GONE
        }
        holder?.itemView?.setOnClickListener {
            //            //跳转视频详情页
            var intent : Intent = Intent(context, VideoDetailActivity::class.java)
            var desc = bean?.data?.description
            var duration = bean?.data?.duration
            var playUrl = bean?.data?.playUrl
            var blurred = bean?.data?.cover?.blurred
            var collect = bean?.data?.consumption?.collectionCount
            var share = bean?.data?.consumption?.shareCount
            var reply = bean?.data?.consumption?.replyCount
            var time = System.currentTimeMillis()
            var videoBean  = VideoBean(photo,title,desc,duration,playUrl,category,blurred,collect ,share ,reply,time)
            var url = SPUtils.getInstance(context!!,"beans").getString(playUrl!!)
            if(url.equals("")){
                var count = SPUtils.getInstance(context!!,"beans").getInt("count")
                if(count!=-1){
                    count = count.inc()
                }else{
                    count = 1
                }
                SPUtils.getInstance(context!!,"beans").put("count",count)
                SPUtils.getInstance(context!!,"beans").put(playUrl!!,playUrl)
                ObjectSaveUtils.saveObject(context!!,"bean$count",videoBean)
            }
            intent.putExtra("data",videoBean as Parcelable)
            context?.let { context -> context.startActivity(intent) }
        }

    }

    class HomeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var tv_detail: TextView? = null
        var tv_title: TextView? = null
        var tv_time: TextView? = null
        var iv_photo: ImageView? = null
        var iv_user: ImageView? = null

        init {
            tv_detail = itemView?.find(R.id.tv_detail)
            tv_title = itemView?.find(R.id.tv_title)
            iv_photo = itemView?.find(R.id.iv_photo)
            iv_user = itemView?.find(R.id.iv_user)
//        tv_detail = itemView?.findViewById(R.id.tv_detail) as TextView?
//        tv_title = itemView?.findViewById(R.id.tv_title) as TextView?
//        iv_photo = itemView?.findViewById(R.id.iv_photo) as ImageView?
//        iv_user = itemView?.findViewById(R.id.iv_user) as ImageView?
//        tv_title?.typeface = Typeface.createFromAsset(context?.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        }
    }
}