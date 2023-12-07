package com.example.bmart.adapters

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.helpers.SharedPreferencesHelper
import com.example.bmart.models.VendorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class VendorAdapter(
    private var vendorModelArrayList: List<VendorModel>,
    private val listener: OnItemClickListener,
    private val favoriteStates: Map<String, Boolean>
) :
    RecyclerView.Adapter<VendorAdapter.MyViewHolder?>() {

    private val fireStore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vendor_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vendors = vendorModelArrayList[position]
        holder.vendorsName.text = vendors.vendorsName
        Picasso.get().load(vendors.vendorsImage).into(holder.vendorsImage)
        holder.vendorsRating.text = vendors.vendorsRating.toString()
        holder.vendorLocation.text = vendors.vendorsLocation
        holder.bind(vendors)
    }

    override fun getItemCount(): Int {
        return vendorModelArrayList.size
    }

    fun updateData(newItems: List<VendorModel>) {
        vendorModelArrayList = newItems
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var vendorsImage: ImageView = itemView.findViewById(R.id.vendor_image)
        var vendorsName: TextView = itemView.findViewById(R.id.vendor_name)
        var vendorsRating: TextView = itemView.findViewById(R.id.vendor_rate)
        var vendorLocation: TextView = itemView.findViewById(R.id.vendor_loc)
        private var heartIcon: ImageButton = itemView.findViewById(R.id.hearticon)
        private var isHeartFilled: Boolean = false
        private val user = auth.currentUser?.uid
        private var vendor: VendorModel? = null

        init {
            itemView.setOnClickListener(this)
            heartIcon.setOnClickListener {
                isHeartFilled = !isHeartFilled
                updateHeartIcon()
            }
        }

        fun bind(vendor: VendorModel) {
            this.vendor = vendor

            // Set the initial state of the heart icon based on isFavorite or loaded favorite state
            isHeartFilled = favoriteStates[vendor.documentId] ?: false

            if (isHeartFilled) {
                heartIcon.setImageResource(R.drawable.favorite_icon_filled)
                heartIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
            }
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

        private fun addVendorToFavorites(userID: String, vendorID: String) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(userID)

            // Update the user's favoriteVendors field with the new vendorID
            userRef.update("FavoriteVendors", FieldValue.arrayUnion(vendorID))
                .addOnSuccessListener {
                    // Successfully added to favorites
                    Toast.makeText(itemView.context, "Item added to favorites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e(ContentValues.TAG, "Error adding vendor to favorites: ${e.message}", e)
                    Toast.makeText(itemView.context, "Error adding vendor to favorites:", Toast.LENGTH_SHORT).show()
                }
        }

        private fun removeVendorFromFavorites(userID: String, vendorID: String) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(userID)

            // Update the user's favoriteVendors field to remove the vendorID
            userRef.update("FavoriteVendors", FieldValue.arrayRemove(vendorID))
                .addOnSuccessListener {
                    // Successfully removed from favorites
                    Toast.makeText(itemView.context, "Vendor removed from favorites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Log.e(TAG, "Error removing vendor from favorites: ${e.message}", e)
                    Toast.makeText(itemView.context, "Error removing vendor from favorites", Toast.LENGTH_SHORT).show()
                }
        }

        private fun updateHeartIcon() {
            val position = adapterPosition

            if (position != RecyclerView.NO_POSITION) {
                val clickedVendor = vendorModelArrayList[position]
                val docId = clickedVendor.documentId

                // Load existing favorite states
                val favoriteStates = SharedPreferencesHelper.loadFavoriteStates(itemView.context).toMutableMap()

                // Toggle the favorite state for the current vendor
                isHeartFilled = !favoriteStates.getOrDefault(docId, false)

                Log.d("TAG", "I RANSS $isHeartFilled, $docId")

                // Update UI based on isHeartFilled
                if (isHeartFilled) {
                    // Set the heart icon to filled state
                    heartIcon.setImageResource(R.drawable.favorite_icon_filled)
                    heartIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
                    if (user != null) {
                        addVendorToFavorites(user, docId)
                    }
                } else {
                    // Set the heart icon to outlined state
                    heartIcon.setImageResource(R.drawable.favorite_icon_outlined)
                    heartIcon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
                    if (user != null) {
                        removeVendorFromFavorites(user, docId)
                    }
                }

                // Update the isFavorite status in the VendorModel
                clickedVendor.isFavorite = isHeartFilled

                // Save the updated favorite states
                favoriteStates[docId] = isHeartFilled
                SharedPreferencesHelper.saveFavoriteStates(itemView.context, favoriteStates)

                Log.d("TAG", "${clickedVendor.isFavorite}, $isHeartFilled")
            } else {
                Log.w(TAG, "Invalid adapterPosition in updateHeartIcon")
            }
        }



        private fun saveFavoriteStatesToSharedPreferences() {
            val favoriteStates = mutableMapOf<String, Boolean>()

            // Iterate through vendors and save their favorite states
            for (vendor in vendorModelArrayList) {
                favoriteStates[vendor.documentId] = vendor.isFavorite
            }

            // Save to SharedPreferences
            SharedPreferencesHelper.saveFavoriteStates(itemView.context, favoriteStates)
        }


    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}