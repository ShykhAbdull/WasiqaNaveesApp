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
    private lateinit var districtList: List<String>
    private lateinit var townList: List<String>
    private lateinit var areaList: List<String>


    // Variables for dynamically updated data
    private var townsToPropertyAreas: MutableMap<String, List<String>> = mutableMapOf()


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
        val dateYear = findViewById<TextView>(R.id.date_year)
        dateYear.visibility = View.GONE


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






// Handle selection actions for "Land Type"
        landTypeAutoComplete.setOnItemClickListener { _, _, position, _ ->
//            selectedLandType = landTypeOptions[position]
            // Take actions based on the selected land type
            when (selectedLandType) {
                "Residential" -> {
                }
                "Commercial" -> {
                    // Action for Commercial
                }
                "Agricultural" -> {
//                    Action to perform for Agricultural
                }
                "Industrial" -> {
//                    Action to perform for Industrial
                }
                "Apartment" -> {
//                    Action to perform for Industrial
                }
                "Shop" -> {
//                    Action to perform for Industrial
                }
            }
            propertyTypeAutoComplete.requestFocus()
            propertyTypeAutoComplete.showDropDown()
        }


        binding.propertyTypeDropdown.setOnClickListener {
            propertyTypeAutoComplete.showDropDown()
        }
