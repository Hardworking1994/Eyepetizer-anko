package com.example.java.eyepetizer.adapter

import android.content.Context
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.ui.VideoDetailActivity
import com.example.java.eyepetizer.utils.ImageLoadUtils
import com.example.java.eyepetizer.utils.ObjectSaveUtils
import com.example.java.eyepetizer.utils.RealDurationUtils
import com.example.java.eyepetizer.utils.SPUtils
import org.jetbrains.anko.*
import java.text.SimpleDateFormat

/**
 * Created by Administrator on 2018/1/30.
 */
class RecordAdapter(context: Context, list: ArrayList<VideoBean>) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    var context: Context? = null
    var list: ArrayList<VideoBean>? = null

    init {
        this.context = context
        this.list = list
    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecordViewHolder?, position: Int) {
        val feed = list?.get(position)?.feed
        feed?.let { ImageLoadUtils.display(context!!,holder?.photo,it) }
        val title = list?.get(position)?.title
        holder?.title?.text=title
        val category = list?.get(position)?.category
        val duration = list?.get(position)?.duration
        val time = list?.get(position)?.time
        val simpleDateFormat = SimpleDateFormat("MM-dd")
        val data=simpleDateFormat.format(time)
        holder?.duration?.text="$category/${RealDurationUtils.druation2RealTime(duration!!)}/$data"
        holder?.itemView?.setOnClickListener {
            val description = list?.get(position)?.description
            val playUrl = list?.get(position)?.playUrl
            val blurred = list?.get(position)?.blurred
            val collect = list?.get(position)?.collect
            val share = list?.get(position)?.share
            val reply = list?.get(position)?.reply
            val currentTimeMillis = System.currentTimeMillis()
            val videoBean=VideoBean(feed,title,description,duration,playUrl,category,blurred,collect,share,reply,currentTimeMillis)
            val url = SPUtils.getInstance(context!!, "beans").getString(playUrl!!)
            if(url == ""){
                var count = SPUtils.getInstance(context!!, "beans").getInt("count")
                count = if(count!=-1){
                    count.inc()
                }else{
                    1
                }
                SPUtils.getInstance(context!!,"beans").put("count",count)
                SPUtils.getInstance(context!!,"beans").put(playUrl!!,playUrl)
                ObjectSaveUtils.saveObject(context!!,"bean$count",videoBean)
            }
            context?.startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecordViewHolder {
        return RecordViewHolder(createView(context!!))
    }

    private fun createView(context: Context): View {

        return with(context) {
            relativeLayout {
                lparams(width= matchParent,height = dip(100))
                imageView {
                    id= R.id.iv_record_photo
                    padding=dip(5)
                }.lparams(width=dip(180),height = matchParent){
                    centerVertically()
                    alignParentLeft()
                }
                verticalLayout {
                    gravity=Gravity.CENTER_VERTICAL
                    textView {
                        id=R.id.tv_record_title
                        maxLines=2
                        padding=dip(5)
                        textSize=14f
                        textColorResource=R.color.black
                    }.lparams(width= matchParent,height = wrapContent)
                    textView {
                        id=R.id.tv_record_duration
                        textSize=12f
                        padding=dip(5)
                    }.lparams(width= matchParent,height = wrapContent)
                }.lparams(width= matchParent,height = matchParent){
                    centerVertically()
                    rightOf(R.id.iv_record_photo)
                }

            }
        }
    }


    class RecordViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var photo:ImageView?=null
        var title:TextView?=null
        var duration:TextView?=null
        init {
            photo=itemView?.find(R.id.iv_record_photo)
            title=itemView?.find(R.id.tv_record_title)
            duration=itemView?.find(R.id.tv_record_duration)
        }
    }
}