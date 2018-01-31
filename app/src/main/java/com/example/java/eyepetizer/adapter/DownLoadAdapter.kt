package com.example.java.eyepetizer.adapter

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.ui.VideoDetailActivity
import com.example.java.eyepetizer.utils.ImageLoadUtils
import com.example.java.eyepetizer.utils.SPUtils
import com.example.java.eyepetizer.utils.showToast
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.*
import zlc.season.rxdownload2.RxDownload
import zlc.season.rxdownload2.entity.DownloadFlag

/**
 * Created by Administrator on 2018/1/26.
 */
class DownLoadAdapter(context: Context, list: ArrayList<VideoBean>) : RecyclerView.Adapter<DownLoadAdapter.DownloadViewHolder>() {

    lateinit var mOnLongListener:OnLongClickListener
    lateinit var disposable: Disposable
    var mContext: Context? = null
    var mList: ArrayList<VideoBean>? = null
    var isDownload = false
    var hasLoaded = false

    init {
        mContext = context
        mList = list
    }

    interface OnLongClickListener{
        fun onLongClick(position: Int)
    }

    fun setOnLongClickListener(onLongClickListener: OnLongClickListener){
        mOnLongListener=onLongClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DownloadViewHolder {
        return DownloadViewHolder(createView(mContext!!))
    }

    private fun createView(context: Context): View {
        return with(context) {
            relativeLayout {
                lparams(width = matchParent, height = dip(100))
                imageView {
                    id = R.id.iv_download_photo
                    padding = dip(5)
                }.lparams(width = dip(180), height = matchParent) {
                    centerVertically()
                    alignParentLeft()
                }
                relativeLayout {
                    verticalLayout {
                        gravity = Gravity.CENTER
                        textView {
                            id = R.id.tv_download_title
                            textSize = 14f
                            textColorResource = R.color.black
                            maxLines = 2
                            padding = dip(5)
                            typeface = Typeface.createFromAsset(mContext?.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
                        }.lparams(width = matchParent, height = wrapContent)
                        textView {
                            padding = dip(5)
                            id = R.id.tv_download_detail
                            textSize = 12f
                        }.lparams(width = matchParent, height = wrapContent)


                    }.lparams(width = matchParent, height = matchParent) {
                        alignParentLeft()
                        leftOf(R.id.iv_download_state)
                    }

                    imageView {
                        id = R.id.iv_download_state
                        setPadding(0, 0, dip(5), 0)
                    }.lparams(width = dip(30), height = matchParent) {
                        alignParentRight()
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    rightOf(R.id.iv_download_photo)
                    centerVertically()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    override fun onBindViewHolder(holder: DownloadViewHolder?, position: Int) {
        val feed = mList?.get(position)?.feed
        feed?.let { ImageLoadUtils.display(mContext!!, holder?.photo, it) }
        val title = mList?.get(position)?.title
        holder?.title?.text = title
        val category = mList?.get(position)?.category
        val duration = mList?.get(position)?.duration
        isDownload = SPUtils.getInstance(mContext!!, "download_state").getBoolean(mList?.get(position)?.playUrl!!)
        getDownLoadState(mList?.get(position)?.playUrl!!, holder)
        if (isDownload) {
            holder?.state?.setImageResource(R.drawable.icon_download_stop)
        }else{
            holder?.state?.setImageResource(R.drawable.icon_download_start)
        }
        holder?.state?.setOnClickListener {
            if(isDownload){
                isDownload=false
                SPUtils.getInstance(mContext!!,"download_state").put(mList?.get(position)?.playUrl!!,false)
                holder?.state?.setImageResource(R.drawable.icon_download_start)
                RxDownload.getInstance(mContext).pauseServiceDownload(mList?.get(position)?.playUrl).subscribe()
            }else{
                isDownload=true
                SPUtils.getInstance(mContext!!,"download_state").put(mList?.get(position)?.playUrl!!,true)
                holder?.state?.setImageResource(R.drawable.icon_download_stop)
                addMission(mList?.get(position)?.playUrl!!,position+1)

            }
        }
        holder?.itemView?.setOnClickListener {
            val description = mList?.get(position)?.description
            val playUrl = mList?.get(position)?.playUrl
            val blurred = mList?.get(position)?.blurred
            val collect = mList?.get(position)?.collect
            val share = mList?.get(position)?.share
            val reply = mList?.get(position)?.reply
            val currentTimeMillis = System.currentTimeMillis()
            val videoBean=VideoBean(feed,title,description,duration,playUrl,category,blurred,collect,share,reply,currentTimeMillis)
            if(hasLoaded){
                val files = RxDownload.getInstance(mContext).getRealFiles(playUrl)
                val uri = Uri.fromFile(files!![0])
                mContext?.startActivity<VideoDetailActivity>("localFile" to uri.toString(),"data" to  videoBean as Parcelable)
            }else{
                mContext?.startActivity<VideoDetailActivity>("data" to videoBean as Parcelable)
            }
        }
        holder?.itemView?.setOnLongClickListener {
            mOnLongListener.onLongClick(position)
            true
        }
    }

    private fun addMission(playUrl: String, i: Int) {
        RxDownload.getInstance(mContext).serviceDownload(playUrl,"download$i").subscribe({
//            isDownload=true
            mContext?.showToast("开始下载")
        },{
            mContext?.showToast("添加任务失败")
        })
    }

    private fun getDownLoadState(playUrl: String, holder: DownloadViewHolder?) {
       disposable= RxDownload.getInstance(mContext).receiveDownloadStatus(playUrl).subscribe { event ->
            if (event.flag == DownloadFlag.FAILED) {
                Log.w("Error", event.error)
            }
            val downloadStatus = event.downloadStatus
            val percentNumber = downloadStatus.percentNumber
            if (percentNumber == 100L) {
                if (!disposable.isDisposed && disposable != null) {
                    disposable.dispose()
                }
                hasLoaded = true
                holder?.state?.visibility = View.GONE
                holder?.detail?.text = "已缓存"
                isDownload = false
                SPUtils.getInstance(mContext!!, "down_state").put(playUrl, false)
            } else {
                if (holder?.state?.visibility != View.VISIBLE) {
                    holder?.state?.visibility = View.VISIBLE
                }
                if (isDownload) {
                    holder?.detail?.text = "缓存中/$percentNumber%"
                } else {
                    holder?.detail?.text = "已暂停/$percentNumber%"
                }
            }
        }
    }
    class DownloadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView? = null
        var title: TextView? = null
        var detail: TextView? = null
        var state: ImageView? = null

        init {
            photo = itemView.find(R.id.iv_download_photo)
            title = itemView.find(R.id.tv_download_title)
            detail = itemView.find(R.id.tv_download_detail)
            state = itemView.find(R.id.iv_download_state)
        }
    }
}