package com.example.bmart.helpers

import android.content.Context
import com.example.bmart.models.CartItemModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.bmart.models.SearchModel

object SharedPreferencesHelper {

    private const val PREF_NAME = "MyAppPreferences"
    private const val KEY_RECENT_SEARCHES = "recentSearches"
    private const val KEY_CART_ITEMS = "cartItems"

    fun saveRecentSearches(context: Context, recentSearches: List<SearchModel>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(recentSearches)
        editor.putString(KEY_RECENT_SEARCHES, json)
        editor.apply()
    }

    fun loadRecentSearches(context: Context): List<SearchModel> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_RECENT_SEARCHES, null)
        val type = object : TypeToken<List<SearchModel>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun saveCartItems(context: Context, cartItems: List<CartItemModel>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(cartItems)
        editor.putString(KEY_CART_ITEMS, json)
        editor.apply()
    }

    fun loadCartItems(context: Context): List<CartItemModel> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_CART_ITEMS, null)
        val type = object : TypeToken<List<CartItemModel>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }
}
