package com.example.bmart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.bmart.R

class Confirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val checkIcon: ImageView = findViewById(R.id.check_icon)
        val successText: TextView = findViewById(R.id.order_success)
        val processing: TextView = findViewById(R.id.processing)
        val backToMenu: TextView = findViewById(R.id.back_to_menu)

        Handler().postDelayed({
            // Hide progress bar
            progressBar.visibility = View.GONE
            processing.visibility = View.GONE
            // Make other views visible
            checkIcon.visibility = View.VISIBLE
            successText.visibility = View.VISIBLE
            backToMenu.visibility = View.VISIBLE
        }, 3000)
    }
}