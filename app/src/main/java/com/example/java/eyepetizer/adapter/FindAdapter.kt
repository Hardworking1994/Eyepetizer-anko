package com.example.java.eyepetizer.adapter

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.mvp.model.bean.FindBean
import com.example.java.eyepetizer.utils.ImageLoadUtils
import org.jetbrains.anko.*

/**
 * Created by Li on 2018/1/18.
 */
class FindAdapter(context: Context,list:MutableList<FindBean>):BaseAdapter(){


    var mContext:Context?=null
    var mList:MutableList<FindBean>?=null
    init {
        mContext=context
        mList=list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View
        var holder:FindViewHolder
        if (convertView==null){
            view= with(mContext){
                this!!.verticalLayout{
                    lparams(width= wrapContent,height = wrapContent)
                    frameLayout{

                        imageView{
                            id= R.id.iv_find_photo
                        }.lparams(width=dip(180),height = dip(180))
                        imageView{
                            id=R.id.iv_find_bg
                            alpha=0.6f
                            backgroundResource=R.drawable.boman_selector
                        }.lparams(width=dip(180),height = dip(180))
                        linearLayout{
                            textView{
                                id=R.id.tv_find_title
                                textColorResource=R.color.white
                                textSize=18f
                                setTypeface(null,1)
                            }
                        }.lparams(width= matchParent,height = matchParent){
                            gravity=Gravity.CENTER
                        }

                    }.lparams(width= wrapContent,height = wrapContent)

                }
            }
            holder= FindViewHolder(view)
            view.tag=holder

        }else{
            view=convertView
            holder=view.tag as FindViewHolder
        }
        ImageLoadUtils.display(mContext!!,holder.photo,mList?.get(position)?.bgPicture!!)
        holder.title?.text=mList?.get(position)!!.name
        return view
    }

    class FindViewHolder (itemView:View){
        var photo:ImageView?=null
        var title:TextView?=null
        init {
            photo=itemView.find(R.id.iv_find_photo)
            title=itemView.find(R.id.tv_find_title)
        }

    }

    override fun getItem(position: Int): FindBean? {
        return mList?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {

        return mList?.size?:0
    }

}