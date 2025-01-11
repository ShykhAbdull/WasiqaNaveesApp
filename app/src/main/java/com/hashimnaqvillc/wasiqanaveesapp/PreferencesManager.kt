package com.hashimnaqvillc.wasiqanaveesapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferencesManager {

    private const val PREFS_NAME = "AppPreferences"

    // Keys for SharedPreferences
    private const val DROPDOWN_LIST_KEY = "DropdownList"
    private const val DISTRICT_TOWN_MAP_KEY = "DistrictTownMap"
    private const val EDIT_TEXT_KEY = "EditTextValue"
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    // Initialize the SharedPreferences in the Application class
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    // Save and Get district dropdown list
    fun saveDropdownList(dropdownList: MutableList<String>) {
        val json = gson.toJson(dropdownList)
        sharedPreferences.edit().putString(DROPDOWN_LIST_KEY, json).apply()
    }
    fun getDropdownList(): MutableList<String> {
        val json = sharedPreferences.getString(DROPDOWN_LIST_KEY, "[]")
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList<String>().toMutableList()
    }



    // Saving the MutableMap with MutableList
    fun saveDistrictTownMap(districtTownMap: MutableMap<String, MutableList<String>>) {
        val json = gson.toJson(districtTownMap) // Serialize the map to JSON
        sharedPreferences.edit().putString("DistrictTownMap", json).apply()
    }

    // Getting the MutableMap with MutableList
    fun getDistrictTownMap(): MutableMap<String, MutableList<String>> {
        val json = sharedPreferences.getString("DistrictTownMap", null)
        val type = object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type // Match the type
        return gson.fromJson(json, type) ?: mutableMapOf() // Deserialize or return an empty map
    }





// Town to Property Area (SAVE&GET)
    fun saveTownAreaMap(townAreaMap: Map<String, MutableList<String>>) {
        val json = gson.toJson(townAreaMap) // Serialize the map to JSON
        sharedPreferences.edit().putString("TownAreaMap", json).apply()
    }
    fun getTownAreaMap(): MutableMap<String, MutableList<String>> {
        val json = sharedPreferences.getString("TownAreaMap", null)
        val type = object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type
        return gson.fromJson(json, type) ?: mutableMapOf() // Deserialize or return an empty map
    }





    // Save EditText value
    fun saveEditTextValue(value: String) {
        sharedPreferences.edit().putString(EDIT_TEXT_KEY, value).apply()
    }

    // Get EditText value
    fun getEditTextValue(): String {
        return sharedPreferences.getString(EDIT_TEXT_KEY, "") ?: ""
    }
}