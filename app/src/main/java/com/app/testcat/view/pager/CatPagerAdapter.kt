package com.app.testcat.view.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CatPagerAdapter(fragment: Fragment,
                      private val fragments: List<Fragment>
): FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}