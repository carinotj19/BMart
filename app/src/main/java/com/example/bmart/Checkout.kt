package com.example.bmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Checkout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "cart")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}