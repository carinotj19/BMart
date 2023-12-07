package com.example.bmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.models.OrderHistoryDate
import com.example.bmart.models.OrderItem

class OrderHistoryAdapter(
    private val orderHistory: List<Any> // List of OrderHistoryDate or OrderItem
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.DATE_HEADER.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_header, parent, false)
                DateHeaderViewHolder(view)
            }
            ViewType.ORDER_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_order, parent, false)
                OrderItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ViewType.DATE_HEADER.ordinal -> {
                val dateHeaderHolder = holder as DateHeaderViewHolder
                val date = (orderHistory[position] as OrderHistoryDate).date
                dateHeaderHolder.bind(date)
            }
            ViewType.ORDER_ITEM.ordinal -> {
                val orderItemHolder = holder as OrderItemViewHolder
                val orderItem = orderHistory[position] as OrderItem
                orderItemHolder.bind(orderItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return orderHistory.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (orderHistory[position]) {
            is OrderHistoryDate -> ViewType.DATE_HEADER.ordinal
            is OrderItem -> ViewType.ORDER_ITEM.ordinal
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    // ViewHolder for date headers
    class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(date: String) {
            dateTextView.text = date
        }
    }

    // ViewHolder for order items
    class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.item_name)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.item_price)
        private val itemImageTextView: ImageView = itemView.findViewById(R.id.item_image)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.item_quantity)

        fun bind(orderItem: OrderItem) {
            val formattedItemPrice = "Price: ₱${String.format("%.2f", orderItem.price)}"
            val formattedQuantity = "Quantity: ${orderItem.quantity}"

            itemNameTextView.text = orderItem.itemName
            itemPriceTextView.text = formattedItemPrice
            itemImageTextView.setImageResource(orderItem.itemImage)
            itemQuantityTextView.text = formattedQuantity
        }
    }
}

enum class ViewType {
    DATE_HEADER,
    ORDER_ITEM
}
