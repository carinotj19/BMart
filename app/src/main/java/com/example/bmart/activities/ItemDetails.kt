package com.example.bmart.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.models.VendorModel
import io.github.serpro69.kfaker.faker
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

class ItemDetails : AppCompatActivity(), VendorAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorModelArrayList: ArrayList<VendorModel>
    private var isHeartFilled: Boolean = false
    private lateinit var heartIcon: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        val itemName: TextView = findViewById(R.id.item_name)
        val itemImage: ImageView = findViewById(R.id.item_image)
        val backBtn: ImageButton = findViewById(R.id.back_button)
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

        itemName.text = intent.getStringExtra("ITEMS-NAME")
        itemImage.setImageResource(intent.getIntExtra("ITEMS-IMAGE", 0))

        dataInitialize()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val vendorAdapter = VendorAdapter(vendorModelArrayList, this)
        recyclerView.adapter = vendorAdapter
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

    private fun dataInitialize() {
        vendorModelArrayList = arrayListOf()
        val faker = faker {  }

        for (i in 0 until 20) {
            val vendorName = faker.name.name()
            val vendorRating = Random.nextFloat() * (5.0 - 1.0) + 1.0
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            val vendorLocation = faker.address.country()

            val vendorModel = VendorModel(
                vendorName,
                R.drawable.a,
                df.format(vendorRating).toFloat(),
                vendorLocation,
            )
            vendorModelArrayList.add(vendorModel)
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem = vendorModelArrayList[position]
        val intent = Intent(this, VendorDetails::class.java)
        val vendorName = clickedItem.vendorsName
        val vendorRating = clickedItem.vendorsRating
        val vendorImage = clickedItem.vendorsImage
        intent.putExtra("VENDORS-NAME", vendorName)
        intent.putExtra("VENDORS-RATING", vendorRating)
        intent.putExtra("VENDOR-PROFILE", vendorImage)
        startActivity(intent)
    }
}