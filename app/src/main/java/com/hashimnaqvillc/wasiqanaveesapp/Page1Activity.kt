package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.PreferencesManager.getDropdownList
import com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage1Binding

class Page1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPage1Binding
    private var selectedDistrict: String? = null
    private var selectedTown: String? = null
    private var selectedPropertyArea: String? = null
    private var selectedLandType: String? = null
    private var selectedPropertyType: String? = null

//    Lists
    private lateinit var districtList: MutableList<String>
    private lateinit var townList: MutableList<String>
    private lateinit var areaList: MutableList<String>
    private lateinit var landList: MutableList<String>


    // Variables for dynamically updated data


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)


        binding = ActivityPage1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingButton = findViewById<ImageButton>(R.id.nav_settings_icon)
        // Launch SettingsActivity
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<ImageButton>(R.id.nav_back)
        backButton.visibility = View.GONE

        val dateMonth = findViewById<TextView>(R.id.date_month_day)
        dateMonth.visibility = View.GONE
//        val dateYear = findViewById<TextView>(R.id.date_year)
//        dateYear.visibility = View.GONE


        setupDropdowns()


// Handle District selection
        binding.districtDropdown.setOnClickListener {
            binding.districtDropdown.showDropDown()
            binding.districtDropdown.requestFocus()
        }

        binding.districtDropdown.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.districtDropdown.showDropDown()
            }else{
                binding.districtDropdown.clearFocus()
            }
        }



// Handle Town selection
        binding.townDropdown.setOnClickListener {
            binding.townDropdown.showDropDown()
            binding.townDropdown.requestFocus()
        }

        binding.townDropdown.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                binding.townDropdown.showDropDown()
            }else{
                binding.townDropdown.clearFocus()
            }
        }


// Handle Property Area selection
        binding.propertyAreaDropdown.setOnClickListener {
            binding.propertyAreaDropdown.showDropDown()
            binding.propertyAreaDropdown.requestFocus()
        }

        binding.propertyAreaDropdown.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                binding.propertyAreaDropdown.showDropDown()
            }else{
                binding.propertyAreaDropdown.clearFocus()
            }
        }

        val landTypeAutoComplete = binding.landTypeDropdown

        landTypeAutoComplete.setOnClickListener {
            landTypeAutoComplete.showDropDown()
            landTypeAutoComplete.requestFocus()
        }
        landTypeAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                landTypeAutoComplete.showDropDown()
            } else {
                landTypeAutoComplete.clearFocus()
            }
        }
//// Data for the dropdowns
//        val landTypeOptions = PreferencesManager.getLandList()
//        val landTypeAdapter = ArrayAdapter(this, custom_dropdown_item,
//            landTypeOptions)
//        landTypeAutoComplete.setAdapter(landTypeAdapter)




        val propertyTypeAutoComplete = binding.propertyTypeDropdown
        val propertyTypeOptions = mutableListOf("Plot", "Building")
        val propertyTypeAdapter = ArrayAdapter(this, custom_dropdown_item,
            propertyTypeOptions)
        propertyTypeAutoComplete.setAdapter(propertyTypeAdapter)





//
//// Handle selection actions for "Land Type"
//        landTypeAutoComplete.setOnItemClickListener { _, _, _, _ ->
////            selectedLandType = landTypeOptions[position]
//            // Take actions based on the selected land type
//            when (selectedLandType) {
//                "Residential" -> {
//                }
//                "Commercial" -> {
//                    // Action for Commercial
//                }
//                "Agricultural" -> {
////                    Action to perform for Agricultural
//                }
//                "Industrial" -> {
////                    Action to perform for Industrial
//                }
//                "Apartment" -> {
////                    Action to perform for Industrial
//                }
//                "Shop" -> {
////                    Action to perform for Industrial
//                }
//            }
//            propertyTypeAutoComplete.requestFocus()
//            propertyTypeAutoComplete.showDropDown()
//        }


        binding.propertyTypeDropdown.setOnClickListener {
            propertyTypeAutoComplete.showDropDown()
        }
        val coveredAreaEditText = binding.coveredAreaEditText


