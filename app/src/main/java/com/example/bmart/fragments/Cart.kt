package com.example.bmart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Adapters.CartAdapter
import com.example.bmart.Models.CartItemModel
import com.example.bmart.R
import com.example.bmart.Checkout
import com.example.bmart.SharedPreferencesHelper

class Cart : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val cartsArrayList = mutableListOf<CartItemModel>()

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
        loadCartItems()

        recyclerView = view.findViewById(R.id.cart_items)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        val cartAdapter = CartAdapter(requireContext(), cartsArrayList, this)
        recyclerView.adapter = cartAdapter

        updateTotal()
        val checkoutButton = view.findViewById<Button>(R.id.cart_checkout_btn)
        checkoutButton.setOnClickListener {
            val intent = Intent(activity, Checkout::class.java)
            startActivity(intent)
        }

        val addDummyDataButton = view.findViewById<Button>(R.id.add_dummy_data)
        addDummyDataButton.setOnClickListener {
            addDummyData()
        }
    }
    private fun addDummyData() {
        val itemsImage = R.drawable.item
        val itemName = "Dummy Item"
        val vendorName = "Dummy Vendor"
        val itemPrice = 25.0

        val formattedItemPrice = "₱${String.format("%.2f", itemPrice)}"

        val dummyCartItem = CartItemModel(itemsImage, itemName, vendorName, formattedItemPrice)
        addToCart(dummyCartItem)
        Toast.makeText(context, "Dummy data added to cart", Toast.LENGTH_SHORT).show()
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
        cartsArrayList.clear()
        cartsArrayList.addAll(SharedPreferencesHelper.loadCartItems(requireContext()))

        val nothingInCartLayout = view?.findViewById<ViewGroup>(R.id.nothing_in_cart_layout)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.cart_items)

        if (cartsArrayList.isEmpty()) {
            nothingInCartLayout?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            nothingInCartLayout?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }
    private fun saveCartItems() {
        SharedPreferencesHelper.saveCartItems(requireContext(), cartsArrayList)
    }
    private fun addToCart(carts: CartItemModel) {
        cartsArrayList.add(carts)
        updateTotal()
        saveCartItems()
        loadCartItems()
        Toast.makeText(context, "Cart Updated", Toast.LENGTH_SHORT).show()
    }
    fun removeFromCart(carts: CartItemModel) {
        cartsArrayList.remove(carts)
        updateTotal()
        saveCartItems() // Save cart items after removing
    }
    override fun onDestroyView() {
        super.onDestroyView()
        saveCartItems()
    }
}