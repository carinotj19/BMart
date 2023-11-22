package com.example.bmart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Adapters.VendorAdapter
import com.example.bmart.Models.VendorModel

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

        val vendorsName = arrayOf(
            getString(R.string.vendors_a),
            getString(R.string.vendors_b),
            getString(R.string.vendors_c),
            getString(R.string.vendors_d),
            getString(R.string.vendors_e),
            getString(R.string.vendors_f),
            getString(R.string.vendors_g),
            getString(R.string.vendors_h),
            getString(R.string.vendors_i),
            getString(R.string.vendors_j),
        )

        val vendorsRating = floatArrayOf(4.5f, 4.2f, 3.8f, 4.0f, 3.7f, 4.1f, 3.9f, 4.3f, 4.4f, 3.6f)

        val vendorsLocation = arrayOf(
            getString(R.string.location_a),
            getString(R.string.location_b),
            getString(R.string.location_c),
            getString(R.string.location_d),
            getString(R.string.location_e),
            getString(R.string.location_f),
            getString(R.string.location_g),
            getString(R.string.location_h),
            getString(R.string.location_i),
            getString(R.string.location_j),
        )

        val vendorsImage = intArrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h,
            R.drawable.i,
            R.drawable.j
        )

        for (i in vendorsName.indices) {
            val vendorModel = VendorModel(
                vendorsName[i],
                vendorsImage[i],
                vendorsRating[i],
                vendorsLocation[i]
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