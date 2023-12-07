package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.bmart.R
import java.text.SimpleDateFormat
import java.util.Date

class Checkout2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout2)

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Checkout::class.java)
            startActivity(intent)
            finish()
        }

        val dateTimeTextView = findViewById<TextView>(R.id.date_time)

        // Retrieve date and time from Intent extras
        val intent = intent
        if (intent != null && intent.hasExtra("DATE") && intent.hasExtra("TIME")) {
            val date = intent.getSerializableExtra("DATE") as Date
            val time = intent.getSerializableExtra("TIME") as Date

            // Format date and time for display
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val timeFormat = SimpleDateFormat("HH:mm:ss")

            val formattedDate = dateFormat.format(date)
            val formattedTime = timeFormat.format(time)

            // Display the formatted date and time in TextViews
            dateTimeTextView.text = "${formattedDate} | ${formattedTime}"
        }

        val confirmBtn = findViewById<Button>(R.id.confirm_button)
        confirmBtn.setOnClickListener {
            val confirmIntent = Intent(this, Confirmation::class.java)
            startActivity(confirmIntent)
            finish()
        }
    }
}