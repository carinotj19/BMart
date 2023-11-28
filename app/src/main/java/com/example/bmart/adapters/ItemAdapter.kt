package com.example.bmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.models.ItemModel
import com.example.bmart.R

class ItemAdapter(
    private var itemModelArrayList: ArrayList<ItemModel>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ItemAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = itemModelArrayList[position]
        holder.vendorsName.text = items.vendorName
        holder.itemsName.text = items.itemsName
        holder.itemsImage.setImageResource(items.itemsImage)
    }

    override fun getItemCount(): Int {
        return itemModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var itemsImage: ImageView = itemView.findViewById(R.id.item_image)
        var vendorsName: TextView = itemView.findViewById(R.id.vendor_name)
        var itemsName: TextView = itemView.findViewById(R.id.item_name)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}