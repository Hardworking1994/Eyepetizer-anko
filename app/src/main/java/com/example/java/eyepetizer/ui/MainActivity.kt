package com.example.java.eyepetizer.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.R.id.*
import com.example.java.eyepetizer.ui.fragment.*
import com.example.java.eyepetizer.utils.showToast
import com.gyf.barlibrary.ImmersionBar
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    var homeFragment: HomeFragment? = null
    var findFragment: FindFragment? = null
    var hotFragemnt: HotFragment? = null
    var mineFragment: MineFragment? = null
    var mExitTime: Long = 0
    var toast: Toast? = null
    private var home: RadioButton? = null
    private var find: RadioButton? = null
    private var hot: RadioButton? = null
    private var mine: RadioButton? = null
    private var titleText: TextView? = null
    private var ivSearch: ImageView? = null


    lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainUi().setContentView(this)
        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init()
        val window = window
        val params = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params
        setRadioButton()
//        initToolbar()
        initFragment(savedInstanceState)
    }


    private fun showSearch() {
        if (find<RadioButton>(rb_mine).isChecked) {
        } else {
            searchFragment = SearchFragment()
            searchFragment.show(fragmentManager, SEARCH_TAG)
        }
    }

    private fun getToday(): String {
        var list = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        var data: Date = Date()
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            //异常情况
            val mFragments: List<Fragment> = supportFragmentManager.fragments
            for (item in mFragments) {
                if (item is HomeFragment) {
                    homeFragment = item
                }
                if (item is FindFragment) {
                    findFragment = item
                }
                if (item is HotFragment) {
                    hotFragemnt = item
                }
                if (item is MineFragment) {
                    mineFragment = item
                }
            }
        } else {
            homeFragment = HomeFragment()
            findFragment = FindFragment()
            mineFragment = MineFragment()
            hotFragemnt = HotFragment()
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.add(R.id.fl_content, homeFragment)
            fragmentTrans.add(R.id.fl_content, findFragment)
            fragmentTrans.add(R.id.fl_content, mineFragment)
            fragmentTrans.add(R.id.fl_content, hotFragemnt)
            fragmentTrans.commit()
        }
        supportFragmentManager.beginTransaction().show(homeFragment)
                .hide(findFragment)
                .hide(mineFragment)
                .hide(hotFragemnt)
                .commit()
    }


    private fun setRadioButton() {
        home = find(R.id.rb_home)
        find = find(R.id.rb_find)
        mine = find(R.id.rb_mine)
        hot = find(R.id.rb_hot)
        titleText = find(tv_bar_title)
        ivSearch = find(iv_search)
        home?.isChecked = true
        home?.setTextColor(resources.getColor(R.color.black))
        home?.setOnClickListener(this)
        find?.setOnClickListener(this)
        hot?.setOnClickListener(this)
        mine?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clearState()
        when (v?.id) {
            R.id.rb_find -> {
                find?.isChecked = true
                find?.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(findFragment)
                        .hide(homeFragment)
                        .hide(mineFragment)
                        .hide(hotFragemnt)
                        .commit()
                titleText?.text = "Discover"
                titleText?.visibility = View.VISIBLE
                ivSearch?.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_home -> {
                home?.isChecked = true
                home?.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(homeFragment)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .hide(hotFragemnt)
                        .commit()
                titleText?.text = getToday()
                titleText?.visibility = View.VISIBLE
                ivSearch?.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_hot -> {
                hot?.isChecked = true
                hot?.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(hotFragemnt)
                        .hide(findFragment)
                        .hide(mineFragment)
                        .hide(homeFragment)
                        .commit()
                titleText?.text = "Ranking"
                titleText?.visibility = View.VISIBLE
                ivSearch?.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_mine -> {
                mine?.isChecked = true
                mine?.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(mineFragment)
                        .hide(findFragment)
                        .hide(homeFragment)
                        .hide(hotFragemnt)
                        .commit()
                titleText?.visibility = View.INVISIBLE
                ivSearch?.setImageResource(R.drawable.icon_setting)
            }
        }

    }


    fun getActionBarHeight(): Int {
        return CommonUtil.getActionBarHeight(this@MainActivity)
    }

    private fun clearState() {
        find<RadioGroup>(rg_root).clearCheck()
        home?.setTextColor(resources.getColor(R.color.gray))
        mine?.setTextColor(resources.getColor(R.color.gray))
        hot?.setTextColor(resources.getColor(R.color.gray))
        find?.setTextColor(resources.getColor(R.color.gray))
    }

    class MainUi : AnkoComponent<MainActivity> {
        override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
            relativeLayout {
                toolbar {
                    backgroundColorResource = R.color.white
                    id = R.id.toolBar
                    relativeLayout {
                        textView {
                            id = R.id.tv_bar_title
                            gravity = Gravity.CENTER
                            textSize = 16f
                            text = ui.owner.getToday()
                            typeface = Typeface.createFromAsset(ui.owner.ctx.assets, "fonts/Lobster-1.4.otf")
                        }.lparams(width = wrapContent, height = matchParent) {
                            centerInParent()
                        }
                        imageView(R.drawable.icon_search) {
                            id = R.id.iv_search
                            onClick {
                                ui.owner.showSearch()
                            }
                        }.lparams(width = wrapContent, height = matchParent) {
                            rightMargin = dip(10)
                            alignParentRight()
                        }

                    }.lparams(width = matchParent, height = matchParent)

                }.lparams(width = wrapContent, height = ui.owner.getActionBarHeight())
                frameLayout {
                    id = R.id.fl_content
                }.lparams(width = matchParent, height = wrapContent) {
                    above(R.id.rg_root)
                    below(R.id.toolBar)
                }
                view {
                    backgroundColorResource = R.color.gray
                }.lparams(width = matchParent, height = dip(0.5f)) {
                    above(R.id.rg_root)
                }
                radioGroup {

                    id = R.id.rg_root
                    backgroundColorResource = R.color.white
                    orientation = LinearLayout.HORIZONTAL
                    padding = dip(5)
                    radioButton {
                        gravity = Gravity.CENTER
                        id = R.id.rb_home
                        textSize = 10f
                        textColorResource = R.color.gray
                        text = "首页"
                        buttonDrawable = null
                        setCompoundDrawablesRelativeWithIntrinsicBounds(null, ui.ctx.getDrawable(R.drawable.home_bg_selected), null, null)

                    }.lparams(height = wrapContent, width = dip(0)) {
                        weight = 1f

                    }
                    radioButton {
                        gravity = Gravity.CENTER
                        id = R.id.rb_find
                        textSize = 10f
                        textColorResource = R.color.gray
                        text = "发现 "
                        buttonDrawable = null
                        setCompoundDrawablesRelativeWithIntrinsicBounds(null, ui.ctx.getDrawable(R.drawable.find_bg_selected), null, null)
                    }.lparams(height = wrapContent, width = dip(0)) {
                        weight = 1f

                    }
                    radioButton {
                        gravity = Gravity.CENTER
                        id = R.id.rb_hot
                        textSize = 10f
                        textColorResource = R.color.gray
                        text = "热门"
                        buttonDrawable = null
                        setCompoundDrawablesRelativeWithIntrinsicBounds(null, ui.ctx.getDrawable(R.drawable.hot_bg_selected), null, null)
                    }.lparams(height = wrapContent, width = dip(0)) {
                        weight = 1f

                    }
                    radioButton {
                        gravity = Gravity.CENTER
                        id = R.id.rb_mine
                        textSize = 10f
                        textColorResource = R.color.gray
                        text = "我的"
                        buttonDrawable = null
                        setCompoundDrawablesRelativeWithIntrinsicBounds(null, ui.ctx.getDrawable(R.drawable.mine_bg_selected), null, null)
                    }.lparams(height = wrapContent, width = dip(0)) {
                        weight = 1f

                    }

                }.lparams(width = matchParent, height = wrapContent) {
                    alignParentBottom()
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        toast?.let { toast!!.cancel() }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 3000) {
                finish()
                toast!!.cancel()
            } else {
                mExitTime = System.currentTimeMillis()
                toast = showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

