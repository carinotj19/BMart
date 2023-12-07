package com.example.bmart.activities.vsActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.example.bmart.R
import com.example.bmart.activities.MyStore
import com.example.bmart.activities.Profile
import com.example.bmart.adapters.FragmentStateAdapter
import com.example.bmart.adapters.TabAdapter
import com.example.bmart.fragments.CanceledOrders
import com.example.bmart.fragments.CompletedOrders
import com.example.bmart.fragments.ToPack
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Orders : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentStateAdapter: FragmentStateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders2)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, MyStore::class.java)
            startActivity(intent)
            finish()
        }

        // Create a list of fragments
        val fragments = listOf(ToPack(), CompletedOrders(), CanceledOrders())
        // Create a list of tab names
        val tabNames = listOf("To Pack", "Completed Orders", "Canceled Orders")
        // Initialize FragmentStateAdapter with fragments and tab names
        fragmentStateAdapter = FragmentStateAdapter(this, fragments, tabNames)
        viewPager.adapter = fragmentStateAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentStateAdapter.getTabName(position)
        }.attach()
    }
}