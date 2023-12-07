package com.example.bmart.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.bmart.R
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Checkout : AppCompatActivity() {
    private lateinit var selectIcon: ImageButton
    private lateinit var continueButton: Button
    private lateinit var date: Date
    private lateinit var time: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "cart")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }
        var isSelected = false
        selectIcon = findViewById(R.id.select_icon)
        selectIcon.setOnClickListener {
            isSelected = !isSelected
            if (isSelected){
                selectIcon.setImageResource(R.drawable.select_icon_filled)
            } else {
                selectIcon.setImageResource(R.drawable.select_icon_outline)
            }
        }

        initializeDateTime()

        continueButton = findViewById(R.id.checkout_btn)
        continueButton.setOnClickListener {
            val intent = Intent(this, Checkout2::class.java)
            intent.putExtra("DATE", date)
            intent.putExtra("TIME", time)
            startActivity(intent)
            finish()
        }
    }


    fun initializeDateTime() {
        // Assign values to the lateinit variables
        date = Date()
        time = Date()
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()

        // Date Picker
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                date = calendar.time

                // Time Picker
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        time = calendar.time

                        // Display the selected date and time if needed
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val timeFormat = SimpleDateFormat("HH:mm:ss")
                        println("Selected Date: ${dateFormat.format(date)}")
                        println("Selected Time: ${timeFormat.format(time)}")
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )

                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
}