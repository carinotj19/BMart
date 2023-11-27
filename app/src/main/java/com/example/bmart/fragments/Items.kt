package com.example.bmart.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.Adapters.ItemAdapter
import com.example.bmart.ItemDetails
import com.example.bmart.Models.ItemModel
import com.example.bmart.R
import io.github.serpro69.kfaker.faker

class Items : Fragment(), ItemAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemModelArrayList: ArrayList<ItemModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.setHasFixedSize(true)
        val itemAdapter = ItemAdapter(itemModelArrayList, this)
        recyclerView.adapter = itemAdapter

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
        val clickedItem = itemModelArrayList[position]
        val intent = Intent(requireContext(), ItemDetails::class.java)
        val itemName = clickedItem.itemsName
        val itemImage = clickedItem.itemsImage

        intent.putExtra("ITEMS-NAME", itemName)
        intent.putExtra("ITEMS-IMAGE", itemImage)
        startActivity(intent)
    }
    private fun dataInitialize() {
        itemModelArrayList = arrayListOf()
        val faker = faker {  }

        for (i in 0 until 20) {
            val vendorName = faker.name.name()
            val itemName = faker.food.vegetables()

            val itemModel = ItemModel(
                vendorName,
                itemName,
                R.drawable.item
            )
            itemModelArrayList.add(itemModel)
        }
    }
}