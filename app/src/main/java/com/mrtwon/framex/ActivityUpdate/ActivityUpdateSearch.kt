package com.mrtwon.framex.ActivityUpdate

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex.R
import pl.droidsonroids.gif.GifImageView
import java.io.File

class ActivityUpdateSearch: AppCompatActivity(), View.OnClickListener {
    lateinit var tb: TabLayout
    lateinit var vp: ViewPager
    lateinit var btn_update: Button
    lateinit var search: TextInputEditText
    lateinit var gif_load: GifImageView
    val mainVM: UpdateSearchVM by lazy { ViewModelProvider(this).get(UpdateSearchVM::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_update_one_element)
        tb = findViewById(R.id.tab_layout)
        vp = findViewById(R.id.view_pager)
        btn_update = findViewById(R.id.btn_update)
        search = findViewById(R.id.text_input)
        gif_load = findViewById(R.id.gif_load)
        vp.adapter = PageAdapter(supportFragmentManager, resources)
        tb.setupWithViewPager(vp)
        btn_update.setOnClickListener(this)
        observerTextInput()
        observer()
        super.onCreate(savedInstanceState)
    }
    fun observerTextInput(){
        search.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                mainVM.searchQuery.postValue(v.text.toString())
                return@setOnEditorActionListener true
            }else{
                Log.i("self-update-search","other key ... $actionId")
            }
            false
        }
    }
    fun observer(){
        mainVM.movieToUpdate.observe(this){
            val countSerial = mainVM.serialToUpdate.value?.size ?: 0
            val count = it.size + countSerial
            when{
                count > 0 -> {
                    btn_update.text = "Обновить ($count)"
                    btn_update.isEnabled = true
                }
                else -> {
                    btn_update.text = "Обновить"
                    btn_update.isEnabled = false
                }
            }
        }
        mainVM.serialToUpdate.observe(this){
            val countMovie = mainVM.movieToUpdate.value?.size ?: 0
            val count = it.size + countMovie
            when{
                count > 0 -> {
                    btn_update.text = "Обновить ($count)"
                    btn_update.isEnabled = true
                    btn_update.isClickable = true
                }
                else -> {
                    btn_update.text = "Обновить"
                    btn_update.isEnabled = false
                    btn_update.isClickable = false
                }
            }
        }
        mainVM.toastLiveData.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_update -> {
                mainVM.update()
            }
        }
    }
}