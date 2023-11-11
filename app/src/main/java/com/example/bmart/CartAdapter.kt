package com.example.bmart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.fragments.Cart

class CartAdapter(var context: Context, private var cartArrayList: ArrayList<CartItems>, private var cartFragment: Cart) :
    RecyclerView.Adapter<CartAdapter.MyViewHolder?>() {
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
        holder.itemsImage.setImageResource(carts.itemsImage)
    }

    override fun getItemCount(): Int {
        return cartArrayList.size
    }

    private fun updateItemPrice(holder: MyViewHolder, carts: CartItems) {
        val priceString = carts.itemPrice.replace("₱", "")
        val price = priceString.toDouble()
        val quantity = carts.quantity
        val total = price * quantity
        holder.itemPrice.text = "₱${String.format("%.2f", total)}"
        // Call the updateTotal function in the Cart fragment
        cartFragment.updateTotal()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemsImage: ImageView
        var itemsName: TextView
        var vendorsName: TextView
        var itemPrice: TextView
        var selectIcon: ImageButton
        private var isSelectFilled: Boolean = false

        init {
            itemsImage = itemView.findViewById(R.id.item_pic)
            itemsName = itemView.findViewById(R.id.item_name)
            vendorsName = itemView.findViewById(R.id.item_origin)
            itemPrice = itemView.findViewById(R.id.item_price)
            selectIcon = itemView.findViewById(R.id.item_select)

            selectIcon.setOnClickListener {
                isSelectFilled = !isSelectFilled
                updateSelectIcon()
            }
        }

        private fun updateSelectIcon() {
            if (isSelectFilled) {
                // Set the select icon to filled state
                selectIcon.setImageResource(R.drawable.select_icon_filled)
                selectIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green_700))
            } else {
                // Set the select icon to outlined state
                selectIcon.setImageResource(R.drawable.select_icon_outline)
                selectIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green_700))
            }
        }
    }
}