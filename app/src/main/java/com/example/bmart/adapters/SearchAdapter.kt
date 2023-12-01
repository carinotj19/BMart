package com.example.bmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.models.SearchModel
import com.example.bmart.R
import com.example.bmart.helpers.SharedPreferencesHelper

class SearchAdapter(private val searches: MutableList<SearchModel>, private val onRemoveListener: OnRemoveListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val queryTextView: TextView = itemView.findViewById(R.id.text_query)
        val removeButton: ImageView = itemView.findViewById(R.id.remove_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_search_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searches[position]
        holder.queryTextView.text = search.query
        holder.removeButton.setOnClickListener {
            // Use adapterPosition to get the correct position
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                // Remove the item from the list
                searches.removeAt(adapterPosition)

                // Update the RecyclerView via the adapter
                notifyItemRemoved(adapterPosition)

                // Notify the fragment about the removal
                onRemoveListener.onRemoveItem(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    interface OnRemoveListener {
        fun onRemoveItem(position: Int)
    }
}
