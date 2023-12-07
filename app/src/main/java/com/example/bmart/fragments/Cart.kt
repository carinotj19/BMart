package com.example.bmart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.adapters.CartAdapter
import com.example.bmart.models.CartItemModel
import com.example.bmart.R
import com.example.bmart.activities.Checkout
import com.example.bmart.helpers.SharedPreferencesHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Cart : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val cartsArrayList = mutableListOf<CartItemModel>()
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var trashIcon: ImageButton
    private lateinit var cartAdapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val checkoutButton = view.findViewById<Button>(R.id.cart_checkout_btn)
        checkoutButton.setOnClickListener {
            val intent = Intent(activity, Checkout::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.cart_items)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        cartAdapter = CartAdapter(requireContext(), cartsArrayList, this)
        recyclerView.adapter = cartAdapter

        auth = Firebase.auth
        fireStore = FirebaseFirestore.getInstance()
        trashIcon = view.findViewById(R.id.trash_icon)

        loadCartItems()
        updateTotal()
        val checkoutButton = view.findViewById<Button>(R.id.cart_checkout_btn)
        checkoutButton.setOnClickListener {
            val intent = Intent(activity, Checkout::class.java)
            startActivity(intent)
        }


        trashIcon.setOnClickListener {
            cartsArrayList.filter { it.isSelected }.forEach { removeFromCart(it) }
            updateTotal()
            updateTrashVisibility()
        }
    }
    fun updateTotal() {
        val totalTextView: TextView = view?.findViewById(R.id.cart_items_total) ?: return
        var totalPrice = 0.0
        for (item in cartsArrayList) {
            val priceString = item.itemPrice.replace("₱", "")
            val price = priceString.toDouble()
            totalPrice += price * item.quantity
        }

        totalTextView.text = "₱${String.format("%.2f", totalPrice)}"
    }
    private fun loadCartItems() {
        val defaultImage = "https://firebasestorage.googleapis.com/v0/b/bmart-179b3.appspot.com/o/defaultVendorImage.jpg?alt=media&token=bb7b47e0-8089-4f3f-a737-eb9580aa0ed4"
        cartsArrayList.clear()
        if (!::auth.isInitialized) {
            auth = FirebaseAuth.getInstance()
        }
        auth.currentUser?.uid?.let { userId ->
            fireStore.collection("Cart")
                .document(userId)
                .collection("Users")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val itemsImage = document.getString("itemImage") ?: defaultImage
                        val itemsName = document.getString("productName") ?: "Unknown Item"
                        val vendorsName = document.getString("vendorName") ?: "Unknown Vendor"
                        val itemPrice = document.getString("itemPrice") ?: "₱0.00"
                        val quantity = document.getLong("quantity")?.toInt() ?: 1

                        val cartItem = CartItemModel(itemsImage, itemsName, vendorsName, itemPrice)
                        cartItem.quantity = quantity
                        cartsArrayList.add(cartItem)
                    }

                    val nothingInCartLayout = view?.findViewById<ViewGroup>(R.id.nothing_in_cart_layout)
                    val recyclerView = view?.findViewById<RecyclerView>(R.id.cart_items)

                    if (cartsArrayList.isEmpty()) {
                        nothingInCartLayout?.visibility = View.VISIBLE
                        recyclerView?.visibility = View.GONE
                    } else {
                        nothingInCartLayout?.visibility = View.GONE
                        recyclerView?.visibility = View.VISIBLE
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }

                    // Update the total after fetching items
                    updateTotal()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to load cart items: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun removeFromCart(carts: CartItemModel) {
        cartsArrayList.remove(carts)
        // Only update the adapter when quantity is 0
        if (carts.quantity == 0) {
            updateTotal()
            removeFromFirestore(carts)
        }
    }

    fun updateTrashVisibility() {
        trashIcon = view?.findViewById(R.id.trash_icon) ?: return

        // Check if at least one item is selected
        val isAnyItemSelected = cartsArrayList.any {
            Log.d("ITEM SELECT", "$it")
            it.isSelected
        }

        // Set the visibility of the trash icon based on selection status
        trashIcon.visibility = if (isAnyItemSelected) View.VISIBLE else View.GONE
    }

    private fun removeFromFirestore(carts: CartItemModel) {
        auth.currentUser?.uid?.let { userId ->
            fireStore.collection("Cart")
                .document(userId)
                .collection("Users")
                .whereEqualTo("productName", carts.itemsName)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Delete the document from Firestore
                        fireStore.collection("Cart")
                            .document(userId)
                            .collection("Users")
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Item deleted from cart", Toast.LENGTH_SHORT).show()
                                loadCartItems() // Fetch updated data after deletion
                                recyclerView.adapter?.notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                // Handle the failure to delete the item from Firestore
                                Toast.makeText(context, "Failed to delete item from cart: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure to fetch the item from Firestore
                    Toast.makeText(context, "Failed to fetch item for deletion: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}