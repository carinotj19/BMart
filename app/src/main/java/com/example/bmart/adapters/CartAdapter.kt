package com.example.bmart.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.models.CartItemModel
import com.example.bmart.R
import com.example.bmart.fragments.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CartAdapter(
    var context: Context,
    private var cartArrayList: List<CartItemModel>,
    private var cartFragment: Cart) :
    RecyclerView.Adapter<CartAdapter.MyViewHolder?>() {

    private val fireStore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.cart_items_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val carts = cartArrayList[position]

        val quantityTextView = holder.itemView.findViewById<TextView>(R.id.item_quantity)
        val addButton = holder.itemView.findViewById<ImageButton>(R.id.add)
        val minusButton = holder.itemView.findViewById<ImageButton>(R.id.minus)

        var quantity = carts.quantity
        quantityTextView.text = quantity.toString()

        addButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
            carts.quantity = quantity
            updateItemPrice(holder, carts)
        }

        minusButton.setOnClickListener {
            if (quantity > 0) {
                quantity--
                quantityTextView.text = quantity.toString()
                carts.quantity = quantity
                updateItemPrice(holder, carts)
            }
        }

        holder.itemsName.text = carts.itemsName
        holder.vendorsName.text = carts.vendorsName
        updateItemPrice(holder, carts)
        Picasso.get().load(carts.itemsImage).into(holder.itemsImage)
    }

    override fun getItemCount(): Int {
        return cartArrayList.size
    }

    private fun updateItemPrice(holder: MyViewHolder, carts: CartItemModel) {
        val priceString = carts.itemPrice.replace("₱", "")
        val price = priceString.toDouble()
        val quantity = carts.quantity
        val total = price * quantity
        holder.itemPrice.text = "₱${String.format("%.2f", total)}"

        // Call the updateTotal function in the Cart fragment
        cartFragment.updateTotal()
        if (quantity == 0) {
            cartFragment.removeFromCart(carts)
        } else {
            updateQuantityInFirestore(carts)
        }
    }

    private fun updateQuantityInFirestore(carts: CartItemModel) {
        auth.currentUser?.uid?.let { userId ->
            fireStore.collection("Cart")
                .document(userId)
                .collection("Users")
                .whereEqualTo("productName", carts.itemsName)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Update the quantity field in the Firestore document
                        fireStore.collection("Cart")
                            .document(userId)
                            .collection("Users")
                            .document(document.id)
                            .update("quantity", carts.quantity)
                            .addOnSuccessListener {
                                // Quantity updated successfully in Firestore
                                Toast.makeText(context, "Quantity updated in cart", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                // Handle the failure to update quantity in Firestore
                                Toast.makeText(context, "Failed to update quantity in cart: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure to fetch the item from Firestore
                    Toast.makeText(context, "Failed to fetch item for quantity update: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemsImage: ImageView
        var itemsName: TextView
        var vendorsName: TextView
        var itemPrice: TextView
        private var isSelectFilled: Boolean = false

        init {
            itemsImage = itemView.findViewById(R.id.item_pic)
            itemsName = itemView.findViewById(R.id.item_name)
            vendorsName = itemView.findViewById(R.id.item_origin)
            itemPrice = itemView.findViewById(R.id.item_price)
        }
    }
}