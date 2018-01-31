package com.example.java.eyepetizer.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.java.eyepetizer.adapter.FindAdapter
import com.example.java.eyepetizer.mvp.contract.FindContract
import com.example.java.eyepetizer.mvp.model.bean.FindBean
import com.example.java.eyepetizer.mvp.presenter.FindPresenter
import com.example.java.eyepetizer.ui.FindDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by Li on 2017/12/7.
 */
class FindFragment: BaseFragment() ,FindContract.View{
    var mPresenter:FindPresenter?=null
    var mAdapter:FindAdapter?=null
    var mList:MutableList<FindBean>?=null
    var gridView:GridView?=null
    override fun setData(beans: MutableList<FindBean>) {

//        mAdapter?.mList=beans
        mList=beans
        mAdapter= FindAdapter(context, mList!!)
        gridView?.adapter=mAdapter
        mAdapter?.notifyDataSetChanged()
    }


    override fun initView() {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter= FindPresenter(context,this)
        mPresenter?.start()
        return UI {
            verticalLayout{
                lparams(width= matchParent,height = matchParent)
               gridView= gridView {
                    gravity=Gravity.CENTER
                    columnWidth=dip(180)
                    verticalSpacing=dip(3)
                    horizontalSpacing=dip(0)
                    numColumns=2
//                    adapter=mAdapter
                   setOnItemClickListener { parent, view, position, id ->
                       var bean=mList?.get(position)
                       var name=bean?.name
                       startActivity<FindDetailActivity>("name" to name)
                   }
                }.lparams(width= matchParent,height = matchParent)
            }
        }.view
    }
}