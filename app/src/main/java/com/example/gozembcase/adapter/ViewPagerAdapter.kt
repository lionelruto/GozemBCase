package com.example.gozembcase.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fa, lifecycle) {

    val fragmentlist: ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int {
        return fragmentlist.size
    }

   fun addFragment(fragment: Fragment){
       fragmentlist.add(fragment)
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentlist.get(position)
    }

}