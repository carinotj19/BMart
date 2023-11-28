package com.example.bmart.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bmart.fragments.Items
import com.example.bmart.fragments.Vendors

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Vendors()
            1 -> Items()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}

