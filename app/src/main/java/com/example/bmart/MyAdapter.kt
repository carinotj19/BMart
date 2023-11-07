package com.example.bmart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var context: Context, private var vendorsArrayList: ArrayList<Vendors>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.vendor_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vendors = vendorsArrayList[position]
        holder.vendorsName.text = vendors.vendorsName
        holder.vendorsImage.setImageResource(vendors.vendorsImage)
    }

    override fun getItemCount(): Int {
        return vendorsArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vendorsImage: ImageView
        var vendorsName: TextView
        var vendorsRating: TextView
        var vendorLocation: TextView

        init {
            vendorsImage = itemView.findViewById(R.id.vendor_image)
            vendorsName = itemView.findViewById(R.id.vendor_name)
            vendorsRating = itemView.findViewById(R.id.vendor_rate)
            vendorLocation = itemView.findViewById(R.id.vendor_loc)
        }
    }
}