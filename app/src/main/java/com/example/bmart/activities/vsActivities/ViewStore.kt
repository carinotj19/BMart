package com.example.bmart.activities.vsActivities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.activities.MyStore
import com.example.bmart.adapters.OtherOrderAdapter
import com.example.bmart.adapters.SimpleItemAdapter
import com.example.bmart.models.OtherOrderModel
import com.example.bmart.models.SimpleItemModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import io.github.serpro69.kfaker.Faker
import java.util.UUID
import kotlin.random.Random

class ViewStore : AppCompatActivity() {

    private lateinit var imageViewVendor: ImageView
    private lateinit var imageViewVendorProfile: ImageView
    private lateinit var errorText: TextView
    private lateinit var vendorName: TextView

    private lateinit var simpleItemAdapter: SimpleItemAdapter
    private lateinit var simpleItemModelArrayList: List<SimpleItemModel>
    private lateinit var recyclerView: RecyclerView

    private lateinit var newName: String
    private lateinit var newImageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_store)

        val backBtn = findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            val intent = Intent(this, MyStore::class.java)
            startActivity(intent)
            finish()
        }

        val addBtn = findViewById<ImageButton>(R.id.add_btn)
        addBtn.setOnClickListener{
            showEditDialog()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        simpleItemAdapter = SimpleItemAdapter(emptyList())
        recyclerView.adapter = simpleItemAdapter

        simpleItemModelArrayList = generateFakeData()

        // Add log statements for debugging
        Log.d("ToPackFragment", "Fake data size: ${simpleItemModelArrayList.size}")

        if (simpleItemModelArrayList.isNotEmpty()) {
            simpleItemAdapter = SimpleItemAdapter(simpleItemModelArrayList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = simpleItemAdapter

            // Update the adapter with fake data
            simpleItemAdapter.updateData(simpleItemModelArrayList)
        } else {
            Log.e("ToPackFragment", "Fake data is empty!")
        }
    }
    private fun generateFakeData(): List<SimpleItemModel> {
        val faker = Faker()
        val fakeData = mutableListOf<SimpleItemModel>()
        // Generate 10 fake ToPackModel items (you can change the number as needed)
        repeat(10) {
            val randomDouble = Random.nextDouble(25.0, 100.0)
            val roundedDouble = String.format("%.1f", randomDouble).toDouble()
            val simpleItemModel = SimpleItemModel(
                itemsName = faker.food.vegetables(),
                itemsImage = R.drawable.item, // Replace with your default user image
                itemsQuantity = faker.random.nextInt(1, 20), // Generate a random total number of items
                itemsPrice = roundedDouble // Generate a random total price
            )
            fakeData.add(simpleItemModel)
        }
        return fakeData
    }
    private fun showEditDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_edit_stall, null)

        vendorName = findViewById(R.id.vendor_name)
        imageViewVendor = dialogView.findViewById(R.id.imageViewStall)
        imageViewVendorProfile = findViewById(R.id.vendor_profile)
        val btnPickImage: ImageButton = dialogView.findViewById(R.id.btnPickImage)
        val editTextName: TextInputEditText = dialogView.findViewById(R.id.editTextName)
        errorText = dialogView.findViewById(R.id.error_text)

        // Set a click listener for picking a new image
        btnPickImage.setOnClickListener {
            // Handle image picking logic (e.g., show an image picker dialog)
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Stall")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                // Save the changes to Firestore and update UI
                // Set the values for onActivityResult
                newName = editTextName.text.toString()
                // Check if a new image is picked
                if (newImageUrl.isNotEmpty() && newName.isNotEmpty()) {
                    Picasso.get().load(newImageUrl).into(imageViewVendorProfile)
                    vendorName.text = newName
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                // Generate a unique filename for the image
                val filename = UUID.randomUUID().toString()

                // Get a reference to the Firebase Storage location
                val storageRef: StorageReference = FirebaseStorage.getInstance().getReference("/images/$filename")

                // Upload the image to Firebase Storage
                storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener {
                        // If the upload is successful, get the download URL
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Load the selected image into the ImageView using Picasso
                            Picasso.get().load(uri).into(imageViewVendor)
                            newImageUrl = uri.toString()
                        }
                    }
                    .addOnFailureListener {
                        // Handle failure if needed
                        Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                        errorText.visibility = View.VISIBLE
                        errorText.text = "Error uploading image"
                    }

                errorText.visibility = View.GONE
                errorText.text = ""
            }
        }
    }
    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1000
    }
}