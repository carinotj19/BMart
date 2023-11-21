package com.example.bmart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private var messagesArrayList: ArrayList<Messages>,
    private val listener: onItemClickListener
    ) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.messages_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messages = messagesArrayList[position]
        holder.profilePicture.setImageResource(messages.profilePicture)
        holder.name.text = messages.name
        holder.recentText.text = messages.recentText
    }

    override fun getItemCount(): Int {
        return messagesArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        var profilePicture: ImageView = itemView.findViewById<ImageView>(R.id.profile_picture)
        var name: TextView = itemView.findViewById<TextView>(R.id.name)
        var recentText: TextView = itemView.findViewById<TextView>(R.id.recent_text)

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

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

}
