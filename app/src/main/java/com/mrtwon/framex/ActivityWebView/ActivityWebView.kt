package com.mrtwon.framex.ActivityWebView

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content

class ActivityWebView: AppCompatActivity() {
    lateinit var web_view: WebView
    var id: Int? = null
    var contentType: String? = null
    val vm: WebViewVM by lazy { ViewModelProvider(this).get(WebViewVM::class.java) }
    lateinit var observer: Observer<Content>

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_webview)
        web_view = findViewById(R.id.web_view)
        initWebView(web_view)

        id = intent.getIntExtra("id", 0)
        contentType = intent.getStringExtra("content_type")

        if(id != null && contentType != null){
            Log.i("web_view","if")
            observerContent()
            vm.getVideoLink(id!!, contentType!!)
            vm.addRecent(id!!, contentType!!)
        }else{
            Log.i("web_view","else")
        }
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(wv: WebView){
        wv.apply {
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            //systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false
        }
        //val decor = window.decorView
        //decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN


    }

    fun observerContent(){
        observer = Observer {
            web_view.loadUrl("https:"+it.iframe_src)
        }
        vm.content.observe(this, observer)
    }

    override fun onDestroy() {
        vm.content.removeObserver(observer)
        super.onDestroy()
    }
}