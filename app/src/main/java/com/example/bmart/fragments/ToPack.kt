package com.example.bmart.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.ToPackAdapter
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.helpers.SharedPreferencesHelper
import com.example.bmart.models.ToPackModel
import com.example.bmart.models.VendorModel
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.provider.Lorem
import kotlin.random.Random

class ToPack : Fragment(), ToPackAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var toPackModelArrayList: List<ToPackModel>
    private lateinit var toPackAdapter : ToPackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_pack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)

        // Initialize toPackModelArrayList with fake data
        toPackModelArrayList = generateFakeData()

        // Add log statements for debugging
        Log.d("ToPackFragment", "Fake data size: ${toPackModelArrayList.size}")

        if (toPackModelArrayList.isNotEmpty()) {
            toPackAdapter = ToPackAdapter(toPackModelArrayList, this)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = toPackAdapter

            // Update the adapter with fake data
            toPackAdapter.updateData(toPackModelArrayList)
        } else {
            Log.e("ToPackFragment", "Fake data is empty!")
        }
    }
    override fun onItemClick(position: Int) {
        showPopupDialog(position)
    }

    private fun generateFakeData(): List<ToPackModel> {
        val faker = Faker()

        val fakeData = mutableListOf<ToPackModel>()

        // Generate 10 fake ToPackModel items (you can change the number as needed)
        repeat(10) {
            val randomDouble = Random.nextDouble(25.0, 100.0)
            val roundedDouble = String.format("%.1f", randomDouble).toDouble()
            val toPackModel = ToPackModel(
                userImage = R.drawable.img_placeholder, // Replace with your default user image
                userName = faker.name.firstName(),
                totalItems = faker.random.nextInt(1, 20), // Generate a random total number of items
                totalPrice = roundedDouble // Generate a random total price
            )
            fakeData.add(toPackModel)
        }
        return fakeData
    }

    private fun showPopupDialog(position: Int) {
        val toPack = toPackModelArrayList[position]
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.to_pack_dialog_layout, null)

        AlertDialog.Builder(requireContext())
            .setTitle("${toPack.userName}'s Order")
            .setView(dialogView)
            .setPositiveButton("Pack Order") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}