package com.example.bmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.models.ToPackModel
import de.hdodenhof.circleimageview.CircleImageView

class ToPackAdapter(
    private var toPackModelArrayList: List<ToPackModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ToPackAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.to_pack_item_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val toPack = toPackModelArrayList[position]
         holder.userImage.setImageResource(toPack.userImage)
         holder.userName.text = toPack.userName
         holder.totalItems.text = "${toPack.totalItems} Item/s"
         holder.totalPrice.text = "Total: ₱${toPack.totalPrice}"
    }

    override fun getItemCount(): Int {
        return toPackModelArrayList.size
    }

    // Update the adapter data when needed
    fun updateData(newItems: List<ToPackModel>) {
        toPackModelArrayList = newItems
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var userImage: CircleImageView = itemView.findViewById(R.id.user_image)
        var userName: TextView = itemView.findViewById(R.id.user_name)
        var totalItems: TextView = itemView.findViewById(R.id.total_items)
        var totalPrice: TextView = itemView.findViewById(R.id.total_price)

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

    // Interface to handle item clicks
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
