package com.mrtwon.framex.ActivityWebView

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mrtwon.framex.R
import com.mrtwon.framex.room.Content
import java.io.ByteArrayInputStream


class ActivityWebView: AppCompatActivity() {
    lateinit var web_view: WebView
    var id: Int? = null
    var contentType: String? = null
    val vm: WebViewVM by lazy { ViewModelProvider(this).get(WebViewVM::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_webview)
        web_view = findViewById(R.id.web_view)
        initWebView(web_view)
        id = intent.getIntExtra("id", 0)
        contentType = intent.getStringExtra("content_type")
        observerContent()
        vm.getVideoLink(id!!, contentType!!)
        vm.addRecent(id!!, contentType!!)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(wv: WebView){
        wv.apply {
            webViewClient = BlockOtherVideo()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    fun observerContent(){
        vm.content.observe(this){
            it.iframe_src = "http:${it.iframe_src}"
            web_view.loadUrl(it.iframe_src!!)
        }
    }

    private inner class BlockOtherVideo internal constructor() : WebViewClient() {

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            if(isBlockedUrl(request)){
                log("blocked resource: ${request?.url?.host}")
                return createEmptyResource()
            }else{
                log("not blocked url: ${request?.url?.host}")
                return null
            }
        }

        fun createEmptyResource(): WebResourceResponse {
            return WebResourceResponse(
                "text/plain",
                "utf-8",
                ByteArrayInputStream("".toByteArray())
            )
        }

        fun isBlockedUrl(webResource: WebResourceRequest?): Boolean{
            if(webResource == null) return false
            val accessHost = "cdnland.in"
            val currentHost = webResource.url.host
            val currentPath = webResource.url.path
            log("host: $currentHost | path $currentPath")
            if(currentHost == null || currentPath == null){
                return false
            }
            if(".mp4" in currentPath){
                return accessHost !in currentHost
            }
            return false
        }

    }

    override fun onStart() {
        log("onStart()")
        super.onStart()
    }

    override fun onPause() {
        log("onPause()")
        super.onPause()
    }

    override fun onDestroy() {
        log("onDestroy()")
        super.onDestroy()
    }

    fun log(s: String){
        Log.i("self-webview-debug",s)
    }

}