package com.example.bmart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.models.VendorModel
import com.example.bmart.R
import com.example.bmart.VendorDetails
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random
import io.github.serpro69.kfaker.faker


class Vendors : Fragment(), VendorAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorModelArrayList: ArrayList<VendorModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendors, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        val vendorAdapter = VendorAdapter(vendorModelArrayList, this)
        recyclerView.adapter = vendorAdapter

        val spinner = view.findViewById<Spinner>(R.id.spinnerCategory)
        val categories = arrayOf("Category 1", "Category 2", "Category 3", "Category 4")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                Toast.makeText(requireContext(), "Selected Category: $selectedCategory", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing was selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onItemClick(position: Int) {
        val clickedItem = vendorModelArrayList[position]
        val intent = Intent(requireContext(), VendorDetails::class.java)
        val vendorName = clickedItem.vendorsName
        val vendorRating = clickedItem.vendorsRating
        val vendorImage = clickedItem.vendorsImage
        Log.d("DEBUGGERS", "${vendorRating::class.simpleName}")
        intent.putExtra("VENDORS-NAME", vendorName)
        intent.putExtra("VENDORS-RATING", vendorRating)
        intent.putExtra("VENDOR-PROFILE", vendorImage)
        startActivity(intent)
    }
    private fun dataInitialize() {
        vendorModelArrayList = arrayListOf()
        val faker = faker {  }

        for (i in 0 until 20) {
            val vendorName = faker.name.name()
            val vendorRating = Random.nextFloat() * (5.0 - 1.0) + 1.0
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            val vendorLocation = faker.address.country()

            val vendorModel = VendorModel(
                vendorName,
                R.drawable.a,
                df.format(vendorRating).toFloat(),
                vendorLocation,
            )
            vendorModelArrayList.add(vendorModel)
        }
    }
}