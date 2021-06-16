package com.mrtwon.framex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dueeeke.tablayout.SegmentTabLayout
import com.dueeeke.tablayout.SlidingTabLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.FragmentTop.FragmentTop
import kotlinx.android.synthetic.main.fragment_top_content.view.*

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