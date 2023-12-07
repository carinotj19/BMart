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
import com.example.bmart.models.SimpleItemModel
import com.example.bmart.models.VendorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class SimpleItemAdapter(
    private var simpleItemModelArrayList: List<SimpleItemModel>,
) :
    RecyclerView.Adapter<SimpleItemAdapter.MyViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list_seller, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val simpleItems = simpleItemModelArrayList[position]
        val formattedItemPrice = "₱${String.format("%.2f", simpleItems.itemsPrice)} /kilo"
        val formattedItemQuantity = "${simpleItems.itemsQuantity} kilo/s left"
        holder.itemsPrice.text = formattedItemPrice
        holder.itemsName.text = simpleItems.itemsName
        holder.itemsQuantity.text = formattedItemQuantity
        holder.itemsImage.setImageResource(simpleItems.itemsImage)
    }

    override fun getItemCount(): Int {
        return simpleItemModelArrayList.size
    }
    fun updateData(newItems: List<SimpleItemModel>) {
        simpleItemModelArrayList = newItems
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemsImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemsPrice: TextView = itemView.findViewById(R.id.item_price)
        var itemsName: TextView = itemView.findViewById(R.id.item_name)
        var itemsQuantity: TextView = itemView.findViewById(R.id.item_quantity)
    }
}