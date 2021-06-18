package com.mrtwon.framex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.mrtwon.framex.ActivityUpdate.ActivityUpdate

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

    /*inner class NetworkState : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val manager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        }

    }*/
}