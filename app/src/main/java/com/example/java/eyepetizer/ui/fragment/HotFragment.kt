package com.example.java.eyepetizer.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.adapter.HotAdapter
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

/**
 * Created by Li on 2017/12/7.
 */
class HotFragment: BaseFragment() {

    val mTabs= listOf("周排行","月排行","总排行")
    lateinit var mFragments:ArrayList<Fragment>
    val STRATEGY= arrayOf("weekly","monthly","historical")


    init {


    }
    override fun initView() {
        val weekFragment=RankFragment()
        val weekBundle=Bundle()
        weekBundle.putString("strategy",STRATEGY[0])
        weekFragment.arguments=weekBundle
        val monthFragment=RankFragment()
        val monthBundle=Bundle()
        monthBundle.putString("strategy",STRATEGY[1])
        monthFragment.arguments=monthBundle
        val allFragment=RankFragment()
        val allBundle=Bundle()
        allBundle.putString("strategy",STRATEGY[2])
        allFragment.arguments=allBundle
        mFragments= ArrayList()
        mFragments.add(weekFragment as Fragment)
        mFragments.add(monthFragment as Fragment)
        mFragments.add(allFragment as Fragment)
        viewPage?.adapter=HotAdapter(fragmentManager,mFragments,mTabs)
        tabs?.setupWithViewPager(viewPage)
    }
    var viewPage:ViewPager?= null
    var tabs:TabLayout?=null
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                lparams(width= matchParent,height = matchParent)
                 tabs = tabLayout {
                    setSelectedTabIndicatorColor(R.color.black)
                }.lparams(width = matchParent, height = wrapContent)
                viewPage= viewPager {
                    id=R.id.vp_hot
                }.lparams(width= matchParent,height = matchParent)

            }
        }.view
    }

}
