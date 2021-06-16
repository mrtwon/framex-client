package com.mrtwon.framex.FragmentUpdate

import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.startandroid.MyApplication
import com.mrtwon.framex.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class FragmentUpdate: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.test_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateMovie()
        super.onViewCreated(view, savedInstanceState)
    }
    fun updateSerial(){
        GlobalScope.launch {
            delay(5000)
            val db = MyApplication.getInstance.DB.dao()
            val serialList = db.getAllSerial()
            val size = serialList.size
            for((counter, element) in serialList.withIndex()){
                element.description?.let {
                    val newValue = it.toLowerCase(Locale.ROOT)
                    element.description_lower = newValue
                }
                element.ru_title?.let {
                    val newValue = it.toLowerCase(Locale.ROOT)
                    element.ru_title_lower = newValue
                    Log.i("self-update","[$counter/$size] old '$it' new '${newValue}'")
                }
                db.updateSerial(element)
            }
        }
    }
    fun updateMovie(){
        GlobalScope.launch {
            val db = MyApplication.getInstance.DB.dao()
            val movieList = db.getAllMovie()
            val size = movieList.size
            for((counter, element) in movieList.withIndex()){
                element.description?.let {
                    val newValue = it.toLowerCase(Locale.ROOT)
                    element.description_lower = newValue
                }
                element.ru_title?.let {
                    val newValue = it.toLowerCase(Locale.ROOT)
                    element.ru_title_lower = newValue
                    Log.i("self-update","[$counter/$size] old '$it' new '${newValue}'")
                }
                db.updateMovie(element)
            }
        }
    }
}