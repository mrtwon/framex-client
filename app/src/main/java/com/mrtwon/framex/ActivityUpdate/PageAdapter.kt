package com.mrtwon.framex.ActivityUpdate

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrtwon.framex.R

class PageAdapter(private val fm: FragmentManager, res: Resources): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val title = arrayListOf<String>(
        res.getString(R.string.tab_movie),
        res.getString(R.string.tab_serial),
        res.getString(R.string.tab_added)
    )
    val fragment = arrayListOf<Fragment>(
        FragmentListMovie(),
        FragmentListSerial(),
        FragmentListToUpdate()
    )
    override fun getCount(): Int {
        return title.size
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}