// Handle selection actions for "Property Type"
        propertyTypeAutoComplete.setOnItemClickListener { _, _, position, _ ->
            selectedPropertyType = propertyTypeOptions[position]




            // Take actions based on the selected property type
            when (selectedPropertyType) {
                "Plot" -> {
                    binding.coveredAreaText.visibility = View.GONE
                    coveredAreaEditText.visibility = View.GONE
                    binding.marlaEditText.requestFocus()

                }
                "Building" -> {
                    binding.coveredAreaText.visibility = View.VISIBLE
                    coveredAreaEditText.visibility = View.VISIBLE
                    binding.marlaEditText.requestFocus()

                }
            }
        }


        binding.findButton.setOnClickListener {

            val selectedDistrict = binding.districtDropdown.text.toString().trim()
            val selectedTown = binding.townDropdown.text.toString().trim()
            val selectedPropertyArea = binding.propertyAreaDropdown.text.toString().trim()
            val selectedLandType = binding.landTypeDropdown.text.toString().trim()

            // Validate dropdown selections
            if (selectedDistrict.isEmpty() || selectedTown.isEmpty() || selectedPropertyArea.isEmpty() || selectedLandType.isEmpty()) {
                Toast.makeText(this, "Please select all required fields (District, Town, Area, Land Type).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedPropertyType == "Building" && binding.coveredAreaEditText.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter values for buildings.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedPropertyType == "Plot" &&
                binding.kanalEditText.text.toString().isEmpty() &&
                binding.marlaEditText.text.toString().isEmpty() &&
                binding.sqftEditText.text.toString().isEmpty()) {

                Toast.makeText(this, "Please enter values for plots.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            // Generate the key to fetch land values
            val districtHashKey = selectedDistrict.hashCode()
            val townHashKey = "$districtHashKey-$selectedTown".hashCode()
            val areaHashKey = "$townHashKey-$selectedPropertyArea".hashCode()
            val finalKey = "$areaHashKey-$selectedLandType".hashCode()

            // Fetch land values using the compound key
            val landValues = PreferencesManager.getLandOptionRates()[finalKey]
            Log.d("LandValues", "Fetched land values: $landValues")

            if (landValues == null) {
                Toast.makeText(this, "No land rates found for the selected area and land type.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve numeric values safely
            val kanalValue = binding.kanalEditText.text.toString().toIntOrNull() ?: 0
            val marlaValue = binding.marlaEditText.text.toString().toFloatOrNull() ?: 0f
            val sqftValue = binding.sqftEditText.text.toString().toIntOrNull() ?: 0
            val coveredAreaValue = coveredAreaEditText.text.toString().toIntOrNull() ?: 0
            val sqftPerMarla = 225


            // Safely retrieve individual values with defaults
            val marlaDc = landValues.marlaDC.toIntOrNull() ?: 0
            val marlaFbr = landValues.marlaFBR.toIntOrNull() ?: 0
            val coveredAreaDc = landValues.coveredAreaDC.toIntOrNull() ?: 0
            val coveredAreaFbr = landValues.coveredAreaFBR.toIntOrNull() ?: 0
            val khasraNumber = landValues.khasraNumber


            // Calculate total square feet and plot value
            val totalSqft = kanalValue * 20 * sqftPerMarla + marlaValue.toInt() * sqftPerMarla + sqftValue

//            Used when Plot is selected in Property Type
            val plotValueDC = totalSqft / sqftPerMarla * marlaDc
            val plotValueFbr = totalSqft / sqftPerMarla * marlaFbr

// Now multiply with the integer value
            val buildingValueDC = coveredAreaDc * coveredAreaValue
            val buildingValueFbr = coveredAreaFbr * coveredAreaValue

//            Log.d("buildingValue", "buildingValueDC: $coveredAreaDc, buildingValueFbr: $coveredAreaFbr, coveredAreaValue: $coveredAreaValue")
//
//
//            Log.d("Calculation", "Total Sqft: $totalSqft, Plot Value DC: $plotValueDC")
//
//
//            // Log additional details
//            Log.d("LandValues", "MarlaDC: $marlaDc, Covered Area DC: $coveredAreaDc, MarlaFbr: $marlaFbr, Covered Area Fbr: $coveredAreaFbr, Khasra Number: $khasraNumber ")
//            Log.d("Calculation", "Total Sqft: $totalSqft, Covered Area Value: $coveredAreaValue")
//
//            Toast.makeText(
//                this,
//                "Total Sqft: $totalSqft\nPlot Value DC: $plotValueDC\nCovered Area Value: $coveredAreaValue",
//                Toast.LENGTH_LONG
//            ).show()



            // Proceed to the next activity with validated data
            val intent = Intent(this, Page2Activity::class.java).apply {
                putExtra("selectedDistrict", selectedDistrict)
                putExtra("selectedTown", selectedTown)
                putExtra("selectedPropertyArea", selectedPropertyArea)
                putExtra("selectedLandType", selectedLandType)
                putExtra("selectedPropertyType", selectedPropertyType)
                putExtra("khasraNumber", khasraNumber)

                putExtra("marlaDc", marlaDc)
                putExtra("plotValueDC", plotValueDC)

                putExtra("marlaFbr", marlaFbr)
                putExtra("plotValueFbr", plotValueFbr)

                putExtra("coveredAreaDc", coveredAreaDc)
                putExtra("buildingValueDC", buildingValueDC)

                putExtra("coveredAreaFbr", coveredAreaFbr)
                putExtra("buildingValueFbr", buildingValueFbr)

                putExtra("kanalValue", kanalValue)
                putExtra("marlaValue", marlaValue)
                putExtra("sqftValue", sqftValue)
                putExtra("coveredAreaValue", coveredAreaValue)
            }
            Log.d("IntentData", "Intent Data: $selectedDistrict, $selectedTown, $selectedPropertyArea, $selectedLandType, $selectedPropertyType, $coveredAreaValue")
            startActivity(intent)
        }



    }


    // Refresh logic in onResume()
    override fun onResume() {
        super.onResume()

        updateUIAfterDeletion()
        setupDropdowns()


    }

    private fun updateUIAfterDeletion() {
            binding.districtDropdown.text.clear()
            binding.townDropdown.text.clear()
            binding.propertyAreaDropdown.text.clear()
            binding.landTypeDropdown.text.clear()
            binding.propertyTypeDropdown.text.clear()
            binding.kanalEditText.text.clear()
            binding.marlaEditText.text.clear()
            binding.sqftEditText.text.clear()
            binding.coveredAreaEditText.text.clear()
    }


//    private fun setupDropdowns() {
//
//        val districtDropDown = binding.districtDropdown
//        val districtHashKey = districtDropDown.text.toString().trim().hashCode()
//
//        val townHashKey = "$districtHashKey-$selectedTown".hashCode()
//        val areaHashKey = "$townHashKey-$selectedPropertyArea".hashCode()
//        val finalKey = "$areaHashKey-$selectedLandType".hashCode()
//
//
//
//        // District Dropdown Setup
//        districtList = PreferencesManager.getDropdownList().toMutableList()
//        val districtAdapter = ArrayAdapter(
//            this,
//            custom_dropdown_item,
//            districtList
//        )
//        binding.districtDropdown.setAdapter(districtAdapter)
//
//
////        --------------------------------------------------------------------------------------
//
//
//// Fetch the districtTownMap once when the activity starts
//        val districtTownMap: MutableMap<Int, MutableList<String>> by lazy {
//            PreferencesManager.getDistrictTownMap()
//        }
//        // Retrieve the towns for the selected district
//        townList = districtTownMap[districtHashKey] ?: mutableListOf()
//
//
//// District dropdown item selection logic
//        binding.districtDropdown.setOnItemClickListener { _, _, _, _ ->
//
//            // Clear dependent dropdown fields for Town and Area
//            binding.townDropdown.text.clear()
//            binding.propertyAreaDropdown.text.clear()
//            binding.landTypeDropdown.text.clear()
//
//
//
//            // Log the retrieved data
//            Log.d("DistrictSelection", "Selected district: $selectedDistrict, towns: $townList")
//
//            val townAdapter = ArrayAdapter(
//                this,
//                custom_dropdown_item,
//                townList
//            )
//            binding.townDropdown.setAdapter(townAdapter)
//            binding.townDropdown.threshold = 1
//
//            // Request focus to the town dropdown
//            binding.townDropdown.requestFocus()
//
//            // Log the cleared selections
//            Log.d("DistrictSelection", "Cleared previous town and area selections.")
//        }
//
//
//// ------------------------------------------------------------------------------------
//
//
//
//// Fetch the TownAreaMap once when the activity starts
//        val townAreaMap =  PreferencesManager.getTownAreaMap()
//        areaList = townAreaMap[townHashKey] ?: mutableListOf()
//
//
//// On Town Selection
//        binding.townDropdown.setOnItemClickListener { _, _, _, _ ->
//
//            // Clear dependent dropdowns
//            binding.propertyAreaDropdown.text.clear()
//            binding.landTypeDropdown.text.clear()
//
//
//            // Update the property area dropdown
//            val areaAdapter = ArrayAdapter(
//                this,
//                custom_dropdown_item,
//                areaList
//            )
//
//            binding.propertyAreaDropdown.setAdapter(areaAdapter)
//
//            // Request focus to the property area dropdown
//            binding.propertyAreaDropdown.requestFocus()
//
//            // Log the selected town and areas
//            Log.d("TownSelection", "Selected town: $selectedTown, areas: $areaList")
//        }
//
////        ---------------------------------------------------------------------------------------
//
//// Fetch the TownAreaMap once when the activity starts
//        val areaLandMap =  PreferencesManager.getAreaLandMap()
//        // Retrieve the areas for the selected town
//        landList = areaLandMap[areaHashKey] ?: mutableListOf()
//
//
//
//
//// On Town Selection
//        binding.propertyAreaDropdown.setOnItemClickListener { _, _, _, _ ->
//
//            // Clear dependent dropdowns
//            binding.landTypeDropdown.text.clear()
//
//
//
//
//            // Update the property area dropdown
//            val landsAdapter = ArrayAdapter(
//                this,
//                custom_dropdown_item,
//                landList
//            )
//            binding.landTypeDropdown.setAdapter(landsAdapter)
//
//            // Request focus to the property area dropdown
//            binding.landTypeDropdown.requestFocus()
//
//        }
//
//
////        ---------------------------------------------------------------------------------------
//
//
//    }


    private fun setupDropdowns() {
// District Selection Listener
        binding.districtDropdown.setOnItemClickListener { _, _, _, _ ->
            val selectedDistrict = binding.districtDropdown.text.toString().trim()
            val districtHashKey = selectedDistrict.hashCode()

            // Clear dependent dropdowns when district changes
            binding.townDropdown.text.clear()
            binding.propertyAreaDropdown.text.clear()
            binding.landTypeDropdown.text.clear()

            // Get towns for selected district
            val districtTownMap = PreferencesManager.getDistrictTownMap()
            townList = districtTownMap[districtHashKey] ?: mutableListOf()

            // 🔹 Fix: Reset adapter with empty list if townList is empty
            val townAdapter = ArrayAdapter(this, custom_dropdown_item, townList.ifEmpty { mutableListOf() })
            binding.townDropdown.setAdapter(townAdapter)
            binding.townDropdown.threshold = 1
            binding.townDropdown.requestFocus()
        }

// Town Selection Listener
        binding.townDropdown.setOnItemClickListener { _, _, _, _ ->
            val currentDistrictHashKey = binding.districtDropdown.text.toString().trim().hashCode()
            val currentTownName = binding.townDropdown.text.toString().trim()
            val currentTownHashKey = "$currentDistrictHashKey-$currentTownName".hashCode()

            // Clear dependent dropdowns when town changes
            binding.propertyAreaDropdown.text.clear()
            binding.landTypeDropdown.text.clear()

            // Get areas for selected town
            val townAreaMap = PreferencesManager.getTownAreaMap()
            val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()

            // 🔹 Fix: Reset adapter with empty list if areas is empty
            val areaAdapter = ArrayAdapter(this, custom_dropdown_item, areas.ifEmpty { mutableListOf() })
            binding.propertyAreaDropdown.setAdapter(areaAdapter)
            binding.propertyAreaDropdown.threshold = 1
            binding.propertyAreaDropdown.requestFocus()

            Log.d("AreasLoaded", "Areas for $currentTownName: $areas")
        }

// Property Area Selection Listener
        binding.propertyAreaDropdown.setOnItemClickListener { _, _, _, _ ->
            val currentDistrictHashKey = binding.districtDropdown.text.toString().trim().hashCode()
            val currentTownName = binding.townDropdown.text.toString().trim()
            val currentTownHashKey = "$currentDistrictHashKey-$currentTownName".hashCode()
            val currentAreaName = binding.propertyAreaDropdown.text.toString().trim()
            val currentAreaHashKey = "$currentTownHashKey-$currentAreaName".hashCode()

            // Get land types for selected area
            val areaLandMap = PreferencesManager.getAreaLandMap()
            val lands = areaLandMap[currentAreaHashKey] ?: mutableListOf()

            // 🔹 Fix: Reset adapter with empty list if lands is empty
            val landsAdapter = ArrayAdapter(this, custom_dropdown_item, lands.ifEmpty { mutableListOf() })
            binding.landTypeDropdown.setAdapter(landsAdapter)
            binding.landTypeDropdown.threshold = 1
            binding.landTypeDropdown.requestFocus()

            Log.d("LandTypesLoaded", "Land types for $currentAreaName: $lands, $currentAreaHashKey")
        }

// Land Type Selection Listener
        binding.landTypeDropdown.setOnItemClickListener { _, _, _, _ ->
            binding.propertyTypeDropdown.requestFocus()
            binding.propertyTypeDropdown.showDropDown()
        }


    }







}