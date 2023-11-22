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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Adapters.CartAdapter
import com.example.bmart.Models.CartItemModel
import com.example.bmart.R
import com.example.bmart.Checkout

class Cart : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartsArrayList: ArrayList<CartItemModel>

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

        dataInitialize()

        recyclerView = view.findViewById(R.id.cart_items)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        val cartAdapter = CartAdapter(requireContext(), cartsArrayList, this)
        recyclerView.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()

        updateTotal()
        val checkoutButton = view.findViewById<Button>(R.id.cart_checkout_btn)
        checkoutButton.setOnClickListener {
            val intent = Intent(activity, Checkout::class.java)
            startActivity(intent)
        }
    }

    fun updateTotal() {
        val totalTextView: TextView = view?.findViewById(R.id.cart_items_total) ?: return
        Log.d("CartFragment", "updateTotal() called")
        var totalPrice = 0.0
        for (item in cartsArrayList) {
            val priceString = item.itemPrice.replace("₱", "")
            val price = priceString.toDouble()
            totalPrice += price * item.quantity
        }

        totalTextView.text = "₱${String.format("%.2f", totalPrice)}"
    }

    private fun dataInitialize() {
        cartsArrayList = arrayListOf<CartItemModel>()

        val itemsImage = intArrayOf(
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item
        )

        val itemsName = arrayOf(
            "Item A",
            "Item B",
            "Item C",
            "Item D",
            "Item E",
            "Item F",
            "Item G",
            "Item H",
            "Item I",
            "Item J",
        )

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

        val itemPrice = floatArrayOf(20.00f, 30.00f, 30.80f, 40.00f, 30.70f, 40.10f, 30.90f, 40.30f, 40.40f, 30.60f)

        val formattedItemPrice = itemPrice.map { "₱$it" }.toTypedArray()

        for (i in itemsName.indices) {
            val carts = CartItemModel(itemsImage[i], itemsName[i], vendorsName[i], formattedItemPrice[i])
            cartsArrayList.add(carts)
        }
    }
}