package com.example.bmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textView: TextView = findViewById<TextView>(R.id.sign_up)
        textView.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)

        })
    }
}