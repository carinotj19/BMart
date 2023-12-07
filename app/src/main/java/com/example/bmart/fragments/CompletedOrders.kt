package com.example.bmart.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.R
import com.example.bmart.adapters.OtherOrderAdapter
import com.example.bmart.models.OtherOrderModel
import io.github.serpro69.kfaker.Faker
import kotlin.random.Random

class CompletedOrders : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var otherOrderModelArrayList: List<OtherOrderModel>
    private lateinit var otherOrderAdapter : OtherOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        // Initialize toPackModelArrayList with fake data
        otherOrderModelArrayList = generateFakeData()

        // Add log statements for debugging
        Log.d("ToPackFragment", "Fake data size: ${otherOrderModelArrayList.size}")

        if (otherOrderModelArrayList.isNotEmpty()) {
            otherOrderAdapter = OtherOrderAdapter(otherOrderModelArrayList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = otherOrderAdapter

            // Update the adapter with fake data
            otherOrderAdapter.updateData(otherOrderModelArrayList)
        } else {
            Log.e("ToPackFragment", "Fake data is empty!")
        }
    }

    private fun generateFakeData(): List<OtherOrderModel> {
        val faker = Faker()
        val fakeData = mutableListOf<OtherOrderModel>()
        // Generate 10 fake ToPackModel items (you can change the number as needed)
        repeat(10) {
            val randomDouble = Random.nextDouble(25.0, 100.0)
            val roundedDouble = String.format("%.1f", randomDouble).toDouble()
            val otherOrderModel = OtherOrderModel(
                userImage = R.drawable.img_placeholder, // Replace with your default user image
                userName = faker.name.firstName(),
                totalItems = faker.random.nextInt(1, 20), // Generate a random total number of items
                totalPrice = roundedDouble // Generate a random total price
            )
            fakeData.add(otherOrderModel)
        }
        return fakeData
    }
}