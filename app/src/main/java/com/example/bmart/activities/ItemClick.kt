package com.example.bmart.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.bmart.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ItemClick : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var itemQuantity: TextView
    private lateinit var addToCart: Button
    private lateinit var addBtn: ImageButton
    private lateinit var minusBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_click)

        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val backBtn: ImageButton = findViewById(R.id.back_button)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.putExtra("fragment", "home")
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }
        itemImage = findViewById(R.id.item_image)
        itemName = findViewById(R.id.item_name)
        itemPrice = findViewById(R.id.item_price)
        itemQuantity = findViewById(R.id.item_quantity)
        addToCart = findViewById(R.id.add_to_cart)
        addBtn = findViewById(R.id.add_button)
        minusBtn = findViewById(R.id.minus_button)

        val productName = intent.getStringExtra("ITEM_NAME")
        itemName.text = productName
        val price = intent.getDoubleExtra("ITEM_PRICE", 0.0)
        val formattedItemPrice = "₱${String.format("%.2f", price)}"
        itemPrice.text = formattedItemPrice
        val profileImage = intent.getStringExtra("ITEM_PROFILE")
        Picasso.get().load(profileImage).into(itemImage)
        val vendorName = intent.getStringExtra("VENDOR_NAME")

        var quantity = 1
        itemQuantity.text = quantity.toString()

        addToCart.setOnClickListener{
            val cartMap = hashMapOf(
                "productName" to productName,
                "vendorName" to vendorName,
                "itemImage" to profileImage,
                "itemPrice" to formattedItemPrice,
                "quantity" to quantity,
            )

            auth.currentUser?.uid?.let { userId ->
                fireStore.collection("Cart")
                    .document(userId)
                    .collection("Users")
                    .add(cartMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Item added to cart successfully
                            // You can show a toast or perform any other actions here
                            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle the failure to add the item to cart
                            Toast.makeText(this, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        addBtn.setOnClickListener {
            quantity++
            itemQuantity.text = quantity.toString()
        }

        minusBtn.setOnClickListener {
            if (quantity > 1) {
                quantity--
                itemQuantity.text = quantity.toString()
            }
        }
    }
}