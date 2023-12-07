package com.example.bmart.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.models.ConversationModel

class ConversationAdapter(private val conversations: MutableList<ConversationModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View types to distinguish between user and bot messages
    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_BOT = 2
    // ViewHolder for user messages
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextViewUser)
    }

    // ViewHolder for bot messages
    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextViewBot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_user, parent, false)
                UserViewHolder(itemView)
            }
            VIEW_TYPE_BOT -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_bot, parent, false)
                BotViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Return the appropriate view type based on the sender
        return when (conversations[position].sender) {
            "You" -> VIEW_TYPE_USER
            "Bot" -> VIEW_TYPE_BOT
            else -> throw IllegalArgumentException("Invalid sender")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                val message = conversations[position]
                holder.messageTextView.text = message.message
            }
            is BotViewHolder -> {
                val message = conversations[position]
                holder.messageTextView.text = message.message
            }
        }
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    fun addMessage(message: ConversationModel) {
        // Add these lines to your addMessage method in the ConversationAdapter
        Log.d("ConversationAdapter", "Adding message: ${message.sender} - ${message.message}")
        conversations.add(message)
        notifyItemInserted(conversations.size - 1)
    }
}
