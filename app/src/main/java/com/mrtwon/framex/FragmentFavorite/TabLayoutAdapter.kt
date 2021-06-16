package com.mrtwon.framex.FragmentFavorite

import com.mrtwon.framex.FragmentTop.FragmentTopContent

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrtwon.framex.Content.CollectionContentEnum
import com.mrtwon.framex.Content.ContentTypeEnum
import com.mrtwon.framex.Content.GenresEnum
import com.mrtwon.framex.Content.ParcelableEnum
import com.mrtwon.framex.R

class TabLayoutAdapter(fm: FragmentManager, res: Resources): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val titleList = arrayListOf<String>(
        res.getString(R.string.tab_favorite),
        res.getString(R.string.tab_recent))

    val fragmentList = arrayListOf<Fragment>(
        FragmentFavorite())

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}