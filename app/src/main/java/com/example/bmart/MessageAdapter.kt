package com.example.bmart

import ItemListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface MessageClickListener {
    fun onMessageClick(position: Int)
}
class MessageAdapter(var context: Context, private var messagesArrayList: ArrayList<Messages>, val listener : ItemListener) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.messages_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messages = messagesArrayList[position]
        holder.profilePicture.setImageResource(messages.profilePicture)
        holder.name.text = messages.name
        holder.recentText.text = messages.recentText
        holder.bindData(messagesArrayList.get(position), listener)
    }

    override fun getItemCount(): Int {
        return messagesArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profilePicture = itemView.findViewById<ImageView>(R.id.profile_picture)
        var name = itemView.findViewById<TextView>(R.id.name)
        var recentText = itemView.findViewById<TextView>(R.id.recent_text)
        fun bindData(itemName: Messages, listener: ItemListener) {
            name?.text = itemName.name
            name?.setOnClickListener {
                listener.onClicked(itemName.name)
            }
        }
        init {

        }
    }
}