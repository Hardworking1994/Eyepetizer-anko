package com.example.java.eyepetizer.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.ui.DownloadActivity
import com.example.java.eyepetizer.ui.RecordActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.UI

/**
 * Created by Li on 2017/12/7.
 */
class MineFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                lparams(width = matchParent, height = matchParent)
                frameLayout {
                    alpha = 0.5f
                    linearLayout {
                        gravity = Gravity.CENTER
                        imageView {
                            imageResource = R.drawable.landing_countdown_background
                        }.lparams(width = dip(100), height = dip(100)) {
                            gravity = Gravity.CENTER
                        }
                    }.lparams(width = matchParent, height = matchParent)
                    linearLayout {
                        gravity = Gravity.CENTER
                        imageView {
                            imageResource = R.drawable.ic_launcher
                        }.lparams(width = dip(70), height = dip(70))
                    }.lparams(width = matchParent, height = matchParent)
                }.lparams(width = matchParent, height = dip(120))
                textView {
                    text = "点击登陆即可发表评论"
                    gravity = Gravity.CENTER
                }.lparams(width = matchParent, height = wrapContent) {
                    topMargin = dip(20)
                }
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER
                        imageView {
                            imageResource = R.drawable.ic_grey_heart
                        }.lparams(width = wrapContent, height = wrapContent)
                        textView {
                            text = "收藏"
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = matchParent)
                    }.lparams(width = dip(0), height = matchParent) {
                        weight = 1f
                    }
                    view {
                        backgroundColorResource = R.color.gray
                    }.lparams(width = dip(0.5f), height = dip(30)) {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER
                        imageView {
                            imageResource = R.drawable.ic_action_reply_grey_without_padding
                        }.lparams(width = wrapContent, height = wrapContent)
                        textView {
                            text = "评论"
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = matchParent)
                    }.lparams(width = dip(0), height = matchParent) {
                        weight = 1f
                    }
                }.lparams(width = matchParent, height = dip(40)) {
                    topMargin = dip(10)
                }
                view {
                    backgroundColorResource = R.color.gray
                }.lparams(width = matchParent, height = dip(0.5f))
                textView {
                    text = "我的缓存"
                    textSize = 14f
                    gravity = Gravity.CENTER_HORIZONTAL
                    typeface = Typeface.createFromAsset(activity.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
                    onClick {
                        activity.startActivity<DownloadActivity>()
                    }

                }.lparams(width = matchParent, height = dip(50)) {
                    topMargin = dip(50)
                }
                textView {
                    text = "观看记录"
                    textSize = 14f
                    gravity = Gravity.CENTER_HORIZONTAL
                    typeface = Typeface.createFromAsset(activity.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
                    onClick {
                        activity.startActivity<RecordActivity>()
                    }
                }.lparams(width = matchParent, height = dip(50)) {
                    topMargin = dip(50)
                }
                textView {
                    text = "意见反馈"
                    textSize = 14f
                    gravity = Gravity.CENTER_HORIZONTAL
                    typeface = Typeface.createFromAsset(activity.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")

                }.lparams(width = matchParent, height = dip(50)) {
                    topMargin = dip(50)
                }

            }
        }.view
    }

    override fun initView() {
    }

}