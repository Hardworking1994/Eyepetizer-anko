package com.example.java.eyepetizer.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.java.eyepetizer.adapter.RankAdapter
import com.example.java.eyepetizer.mvp.contract.HotContract
import com.example.java.eyepetizer.mvp.model.bean.HotBean
import com.example.java.eyepetizer.mvp.presenter.HotPresenter
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.verticalLayout

/**
 * Created by Administrator on 2018/1/23.
 */
class RankFragment:BaseFragment(),HotContract.View {

    lateinit var mStrategy:String
    lateinit var mAdapter:RankAdapter
    lateinit var mPresenter:HotPresenter
    var mList:ArrayList<HotBean.ItemListBean.DataBean> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mAdapter= RankAdapter(context,mList)
        if(arguments!=null){
            mStrategy=arguments.getString("strategy")
            mPresenter= HotPresenter(context,this)
            mPresenter.requestData(mStrategy)
        }
        return UI {
            verticalLayout {
                lparams(width= matchParent,height = matchParent)
                recyclerView {
                    layoutManager=LinearLayoutManager(context)
                    adapter=mAdapter
                }.lparams(width= matchParent,height = matchParent)
            }
        }.view
    }

    override fun setData(bean: HotBean) {
        if(mList.size>0){
            mList.clear()
        }
        bean.itemList?.forEach {
            it?.data?.let { it1->mList.add(it1) }
        }
        mAdapter.notifyDataSetChanged()

    }

    override fun initView() {
    }

}