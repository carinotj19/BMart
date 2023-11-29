package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.bmart.R

class VendorDetails : AppCompatActivity() {
    private var isHeartFilled: Boolean = false
    private lateinit var heartIcon: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_details)
        val vendorName: TextView = findViewById(R.id.vendor_name)
        val vendorRating: TextView = findViewById(R.id.vendor_rating)
        val vendorProfile: ImageView = findViewById(R.id.vendor_profile)
        val backBtn = findViewById<ImageButton>(R.id.back_button)
        heartIcon = findViewById(R.id.heart_icon)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "cart")
            setResult(RESULT_OK, intent)
            finish()
        }
        heartIcon.setOnClickListener {
            isHeartFilled = !isHeartFilled
            updateHeartIcon()
        }

        vendorName.text = intent.getStringExtra("VENDORS-NAME")
        vendorRating.text = intent.getFloatExtra("VENDORS-RATING", 0.0F).toString()
        vendorProfile.setImageResource(intent.getIntExtra("VENDOR-PROFILE", 0))
    }

    private fun updateHeartIcon() {
        if (isHeartFilled) {
            // Set the heart icon to filled state
            heartIcon.setImageResource(R.drawable.favorite_icon_filled)
            heartIcon.setColorFilter(ContextCompat.getColor(this, R.color.red))
        } else {
            // Set the heart icon to outlined state
            heartIcon.setImageResource(R.drawable.favorite_icon_outlined)
            heartIcon.setColorFilter(ContextCompat.getColor(this, R.color.red))
        }
    }
}