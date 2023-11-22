package com.example.bmart.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Models.SearchModel
import com.example.bmart.R

class SearchAdapter(private val searches: List<SearchModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val queryTextView: TextView = itemView.findViewById(R.id.text_query)
        val timestampTextView: TextView = itemView.findViewById(R.id.text_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searches[position]
        holder.queryTextView.text = search.query
        holder.timestampTextView.text = search.timestamp.toString()
    }

    override fun getItemCount(): Int {
        return searches.size
    }

}
