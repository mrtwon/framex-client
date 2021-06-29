package com.mrtwon.framex.ActivityWelcome

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.R

class ActivityWelcome: AppCompatActivity(), View.OnClickListener {
    lateinit var check_box: CheckBox
    lateinit var start: MaterialButton
    lateinit var image: ImageView
    //lateinit var welcome_text: TextView
    lateinit var web_view: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_welcome)
        start = findViewById(R.id.start)
        image = findViewById(R.id.action_to_check_box)
        check_box = findViewById(R.id.check_box)
        web_view = findViewById(R.id.web_view)
        start.setOnClickListener(this)
        observerCheckBox()
        setHtmlText()
        super.onCreate(savedInstanceState)
    }
    fun setHtmlText(){
        web_view.settings.javaScriptEnabled = true
        web_view.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        val html = """ 
<html>
<head>
<style>
   body{
	background: #D1D1D1;
	color:#000
   }
   mark {
    background: #CA5454;
    padding: 0 3px;
    border: 1px dashed #333;
   }
  p, span { display: inline; color:#ddd; }
  h2 { display: inline; }
  </style>
</head>
<div style="width:100%">
<h2>Frame<h2 style="color:#CA5454">X</h2></h2>
<ul>
<li>У нас <mark><p>нет рекламы</p></mark></li>
<li>Мы не монетизируем приложения</li>
<li>В базе более <mark><p>60 000 контента</p></mark></li>
<li>Уникальный поиск по описанию</li>
<li>Ежедневное добавление <mark><p>нового контента</p></mark></li>
</ul>
</html>
        """
        val encodedHtml: String = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
        web_view.loadData(encodedHtml, "text/html", "base64")
    }
    fun observerCheckBox(){
        check_box.setOnClickListener{
            if(check_box.isChecked){
                image.setImageResource(R.drawable.sherlok_welcome)
            }else{
                image.setImageResource(R.drawable.welcome_sherlok_02)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.start -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}