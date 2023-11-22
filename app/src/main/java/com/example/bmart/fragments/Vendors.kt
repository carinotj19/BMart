package com.example.bmart.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Adapters.VendorAdapter
import com.example.bmart.R
import com.example.bmart.VendorDetails
import com.example.bmart.Models.VendorModel

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

        val vendorsName = arrayOf(
            getString(R.string.vendors_a),
            getString(R.string.vendors_b),
            getString(R.string.vendors_c),
            getString(R.string.vendors_d),
            getString(R.string.vendors_e),
            getString(R.string.vendors_f),
            getString(R.string.vendors_g),
            getString(R.string.vendors_h),
            getString(R.string.vendors_i),
            getString(R.string.vendors_j),
        )

        val vendorsRating = floatArrayOf(4.5f, 4.2f, 3.8f, 4.0f, 3.7f, 4.1f, 3.9f, 4.3f, 4.4f, 3.6f)

        val vendorsLocation = arrayOf(
            getString(R.string.location_a),
            getString(R.string.location_b),
            getString(R.string.location_c),
            getString(R.string.location_d),
            getString(R.string.location_e),
            getString(R.string.location_f),
            getString(R.string.location_g),
            getString(R.string.location_h),
            getString(R.string.location_i),
            getString(R.string.location_j),
        )

        val vendorsImage = intArrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h,
            R.drawable.i,
            R.drawable.j
        )

        for (i in vendorsName.indices) {
            val vendorModel = VendorModel(
                vendorsName[i],
                vendorsImage[i],
                vendorsRating[i],
                vendorsLocation[i]
            )
            vendorModelArrayList.add(vendorModel)
        }
    }
 }