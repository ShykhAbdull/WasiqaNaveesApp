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


//                                                     KEY ,  VALUE
    fun saveDistrictTownMap(districtTownMap: MutableMap<Int, MutableList<String>>) {
        val json = gson.toJson(districtTownMap) // Serialize the map to JSON
        sharedPreferences.edit().putString("DistrictTownMap", json).apply()
    }
//                                      KEY ,  VALUE
    fun getDistrictTownMap(): MutableMap<Int, MutableList<String>> {
        val json = sharedPreferences.getString("DistrictTownMap", null)
        val type = object : TypeToken<MutableMap<Int, MutableList<String>>>() {}.type // Updated type to use Int keys
        return gson.fromJson(json, type) ?: mutableMapOf() // Deserialize or return an empty map
    }




// Town to Property Area (SAVE&GET)
    fun saveTownAreaMap(townAreaMap: MutableMap<Int, MutableList<String>>) {
        val json = gson.toJson(townAreaMap) // Serialize the map to JSON
        sharedPreferences.edit().putString("TownAreaMap", json).apply()
    }

    fun getTownAreaMap(): MutableMap<Int, MutableList<String>> {
        val json = sharedPreferences.getString("TownAreaMap", null)
        val type = object : TypeToken<MutableMap<Int, MutableList<String>>>() {}.type
        return gson.fromJson(json, type) ?: mutableMapOf() // Deserialize or return an empty map
    }





    // Property Area to Land Type (SAVE&GET)
    fun saveAreaLandMap(areaLandMap: MutableMap<Int, MutableList<String>>) {
        val json = gson.toJson(areaLandMap) // Serialize the map to JSON
        sharedPreferences.edit().putString("AreaLandMap", json).apply()
    }
    fun getAreaLandMap(): MutableMap<Int, MutableList<String>> {
        val json = sharedPreferences.getString("AreaLandMap", null)
        val type = object : TypeToken<MutableMap<Int, MutableList<String>>>() {}.type
        return gson.fromJson(json, type) ?: mutableMapOf() // Deserialize or return an empty map
    }





    // Save land rates with updated hierarchical structure
    fun saveLandRates(areaLandRates: MutableMap<String, MutableMap<String, MutableMap<String, MutableMap<String, Map<String, String>>>>>) {
        val json = gson.toJson(areaLandRates) // Serialize the entire nested structure
        sharedPreferences.edit().putString("AreaLandRates", json).apply()
    }

    // Retrieve land rates with updated hierarchical structure
    fun getLandRates(): MutableMap<String, MutableMap<String, MutableMap<String, MutableMap<String, Map<String, String>>>>> {
        val json = sharedPreferences.getString("AreaLandRates", "{}") // Default to an empty JSON object
        val type = object : TypeToken<MutableMap<String, MutableMap<String, MutableMap<String, MutableMap<String, Map<String, String>>>>>>() {}.type
        return gson.fromJson(json, type) ?: mutableMapOf() // Deserialize or return an empty map if null
    }








//    fun saveLandList(landList: MutableList<String>) {
//        val json = gson.toJson(landList)
//        sharedPreferences.edit().putString("LandList", json).apply()
//    }
//
//    fun getLandList(): MutableList<String> {
//        val json = sharedPreferences.getString("LandList", "[]")
//        val type = object : TypeToken<List<String>>() {}.type
//        return gson.fromJson(json, type) ?: emptyList<String>().toMutableList()
//    }



//    // Save EditText value
//    fun saveEditTextValue(value: String) {
//        sharedPreferences.edit().putString(EDIT_TEXT_KEY, value).apply()
//    }
//
//    // Get EditText value
//    fun getEditTextValue(): String {
//        return sharedPreferences.getString(EDIT_TEXT_KEY, "") ?: ""
//    }
}