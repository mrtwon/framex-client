package com.mrtwon.framex.Helper

import android.icu.math.BigDecimal

class HelperFunction {
    /*
    Helper function for work with data
     */
    companion object{

        //round rating and return String result
        fun roundRating(rating: Double?): String{
            if (rating == null) return "0.0"
            return BigDecimal(rating.toString()).setScale(1, BigDecimal.ROUND_DOWN).toString()
        }

        //round rating and return Double result
        fun helperConvertDouble(rating: Double?): Double{
            if (rating == null) return 0.0
            return BigDecimal(rating.toString()).setScale(1, BigDecimal.ROUND_DOWN).toDouble()
        }
    }

}