package com.mrtwon.framex.ActivityUpdate

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex.R

class ActivityUpdateSearch: AppCompatActivity(), View.OnClickListener {
    lateinit var tb: TabLayout
    lateinit var vp: ViewPager
    lateinit var btn_update: Button
    lateinit var search: TextInputEditText
    lateinit var progress_bar: ProgressBar
    val mainVM: UpdateSearchVM by lazy { ViewModelProvider(this).get(UpdateSearchVM::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_update_one_element)
        tb = findViewById(R.id.tab_layout)
        vp = findViewById(R.id.view_pager)
        btn_update = findViewById(R.id.btn_update)
        search = findViewById(R.id.text_input)
        progress_bar = findViewById(R.id.progress_bar)
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
                mainVM.search(search.text.toString())
                return@setOnEditorActionListener true
            }else{
                Log.i("self-update-search","other key ... $actionId")
            }
            /* if(event.action == KeyEvent.ACTION_DOWN){
                if(actionId == KeyEvent.KEYCODE_ENTER){
                    mainVM.search(search.text.toString())
                    Log.i("self-update-search","search")
                }else{
                    Log.i("self-update-search","other... $actionId")
                }
            }*/
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
        mainVM.searchQuery.observe(this){
            mainVM.search(it)
        }
        mainVM.visibilityPb.observe(this){
            if(it){
                vp.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
            }else{
                progress_bar.visibility = View.GONE
                vp.visibility = View.VISIBLE
            }
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