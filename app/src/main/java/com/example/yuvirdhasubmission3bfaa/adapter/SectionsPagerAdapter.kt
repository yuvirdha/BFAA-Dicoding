package com.example.yuvirdhasubmission3bfaa.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.yuvirdhasubmission3bfaa.fragment.FgFragment
import com.example.yuvirdhasubmission3bfaa.fragment.FsFragment
import com.example.yuvirdhasubmission3bfaa.R

class SectionsPagerAdapter(private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    //deklarasi username untuk menampilkan list follow fragment
    var username: String? = null

    //set tablayout name
    companion object{
        @StringRes
        private val TAB = intArrayOf(
            R.string.tab1,
            R.string.tab2
        )
    }

    @Nullable
    override fun getPageTitle(pos:Int): CharSequence? {
        //get tablayout name based on the position
        return context.resources?.getString(TAB[pos])
    }

    override fun getItem(pos: Int): Fragment {
        //get the fragment content based on the position
        var fragment: Fragment? = null

        when (pos) {
            0 -> fragment = FgFragment()
            1 -> fragment = FsFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

}