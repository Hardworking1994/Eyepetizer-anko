package com.example.java.eyepetizer.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by Administrator on 2018/1/25.
 */
class KeyBoardUtils {
    companion object {

        fun openKeyBoard(context: Context,editText: EditText){
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText,InputMethodManager.RESULT_SHOWN)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        fun closeKyeBoard(context: Context,editText: EditText){
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken,0)
        }
    }
}