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

        val itemsName = arrayOf(
            "Item A",
            "Item B",
            "Item C",
            "Item D",
            "Item E",
            "Item F",
            "Item G",
            "Item H",
            "Item I",
            "Item J",
        )

        val itemsImage = intArrayOf(
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item,
            R.drawable.item
        )

        for (i in itemsName.indices) {
            val itemModel = ItemModel(
                vendorsName[i],
                itemsName[i],
                itemsImage[i],
            )
            itemModelArrayList.add(itemModel)
        }
    }
}