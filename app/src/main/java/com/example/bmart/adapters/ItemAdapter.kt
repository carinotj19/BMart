package com.example.bmart.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.models.ItemModel
import com.example.bmart.R
import com.example.bmart.models.VendorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ItemAdapter(
    private var itemModelArrayList: List<ItemModel>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ItemAdapter.MyViewHolder?>() {

    private val fireStore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = itemModelArrayList[position]
        val formattedItemPrice = "₱${String.format("%.2f", items.itemsPrice)}"
        holder.itemsPrice.text = formattedItemPrice
        holder.itemsName.text = items.itemsName
        Picasso.get().load(items.itemsImage).into(holder.itemsImage)
    }

    override fun getItemCount(): Int {
        return itemModelArrayList.size
    }
    fun updateData(newItems: List<ItemModel>) {
        itemModelArrayList = newItems
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var itemsImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemsPrice: TextView = itemView.findViewById(R.id.item_price)
        var itemsName: TextView = itemView.findViewById(R.id.item_name)
        private var addToCart: ImageButton = itemView.findViewById(R.id.add_to_cart)
        init {
            itemView.setOnClickListener(this)
            addToCart.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Get the clicked item
                    val clickedItem = itemModelArrayList[position]

                    addToCart(clickedItem)
                }
            }
        }
        private fun addToCart(item: ItemModel) {
            val defaultPrice = 25.00
            val formattedItemPrice = "₱${String.format("%.2f", defaultPrice)}"
            val cartMap = hashMapOf(
                "productName" to item.itemsName,
                "vendorName" to item.vendorName,
                "itemImage" to item.itemsImage,
                "itemPrice" to formattedItemPrice,
                "quantity" to 1,
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
                            Toast.makeText(itemView.context, "Item added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle the failure to add the item to cart
                            Toast.makeText(itemView.context, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(itemModelArrayList[position])
            }
        }
    }


    interface OnItemClickListener{
        fun onItemClick(item: ItemModel)
    }
}