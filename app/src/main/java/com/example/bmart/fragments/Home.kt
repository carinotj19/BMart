package com.example.bmart.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.MyAdapter
import com.example.bmart.Vendors
import com.example.bmart.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Home : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var vendorsArrayList: ArrayList<Vendors>
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        var myAdapter = MyAdapter(requireContext(), vendorsArrayList)
        recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()

    }

    private fun dataInitialize() {
        vendorsArrayList = arrayListOf<Vendors>()

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
            val vendors = Vendors(vendorsName[i], vendorsImage[i], vendorsRating[i], vendorsLocation[i])
            vendorsArrayList.add(vendors)
        }
    }

}