package com.example.bmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.models.VendorModel

class VendorAdapter(
    private var vendorModelArrayList: ArrayList<VendorModel>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<VendorAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vendor_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vendors = vendorModelArrayList[position]
        holder.vendorsName.text = vendors.vendorsName
        holder.vendorsImage.setImageResource(vendors.vendorsImage)
        holder.vendorsRating.text = vendors.vendorsRating.toString()
        holder.vendorLocation.text = vendors.vendorsLocation
    }

    override fun getItemCount(): Int {
        return vendorModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var vendorsImage: ImageView = itemView.findViewById(R.id.vendor_image)
        var vendorsName: TextView = itemView.findViewById(R.id.vendor_name)
        var vendorsRating: TextView = itemView.findViewById(R.id.vendor_rate)
        var vendorLocation: TextView = itemView.findViewById(R.id.vendor_loc)
        private var heartIcon: ImageButton = itemView.findViewById(R.id.hearticon)
        private var isHeartFilled: Boolean = false

        init {
            itemView.setOnClickListener(this)
            heartIcon.setOnClickListener {
                isHeartFilled = !isHeartFilled
                updateHeartIcon()
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }


        private fun updateHeartIcon() {
            if (isHeartFilled) {
                // Set the heart icon to filled state
                heartIcon.setImageResource(R.drawable.favorite_icon_filled)
                heartIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
            } else {
                // Set the heart icon to outlined state
                heartIcon.setImageResource(R.drawable.favorite_icon_outlined)
                heartIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
            }
        }

    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}