package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.OrderHistoryAdapter
import com.example.bmart.models.OrderHistoryDate
import com.example.bmart.models.OrderItem

class Orders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val backBtn: ImageButton = findViewById(R.id.back_button)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "home")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }

        // Assuming you have a RecyclerView in your layout with the id "orderHistoryRecyclerView"
        val recyclerView: RecyclerView = findViewById(R.id.orderHistoryRecyclerView)

        // Sample data (replace with your actual order history data)
        val orderHistoryList = listOf(
            OrderHistoryDate("2023-12-01"),
            OrderItem("Item 1", R.drawable.item, 2, 19.99),
            OrderItem("Item 2", R.drawable.item, 1, 25.99),
            OrderHistoryDate("2023-12-02"),
            OrderItem("Item 3", R.drawable.item, 3, 15.99)
            // Add more data as needed
        )

        // Set up the RecyclerView with your custom adapter
        val adapter = OrderHistoryAdapter(orderHistoryList)
        recyclerView.adapter = adapter

        // Assuming you want a vertical LinearLayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }
}