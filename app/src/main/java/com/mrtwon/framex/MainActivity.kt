package com.mrtwon.framex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.github.mrtwon.library.IgnoreCode
import com.github.mrtwon.library.XmlParse
import com.github.mrtwon.library.XmlParseBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.mrtwon.framex.ActivityUpdate.ActivityUpdate
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    lateinit var navController: NavController
    lateinit var bottomBar: BottomNavigationView
    val vm: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottomBar = findViewById(R.id.bottom_nav_menu)
        bottomBar.setOnItemSelectedListener(this)


        //если пришёл intent с намерением запустить фрагмент
        val redirect = intent.getStringExtra("redirect")
        if(redirect != null){
            when(redirect){
                "FragmentSubscription" -> navController.navigate(R.id.fragmentSubscription)
            }
        }
    }
    fun hiddenBottomBar(){
        bottomBar.visibility = View.GONE
    }
    fun showBottomBar(){
        bottomBar.visibility = View.VISIBLE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> { navController.navigate(R.id.fragmentFavorite) }
            R.id.home -> { navController.navigate(R.id.fragmentHome)}
            R.id.search -> {  navController.navigate(R.id.fragmentSearch) }
            R.id.update -> {
                startActivity(Intent(this, ActivityUpdate::class.java))
            }
        }
        return true
    }
    fun reselectedNavigationPosition(){
        val current = navController.currentDestination?.id
        when(current){
            R.id.fragmentHome -> {
                if(bottomBar.selectedItemId != R.id.home) {
                    bottomBar.selectedItemId = R.id.home
                }
            }
            R.id.fragmentFavorite -> {
                if(bottomBar.selectedItemId != R.id.favorite) {
                    bottomBar.selectedItemId = R.id.favorite
                }
            }
            R.id.fragmentSearch -> {
                if(bottomBar.selectedItemId != R.id.search) {
                    bottomBar.selectedItemId = R.id.search
                }
            }
        }
    }

    private fun log(s: String){
        Log.i("self-main",s)
    }
}