package com.example.bmart.helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.bmart.models.SearchModel

object SharedPreferencesHelper {

    private const val PREF_NAME = "MyAppPreferences"
    private const val KEY_RECENT_SEARCHES = "recentSearches"
    private const val KEY_FAVORITE_STATES = "favoriteStates"

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

    fun saveFavoriteStates(context: Context, favoriteStates: Map<String, Boolean>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favoriteStates)
        editor.putString(KEY_FAVORITE_STATES, json)
        editor.apply()
    }

    fun loadFavoriteStates(context: Context): Map<String, Boolean> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_FAVORITE_STATES, null)
        val type = object : TypeToken<Map<String, Boolean>>() {}.type
        return Gson().fromJson(json, type) ?: emptyMap()
    }
}