// Handle selection actions for "Property Type"
        propertyTypeAutoComplete.setOnItemClickListener { _, _, position, _ ->
            selectedPropertyType = propertyTypeOptions[position]
            val coveredArea = binding.coveredAreaText
            val coveredAreaEditText = binding.coveredAreaEditText



            // Take actions based on the selected property type
            when (selectedPropertyType) {
                "Plot" -> {
                    coveredArea.visibility = View.GONE
                    coveredAreaEditText.visibility = View.GONE                     }
                "Building" -> {
                    coveredArea.visibility = View.VISIBLE
                    coveredAreaEditText.visibility = View.VISIBLE                }
            }
        }


        binding.findButton.setOnClickListener {

            val areaLandRates = PreferencesManager.getLandRates()
            val selectedDistrict = binding.districtDropdown.text.toString().trim()
            val selectedTown = binding.townDropdown.text.toString().trim()
            val selectedArea = binding.propertyAreaDropdown.text.toString().trim()
            val selectedLandType = binding.landTypeDropdown.text.toString().trim()

            // Validate dropdown selections
            if (selectedDistrict.isEmpty() || selectedTown.isEmpty() || selectedArea.isEmpty() || selectedLandType.isEmpty()) {
                Toast.makeText(this, "Please select all required fields (District, Town, Area, Land Type).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve numeric values safely
            val kanalValue = binding.kanalEditText.text.toString().toDoubleOrNull() ?: 0.0
            val marlaValue = binding.marlaEditText.text.toString().toDoubleOrNull() ?: 0.0
            val sqftValue = binding.sqftEditText.text.toString().toDoubleOrNull() ?: 0.0
            val sqftPerMarla = 225.0

            // Fetch land rates for the selected district, town, area, and land type
            val landRates = areaLandRates[selectedDistrict]?.get(selectedTown)?.get(selectedArea)?.get(selectedLandType)

            if (landRates == null) {
                Toast.makeText(this, "No land rates found for the selected area and land type.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Safely retrieve individual values with defaults
            val marlaDc = landRates["MarlaDC"]?.toDoubleOrNull() ?: 0.0
            val marlaFbr = landRates["MarlaFBR"]?.toDoubleOrNull() ?: 0.0
            val coveredAreaDc = landRates["CoveredAreaDC"]?.toDoubleOrNull() ?: 0.0
            val coveredAreaFbr = landRates["CoveredAreaFBR"]?.toDoubleOrNull() ?: 0.0
            val khasraNumber = landRates["KhasraNumber"] ?: "N/A"

            // Calculate total square feet and plot value
            val totalSqft = kanalValue * 20 * sqftPerMarla + marlaValue * sqftPerMarla + sqftValue

            val plotValueDC = totalSqft / sqftPerMarla * marlaDc

//            val plotValueFBR = totalSqft / sqftPerMarla * marlaFbr









            Log.d("Calculation", "Total Sqft: $totalSqft, Plot Value DC: $plotValueDC")

            // Retrieve covered area value safely
            val coveredAreaValue = binding.coveredAreaEditText.text.toString().toDoubleOrNull() ?: 0.0

            // Log additional details
            Log.d("LandRates", "MarlaDC: $marlaDc, Covered Area DC: $coveredAreaDc, Khasra Number: $khasraNumber")
            Log.d("Calculation", "Total Sqft: $totalSqft, Covered Area Value: $coveredAreaValue")

            // Optional: Show results in a Toast or update UI
            Toast.makeText(
                this,
                "Total Sqft: $totalSqft\nPlot Value DC: $plotValueDC\nCovered Area Value: $coveredAreaValue",
                Toast.LENGTH_LONG
            ).show()

            // Proceed to the next activity with validated data
            val intent = Intent(this, Page2Activity::class.java).apply {
                putExtra("selectedDistrict", selectedDistrict)
                putExtra("selectedTown", selectedTown)
                putExtra("selectedArea", selectedArea)
                putExtra("selectedLandType", selectedLandType)
                putExtra("plotValueDC", plotValueDC)
                putExtra("kanalValue", kanalValue)
                putExtra("marlaValue", marlaValue)
                putExtra("sqftValue", sqftValue)
                putExtra("coveredAreaValue", coveredAreaValue)
            }
            startActivity(intent)
        }



    }


    // Refresh logic in onResume()
    override fun onResume() {
        super.onResume()

        setupDropdowns()

    }


    private fun setupDropdowns() {

        // District Dropdown Setup
        val districtList = PreferencesManager.getDropdownList().toMutableList()
        val districtAdapter = ArrayAdapter(
            this,
            custom_dropdown_item,
            districtList
        )
        binding.districtDropdown.setAdapter(districtAdapter)


//        --------------------------------------------------------------------------------------


// Fetch the districtTownMap once when the activity starts
        val districtTownMap: MutableMap<Int, MutableList<String>> by lazy {
            PreferencesManager.getDistrictTownMap()
        }

// District dropdown item selection logic
        binding.districtDropdown.setOnItemClickListener { _, _, _, _ ->

            // Clear dependent dropdown fields for Town and Area
            binding.townDropdown.text.clear()
            binding.propertyAreaDropdown.text.clear()
            binding.landTypeDropdown.text.clear()

            // Normalize the selected district to avoid formatting issues
            selectedDistrict = binding.districtDropdown.text.toString().trim()
            val selectedDistrictIndex = districtList.indexOf(selectedDistrict)

            // Retrieve the towns for the selected district
            val towns = districtTownMap[selectedDistrictIndex] ?: mutableListOf()

            // Log the retrieved data
            Log.d("DistrictSelection", "Selected district: $selectedDistrict, towns: $towns")

            // Update the town dropdown
            val townAdapter = ArrayAdapter(
                this,
                custom_dropdown_item,
                towns
            )
            binding.townDropdown.setAdapter(townAdapter)
            binding.townDropdown.threshold = 1

            // Request focus to the town dropdown
            binding.townDropdown.requestFocus()

            // Log the cleared selections
            Log.d("DistrictSelection", "Cleared previous town and area selections.")
        }


// ------------------------------------------------------------------------------------

// Fetch the TownAreaMap once when the activity starts
        val townAreaMap: MutableMap<Int, MutableList<String>> by lazy {
            PreferencesManager.getTownAreaMap()
        }

// On Town Selection
        binding.townDropdown.setOnItemClickListener { _, _, _, _ ->

            // Clear dependent dropdowns
            binding.propertyAreaDropdown.text.clear()
            binding.landTypeDropdown.text.clear()

            // Normalize the selected town to avoid formatting issues
            selectedTown = binding.townDropdown.text.toString().trim()
            val selectedTownIndex = townList.indexOf(selectedTown)

            // Retrieve the areas for the selected town
            val areas = townAreaMap[selectedTownIndex] ?: mutableListOf()

            // Update the property area dropdown
            val areaAdapter = ArrayAdapter(
                this,
                custom_dropdown_item,
                areas
            )
            binding.propertyAreaDropdown.setAdapter(areaAdapter)

            // Request focus to the property area dropdown
            binding.propertyAreaDropdown.requestFocus()

            // Log the selected town and areas
            Log.d("TownSelection", "Selected town: $selectedTown, areas: $areas")
        }

//        ---------------------------------------------------------------------------------------

// Fetch the TownAreaMap once when the activity starts
        val areaLandMap: MutableMap<Int, MutableList<String>> by lazy {
            PreferencesManager.getAreaLandMap()
        }

// On Town Selection
        binding.propertyAreaDropdown.setOnItemClickListener { _, _, _, _ ->

            // Clear dependent dropdowns
            binding.landTypeDropdown.text.clear()

            // Normalize the selected town to avoid formatting issues
            selectedPropertyArea = binding.propertyAreaDropdown.text.toString().trim()
            val selectedPropertyAreaIndex = areaList.indexOf(selectedPropertyArea)

            // Retrieve the areas for the selected town
            val lands = areaLandMap[selectedPropertyAreaIndex] ?: mutableListOf()

            // Update the property area dropdown
            val landsAdapter = ArrayAdapter(
                this,
                custom_dropdown_item,
                lands
            )
            binding.landTypeDropdown.setAdapter(landsAdapter)

            // Request focus to the property area dropdown
            binding.landTypeDropdown.requestFocus()

        }


//        ---------------------------------------------------------------------------------------


    }







}