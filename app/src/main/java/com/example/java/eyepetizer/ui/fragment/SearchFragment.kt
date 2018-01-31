package com.example.java.eyepetizer.ui.fragment

import android.app.DialogFragment
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.adapter.SearchAdapter
import com.example.java.eyepetizer.search.CircularRevealAnim
import com.example.java.eyepetizer.ui.ResultActivity
import com.example.java.eyepetizer.utils.KeyBoardUtils
import com.example.java.eyepetizer.utils.showToast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by Li on 2017/12/7.
 */
const val SEARCH_TAG = "SearchFragment"

class SearchFragment : DialogFragment(), CircularRevealAnim.AnimListener, ViewTreeObserver.OnPreDrawListener {


    var data: MutableList<String> = arrayListOf("脱口秀", "666", "魔性", "公益", "VR", "教程", "奥斯卡", "舞蹈", "清新", "电影相关", "魔性")

    lateinit var mRootView: View
    lateinit var mCircularRevealAnim: CircularRevealAnim
    lateinit var mAdapter: SearchAdapter

    private var et_search: EditText? = null
    private var iv_search: ImageView? = null
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setData()
    }

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    private fun initDialog() {
        val window = dialog.window
        val metrics = resources.displayMetrics
        val width = (metrics.widthPixels * 0.98).toInt()
        window.setLayout(width,WindowManager.LayoutParams.MATCH_PARENT)
        window.setGravity(Gravity.TOP)
        window.setWindowAnimations(R.style.DialogEmptyAnimation)
    }


    private fun setData() {
        mAdapter = SearchAdapter(activity, data as ArrayList<String>,object :SearchAdapter.OnDialogDismiss{
            override fun onDismiss() {
                hideAnim()
            }

        })
//        mAdapter.setOnDialogDismissListener(object : SearchAdapter.OnDialogDismiss {
//            override fun onDismiss() {
//                hideAnim()
//            }
//        })
        val manager = FlexboxLayoutManager()
        manager.flexDirection = FlexDirection.ROW
        manager.flexWrap = FlexWrap.WRAP
        recyclerView?.layoutManager = manager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = mAdapter

    }

    private fun init() {
        mCircularRevealAnim = CircularRevealAnim()
        mCircularRevealAnim.setAnimLister(this)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
                hideAnim()
            } else {
                search()
            }
            false
        }
        iv_search?.viewTreeObserver?.addOnPreDrawListener(this)
        setData()
    }

    private fun search() {
        val searchKey = et_search?.text.toString()
        if (TextUtils.isEmpty(searchKey.trim({ it <= ' ' }))) {
            activity.showToast("请输入关键字")
        } else {
            hideAnim()
            activity.startActivity<ResultActivity>("keyWord" to et_search?.text.toString().trim())
        }
    }

    private fun hideAnim() {
        KeyBoardUtils.closeKyeBoard(activity, et_search!!)
        mCircularRevealAnim?.hide(iv_search!!, mRootView)
    }

    override fun onHideAnimationEnd() {
        et_search?.setText("")
        dismiss()
    }

    override fun onShowAnimationEnd() {
        if (isVisible) {
            KeyBoardUtils.openKeyBoard(activity, et_search!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mRootView = UI {
            verticalLayout {
                lparams(width = matchParent, height = matchParent)
                backgroundResource = R.drawable.shape_corner_white
                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    padding = dip(6)
                    imageView {
                        padding = dip(6)
                        imageResource = R.drawable.ic_back_24dp
                        onClick {
                            hideAnim()
                        }
                    }
                    et_search = editText {
                        background = null
                        hint = "帮你找到感兴趣的视频"
                        imeOptions = EditorInfo.IME_ACTION_SEARCH
                        padding = dip(6)
                        singleLine = true
                        hintTextColor = R.color.gray
                        textSize = 14f

                    }.lparams(width = dip(0), height = wrapContent) {
                        weight = 1f
                    }
                   iv_search= imageView {
                        padding = dip(6)
                        imageResource = R.drawable.ic_search_bg_24dp
                        onClick {
                            search()
                        }

                    }
                }.lparams(width = matchParent, height = wrapContent)

                view {
                    backgroundColorResource = R.color.divider
                }.lparams(width = wrapContent, height = dip(0.5f))
                textView {
                    backgroundResource = R.drawable.shape_corner_white
                    gravity = Gravity.CENTER
                    padding = dip(10)
                    text = "输入标题或描述中的关键词找到更多视频"
                    textColorResource = R.color.gray
                    textSize = 12f
                }.lparams(width = matchParent, height = wrapContent)

                textView {
                    gravity = Gravity.CENTER
                    padding = dip(20)
                    textColorResource = R.color.black
                    text = "-热门搜索词-"
                    typeface = Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
                }.lparams(width = matchParent, height = wrapContent)

                recyclerView = recyclerView {

                }.lparams(width = matchParent, height = matchParent)

            }
        }.view
        return mRootView
    }

    override fun onPreDraw(): Boolean {
        iv_search?.viewTreeObserver?.removeOnPreDrawListener(this)
        mCircularRevealAnim.show(iv_search!!, mRootView)
        return true
    }

}