package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.bmart.R

class Terms : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val backBtn: ImageButton = findViewById(R.id.back_button)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "Home")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }
    }
}