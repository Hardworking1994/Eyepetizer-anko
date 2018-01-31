package com.example.java.eyepetizer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.ui.ResultActivity
import com.google.android.flexbox.FlexboxLayoutManager
import org.jetbrains.anko.*

/**
 * Created by Administrator on 2018/1/25.
 */
class SearchAdapter(context: Context,list:ArrayList<String>,onDialogDismiss: OnDialogDismiss): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var context:Context?=null
    var list:ArrayList<String>?=null
    var onDialogDismiss: OnDialogDismiss?=null
    init {
        this.context=context
        this.list=list
        this.onDialogDismiss=onDialogDismiss
    }




    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchViewHolder {
        return SearchViewHolder(createView(context!!))
    }
    private fun createView(context: Context):View{
        return with(context){
            linearLayout {
                textView {
                    id=R.id.tv_search_title
                    text="我的"
                    padding=dip(5)
                    textColorResource= R.color.white
                    backgroundResource=R.drawable.sharpe_corner_gray
                    gravity=Gravity.CENTER
                }.lparams(width= wrapContent,height = wrapContent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }

    override fun onBindViewHolder(holder: SearchViewHolder?, position: Int) {
        holder?.title?.text=list!![position]
        val params = holder?.title?.layoutParams
        if(params is FlexboxLayoutManager.LayoutParams){
            (holder?.title?.layoutParams as FlexboxLayoutManager.LayoutParams).flexGrow =1.0f
        }
        holder?.itemView?.setOnClickListener {
            val keyWord= list!![position]
            context?.startActivity<ResultActivity>("keyWord" to keyWord)
            onDialogDismiss?.onDismiss()
        }

    }


    class SearchViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView){
        var title:TextView?=null
        init {
            title=itemView?.find(R.id.tv_search_title)
        }
    }

    interface OnDialogDismiss {
        fun onDismiss()
    }
//    fun setOnDialogDismissListener(onDialogDismiss: OnDialogDismiss){
//        dialogDismiss=onDialogDismiss
//    }
}