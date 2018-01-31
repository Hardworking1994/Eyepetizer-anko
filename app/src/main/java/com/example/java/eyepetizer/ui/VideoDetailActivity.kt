package com.example.java.eyepetizer.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.mvp.model.bean.VideoBean
import com.example.java.eyepetizer.utils.*
import com.shuyu.gsyvideoplayer.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.*
import zlc.season.rxdownload2.RxDownload

/**
 * Created by Li on 2018/1/16.
 */
class VideoDetailActivity : AppCompatActivity() {
    companion object {
        var MSG_IMAGE_LOADED = 101
    }

    var mContext: Context = this
    lateinit var bean: VideoBean
    var imageView: ImageView? = null
    val diposable:Disposable?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bean = intent.getParcelableExtra("data")
        VideoDetailUI().setContentView(this)
        Glide.with(this).load(bean.blurred).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_empty_picture)
                .error(R.drawable.ic_empty_picture)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        find<LinearLayout>(R.id.vl_bg).background = BitmapDrawable(resource)
                    }
                })
        ImageLoadUtils.displayHigh(this, imageView, bean.feed!!)

    }

    class VideoDetailUI : AnkoComponent<VideoDetailActivity> {
        override fun createView(ui: AnkoContext<VideoDetailActivity>): View = with(ui) {
            verticalLayout {
                video {
                    id = R.id.gsy_player
                    titleTextView.visibility = View.GONE
                    backButton.visibility = View.GONE
                    ui.owner.imageView = ImageView(ui.ctx)
                    setThumbImageView(ui.owner.imageView)
                    setIsTouchWiget(true)
                    isRotateViewAuto = false
                    isLockLand = false
                    isShowFullAnimation = false
                    isNeedLockFull = true
                    setThumbPlay(true)
                    var uri = ui.owner.intent.getStringExtra("localFile")
                    val orientationUtils = OrientationUtils(ui.owner, this)
                    if (uri != null) {
                        Log.e("uri", uri)
                        setUp(uri, false, null, null)
                    } else {
                        setUp(ui.owner.bean.playUrl, false, null, null)
                    }
                    fullscreenButton.setOnClickListener {
                        orientationUtils.resolveByClick()
                        startWindowFullscreen(ui.ctx, true, true)
                    }

                    setStandardVideoAllCallBack(object : StandardVideoAllCallBack {
                        override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
                        }

                        override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickResume(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickStartIcon(url: String?, vararg objects: Any?) {
                        }

                        override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
                        }

                        override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickBlank(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickStop(url: String?, vararg objects: Any?) {
                        }

                        override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickSeekbar(url: String?, vararg objects: Any?) {
                        }

                        override fun onPlayError(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickStartThumb(url: String?, vararg objects: Any?) {
                        }

                        override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
                        }

                        override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
                        }

                        override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
                        }

                        override fun onPrepared(url: String?, vararg objects: Any?) {
                            orientationUtils.isEnable = true
                        }

                        override fun onAutoComplete(url: String?, vararg objects: Any?) {
                        }

                        override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                            orientationUtils.backToProtVideo()
                        }

                        override fun onClickStartError(url: String?, vararg objects: Any?) {
                        }


                    })
                    setLockClickListener { view, lock -> orientationUtils.isEnable = !lock }

                }.lparams(width = matchParent, height = dip(0)) {
                    weight = 2f
                }
                verticalLayout {
                    id = R.id.vl_bg
                    padding = dip(20)
                    verticalLayout {
                        textView {
                            text = ui.owner.bean.title
                            textColorResource = R.color.white
                            textSize = 18f
                            setTypeface(Typeface.createFromAsset(ui.ctx.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF"), 1)
                        }.lparams(width = wrapContent, height = wrapContent)
                        textView {
                            text = ui.owner.bean.category + "/" + RealDurationUtils.druation2RealTime(ui.owner.bean.duration!!)
                            textColorResource = R.color.white
                            textSize = 12f
                            setTypeface(Typeface.createFromAsset(ui.ctx.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF"), 1)
                        }.lparams(width = wrapContent, height = wrapContent) {
                            topMargin = dip(5)
                        }
                        textView {
                            text = ui.owner.bean.description
                            textColorResource = R.color.white
                            textSize = 14f
                            typeface = Typeface.createFromAsset(ui.ctx.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
                        }.lparams(width = wrapContent, height = wrapContent) {
                            topMargin = dip(5)
                        }
                    }.lparams {
                        weight = 1f
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        imageView(R.drawable.ic_action_favorites_without_padding)
                        textView {
                            text = ui.owner.bean.collect.toString()
                            textColorResource = R.color.white
                        }.lparams {
                            leftMargin = dip(5)
                        }
                        imageView(R.drawable.ic_action_share_without_padding).lparams {
                            leftMargin = dip(10)
                        }
                        textView {
                            text = ui.owner.bean.share.toString()
                            textColorResource = R.color.white
                        }.lparams {
                            leftMargin = dip(5)
                        }
                        imageView(R.drawable.ic_action_reply_nopadding).lparams {
                            leftMargin = dip(10)
                        }
                        textView {
                            text = ui.owner.bean.share.toString()
                            textColorResource = R.color.white
                        }.lparams {
                            leftMargin = dip(5)
                        }
                        imageView(R.drawable.action_download_cut).lparams {
                            leftMargin = dip(10)
                        }
                        textView {
                            text = "缓存"
                            textColorResource = R.color.white
                            setOnClickListener {
                                var url = ui.owner.bean.playUrl?.let { it ->
                                    SPUtils.getInstance(ui.ctx, "downloads").getString(it)
                                }
                                if (url.equals("")) {
                                    var count = SPUtils.getInstance(ui.ctx, "downloads").getInt("count")
                                    count = if (count != -1) {
                                        count.inc()
                                    } else {
                                        1
                                    }
                                    SPUtils.getInstance(ui.ctx, "downloads").put("count", count)
                                    ObjectSaveUtils.saveObject(ui.ctx, "download$count", ui.owner.bean)
                                    ui.owner.addMission(ui.owner.bean.playUrl, count)
                                }else{
                                    ui.owner.showToast("该视频已经缓存")
                                }
                            }
                        }.lparams {
                            leftMargin = dip(5)
                        }
                    }.lparams(width = matchParent, height = wrapContent)

                }.lparams(width = matchParent, height = dip(0)) {

                    weight = 3f
                }

            }
        }



    }
    fun addMission(playUrl: String?, count: Int) {
        RxDownload.getInstance(this).serviceDownload(playUrl,"download$count").subscribe({
            showToast("开始下载")
            SPUtils.getInstance(this, "downloads").put(bean.playUrl.toString(),bean.playUrl.toString())
            SPUtils.getInstance(this, "download_state").put(playUrl.toString(), true)
        },{
            showToast("添加任务失败")
        })

    }

    override fun onBackPressed() {
        if (find<StandardGSYVideoPlayer>(R.id.gsy_player).isIfCurrentIsFullscreen) {
            StandardGSYVideoPlayer.backFromWindowFull(this)
            return
        }
        super.onBackPressed()
    }


    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        OrientationUtils(this, find<StandardGSYVideoPlayer>(R.id.gsy_player))?.releaseListener()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (find<StandardGSYVideoPlayer>(R.id.gsy_player).currentState == StandardGSYVideoPlayer.CURRENT_STATE_PLAYING) {
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!find<StandardGSYVideoPlayer>(R.id.gsy_player).isIfCurrentIsFullscreen) {
                    find<StandardGSYVideoPlayer>(R.id.gsy_player).startWindowFullscreen(this, true, true)
                }
            } else {
                if (find<StandardGSYVideoPlayer>(R.id.gsy_player).isIfCurrentIsFullscreen) {
                    StandardGSYVideoPlayer.backFromWindowFull(this)
                }
                OrientationUtils(this, find<StandardGSYVideoPlayer>(R.id.gsy_player)).isEnable = true
            }

        }
    }

}