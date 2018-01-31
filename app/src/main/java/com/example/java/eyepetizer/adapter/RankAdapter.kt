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

/**
 * Created by Administrator on 2018/1/22.
 */
class RankAdapter(context: Context, list: ArrayList<HotBean.ItemListBean.DataBean>) : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {
    var context: Context? = null
    var list: ArrayList<HotBean.ItemListBean.DataBean>? = null

    init {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RankViewHolder {
        return RankViewHolder(createView(context!!))
    }

    private fun createView(context: Context): View {
        return with(context) {
            verticalLayout {
                lparams(width = matchParent, height = wrapContent)
                frameLayout {
                    imageView {
                        id = R.id.iv_finddetail_photo
                    }.lparams(width = matchParent, height = matchParent)

                    verticalLayout {
                        id = R.id.ll_finddetail_moban
                        backgroundResource = R.drawable.boman_selector
                        relativeLayout {
                            id = R.id.rl_finddetail_text
                            gravity = Gravity.CENTER
                            textView {
                                id = R.id.tv_finddetail_title
                                gravity = Gravity.CENTER
                                text = "测试"
                                textColorResource = R.color.white
                                textSize = 20f
                                typeface= Typeface.createFromAsset(context.assets,"fonts/FZLanTingHeiS-L-GB-Regular.TTF")
                            }.lparams(width = wrapContent, height = wrapContent) {
                                topMargin = dip(28)
                                centerInParent()
                            }
                            textView {
                                id = R.id.tv_finddetail_time
                                gravity = Gravity.CENTER
                                textColorResource = R.color.white
                            }.lparams(width = matchParent, height = wrapContent) {
                                below(R.id.tv_finddetail_title)
                                topMargin = dip(2)
                            }

                        }.lparams(width = matchParent, height = matchParent)


                    }.lparams(width = matchParent, height = matchParent)


                }.lparams(width = matchParent, height = dip(250))

            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: RankViewHolder?, position: Int) {
        var photoUrl:String?=list?.get(position)?.cover?.feed
        photoUrl?.let { ImageLoadUtils.display(context!!,holder?.photo,it) }
        val title:String? = list?.get(position)?.title
        holder?.title?.text=title
        val category = list?.get(position)?.category
        val duration = list?.get(position)?.duration
        holder?.time?.text= "$category/${duration.let { RealDurationUtils.druation2RealTime(duration!!) }}"
        holder?.itemView?.setOnClickListener {
            val description = list?.get(position)?.description
            val playUrl = list?.get(position)?.playUrl
            val blurred = list?.get(position)?.cover?.blurred
            val collectionCount = list?.get(position)?.consumption?.collectionCount
            val shareCount = list?.get(position)?.consumption?.shareCount
            val replyCount = list?.get(position)?.consumption?.replyCount
            val currentTimeMillis = System.currentTimeMillis()
            var videoBean:VideoBean= VideoBean(photoUrl,title,description,duration,playUrl,category,blurred,collectionCount,shareCount,replyCount,currentTimeMillis)
            var url=SPUtils.getInstance(context!!,"beans").getString(playUrl!!)
            if(url == ""){
                var count=SPUtils.getInstance(context!!,"beans").getInt("count")
                count = if(count!=-1){
                    count.inc()
                }else{
                    1
                }
                SPUtils.getInstance(context!!,"beans").put("count",count)
                SPUtils.getInstance(context!!,"beans").put(playUrl!!,playUrl)
                ObjectSaveUtils.saveObject(context!!,"bean$count",videoBean)
            }
            context!!.startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
        }

    }

    class RankViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView? = null
        var title: TextView? = null
        var time: TextView? = null
        init {
            photo = itemView?.find(R.id.iv_finddetail_photo)
            title = itemView?.find(R.id.tv_finddetail_title)
            time = itemView?.find(R.id.tv_finddetail_time)
        }
    }
}