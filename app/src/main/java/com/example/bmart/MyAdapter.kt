package com.example.bmart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(var context: Context, private var newsArrayList: ArrayList<News>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.itemlist, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = newsArrayList[position]
        holder.heading.text = news.heading
        holder.title.setImageResource(news.titleImage)
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: ShapeableImageView
        var heading: TextView

        init {
            title = itemView.findViewById(R.id.title_image)
            heading = itemView.findViewById(R.id.heading)
        }
    }
}