package com.mrtwon.framex.FragmentTop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.dueeeke.tablayout.SlidingTabLayout
import com.mrtwon.framex.Content.CollectionContentEnum
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.Content.ParcelableEnum
import com.mrtwon.framex.MainActivity
import com.mrtwon.framex.MainViewModel
import com.mrtwon.framex.R
import kotlinx.android.synthetic.main.fragment_top_content.view.*
import pl.droidsonroids.gif.GifImageView

class FragmentTop: Fragment() {
    //val vm: MainViewModel by lazy { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
    lateinit var view_pager: ViewPager
    lateinit var tab_layout: SlidingTabLayout
    var imageResId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        imageResId = arguments?.getInt("img_resource")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_content, container, false)
        view_pager = view.view_pager
        tab_layout = view.tab_layout
        imageResId?.let {
            view.top_image.setImageResource(it)
        }
        view_pager.adapter = TabLayoutAdapter(childFragmentManager, resources, requireArguments())
        tab_layout.setViewPager(view_pager)
        return view
    }

    companion object{
        fun instance(): Fragment{
            return FragmentTop()
        }
    }

    override fun onDestroy() {
        Log.i("self-top-content","onDestroy()")
        super.onDestroy()
    }
}