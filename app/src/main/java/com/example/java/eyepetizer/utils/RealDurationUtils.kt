package com.example.java.eyepetizer.utils

/**
 * Created by Li on 2018/1/17.
 */

class RealDurationUtils{
    companion object {
        fun druation2RealTime(duration:Long): String {
            var minute=duration?.div(60)
            var second=duration?.minus(minute?.times(60))
            var realMinute: String
            var realSecond: String
            realMinute = if (minute!! < 10) {
                "0" + minute
            } else {
                minute.toString()
            }
            realSecond = if (second!! < 10) {
                "0" + second
            } else {
                second.toString()
            }
            return "$realMinute'$realSecond''"
        }

    }
}