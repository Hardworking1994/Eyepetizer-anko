package com.example.java.eyepetizer.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Administrator on 2018/1/23.
 */

class HotAdapter(fragmentManager: FragmentManager?, mFragments: ArrayList<Fragment>, mTabs: List<String>) : FragmentPagerAdapter(fragmentManager) {
    var mFm:FragmentManager?=null
    var mList:ArrayList<Fragment>?=null
    var mTitles:List<String>?=null
    init {
        mFm= fragmentManager
        mList=mFragments
        mTitles=mTabs
    }

    override fun getPageTitle(position: Int): String? {
        return mTitles?.get(position)
    }
    override fun getItem(position: Int): Fragment? {
        return mList?.get(position)
    }

    override fun getCount(): Int {
        return mList?.size?:0
    }

}
