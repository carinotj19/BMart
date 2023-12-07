package com.example.bmart.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmart.adapters.SearchAdapter
import com.example.bmart.models.SearchModel
import com.example.bmart.R
import com.example.bmart.activities.VendorDetails
import com.example.bmart.adapters.VendorAdapter
import com.example.bmart.helpers.SharedPreferencesHelper
import com.example.bmart.models.VendorModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Search : Fragment(), SearchAdapter.OnRemoveListener, VendorAdapter.OnItemClickListener {

    private lateinit var searchContainer: FrameLayout
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar

    private lateinit var recentSearchesLayout: ViewGroup

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val recentSearches = mutableListOf<SearchModel>()
    private lateinit var searchAdapter: SearchAdapter

    private lateinit var vendorAdapter: VendorAdapter
    private lateinit var vendorList: List<VendorModel>

    private lateinit var favoriteStates: Map<String, Boolean>

    private var lastQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        favoriteStates = SharedPreferencesHelper.loadFavoriteStates(requireContext())

        recentSearchesLayout = view.findViewById(R.id.recent_searches_layout)
        searchContainer = view.findViewById(R.id.search_fragment_container)
        resultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)
        searchView = view.findViewById(R.id.searchView)
        progressBar = view.findViewById(R.id.progress_bar)

        loadRecentSearches()
        // Initialize RecyclerView and Adapter for recent searches
        resultsRecyclerView.layoutManager = LinearLayoutManager(context)
        searchAdapter = SearchAdapter(recentSearches, this)
        resultsRecyclerView.adapter = searchAdapter

        resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vendorAdapter = VendorAdapter(emptyList(), this, favoriteStates) // Initialize adapter with empty list
        resultsRecyclerView.adapter = vendorAdapter

        // Display recent searches by default
        switchToRecentSearches()

        // SearchView listener to switch to search results
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                if (!query.isNullOrBlank() && !query.equals(lastQuery, ignoreCase = true)) {
                    // Add the search query to recent searches
                    addRecentSearch(query)
                    lastQuery = query
                }
                // Implement logic to show search results as needed
                switchToSearchResults(query)

                progressBar.visibility = View.GONE
                searchContainer.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                progressBar.visibility = View.VISIBLE
                searchContainer.visibility = View.GONE
                vendorAdapter.updateData(emptyList())
                resultsRecyclerView.visibility = View.GONE
                return true
            }
        })
        progressBar.visibility = View.GONE
        searchContainer.visibility = View.VISIBLE
        return view
    }

    private fun switchToRecentSearches() {
        recentSearchesLayout.visibility = View.VISIBLE
        resultsRecyclerView.visibility = View.GONE
        val noResultsTextView = view?.findViewById<TextView>(R.id.no_results_text_view)
        noResultsTextView?.visibility = View.GONE
    }

    private fun switchToSearchResults(query: String?) {
        vendorAdapter.updateData(emptyList())
        if (!query.isNullOrBlank()) {
            performFirestoreQuery(query)
        } else {
            recentSearchesLayout.visibility = View.VISIBLE
            resultsRecyclerView.visibility = View.GONE
            val noResultsTextView = view?.findViewById<TextView>(R.id.no_results_text_view)
            noResultsTextView?.visibility = View.GONE
        }
    }
    private fun performFirestoreQuery(query: String) {
        val vendorsCollection = db.collection("Vendors")

        // Query for properties where any substring of length 3 or more is in the 'propertyName' field
        val queryTask: Task<QuerySnapshot> =
            vendorsCollection.orderBy("vendorsName")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()

        queryTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle the query results
                val queryResults = task.result?.documents

                Log.d("Firebase Query", "Results: $queryResults")
                if (!queryResults.isNullOrEmpty()) {
                    vendorList = queryResults.map { document ->
                        VendorModel(
                            document.getString("vendorsName")?: "",
                            document.getString("vendorsImage") ?: "",
                            document.getDouble("vendorsRating") ?: 0.0,
                            document.getString("vendorsLocation") ?: "",
                            document.id,
                        )
                    }
                    // Update the RecyclerView with the search results
                    vendorAdapter.updateData(vendorList)

                    // Show searchResultsLayout and hide recentSearchesLayout
                    resultsRecyclerView.visibility = View.VISIBLE
                    recentSearchesLayout.visibility = View.GONE
                } else {
                    // If no results found, show a message outside RecyclerView
                    resultsRecyclerView.visibility = View.GONE
                    recentSearchesLayout.visibility = View.GONE
                    val noResultsTextView = view?.findViewById<TextView>(R.id.no_results_text_view)
                    noResultsTextView?.visibility = View.VISIBLE
                    noResultsTextView?.text = "No results found."
                    noResultsTextView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    noResultsTextView?.setPadding(16, 16, 16, 16)
                }
            } else {
                // Handle the error
                val exception = task.exception
                // Log or show an error message
                Log.e("Firebase Query", "Error: $exception")
            }
        }
    }


    private fun loadRecentSearches() {
        // Load recent searches from SharedPreferences using SharedPreferencesHelper
        recentSearches.clear()
        recentSearches.addAll(SharedPreferencesHelper.loadRecentSearches(requireContext()))
    }

    private fun saveRecentSearches() {
        // Save recent searches to SharedPreferences using SharedPreferencesHelper
        SharedPreferencesHelper.saveRecentSearches(requireContext(), recentSearches)
    }

    private fun addRecentSearch(query: String) {
        // Add a new search item to the list
        val searchItem = SearchModel(query)
        recentSearches.add(0, searchItem) // Add to the beginning of the list

        // Limit the list size if needed
        if (recentSearches.size > MAX_RECENT_SEARCHES) {
            recentSearches.removeAt(recentSearches.size - 1)
        }

        // Update the RecyclerView via the adapter
        searchAdapter.notifyDataSetChanged()
        Toast.makeText(context, "Recent Searches Updated", Toast.LENGTH_SHORT).show()

        // Save recent searches to SharedPreferences
        saveRecentSearches()
    }

    override fun onRemoveItem(position: Int) {
        // Save recent searches to SharedPreferences
        saveRecentSearches()
    }

    override fun onItemClick(position: Int) {
        val clickedItem = vendorList[position]
        val intent = Intent(requireContext(), VendorDetails::class.java)
        val vendorName = clickedItem.vendorsName
        val vendorRating = clickedItem.vendorsRating
        val vendorImage = clickedItem.vendorsImage
        val documentId = clickedItem.documentId
        Log.d("docID", "$documentId")
        intent.putExtra("VENDORS_NAME", vendorName)
        intent.putExtra("VENDORS_RATING", vendorRating)
        intent.putExtra("VENDOR_PROFILE", vendorImage)
        intent.putExtra("VENDOR_DOCUMENT_ID", clickedItem.documentId)
        startActivity(intent)
    }

    companion object {
        private const val MAX_RECENT_SEARCHES = 5 // Adjust the maximum number of recent searches as needed
    }
}
