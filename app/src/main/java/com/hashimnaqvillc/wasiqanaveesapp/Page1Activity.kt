package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
            val selectedArea = binding.propertyAreaDropdown.text.toString().trim()
            val selectedLandType = binding.landTypeDropdown.text.toString().trim()

            // Retrieve numeric values
            val kanalValue = binding.kanalEditText.text.toString().toDoubleOrNull() ?: 0.0
            val marlaValue = binding.marlaEditText.text.toString().toDoubleOrNull() ?: 0.0
            val sqftValue = binding.sqftEditText.text.toString().toDoubleOrNull() ?: 0.0
            val sqftPerMarla = 225.0

// Retrieve land rates for the selected area and land type
            val landRates = areaLandRates[selectedArea]?.get(selectedLandType)

// Safely retrieve individual values with default fallbacks
            val marlaDc = landRates?.get("MarlaDC")?.toDoubleOrNull() ?: 1.0
            val marlaFbr = landRates?.get("MarlaFBR") ?: "N/A"
            val coveredAreaDc = landRates?.get("CoveredAreaDC") ?: "N/A"
            val coveredAreaFbr = landRates?.get("CoveredAreaFBR") ?: "N/A"
            val transferFee = landRates?.get("TransferFee") ?: "N/A"
            val khasraNumber = landRates?.get("KhasraNumber") ?: "N/A"


//            Log.d("LandRates", "MarlaDC: $marlaDc, MarlaFBR: $marlaFbr")

            val totalSqft = (kanalValue * 20 * sqftPerMarla + marlaValue * sqftPerMarla + sqftValue) / sqftPerMarla
            val plotValueDC = totalSqft * marlaDc

            Log.d("Calculation", "Total Sqft: $plotValueDC")

            // Retrieve covered area value safely
            val coveredAreaValue = binding.coveredAreaEditText.text.toString().toDoubleOrNull() ?: 0.0

            // Log results or proceed with further operations
            Log.d("Calculation", "Total Sqft: $totalSqft, Covered Area Value: $coveredAreaValue")

            // Optional: Show results in a Toast or update UI
            Toast.makeText(
                this,
                "Total Sqft: $totalSqft\nCovered Area Value: $coveredAreaValue",
                Toast.LENGTH_SHORT
            ).show()








//            val totalSqft = (kanalValue!! * 20 * sqftPerMarla + marlaValue!! * sqftPerMarla + sqftValue!!) / sqftPerMarla





            // Validate dropdown selections and numeric inputs
            when {
                selectedDistrict.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select a district", Toast.LENGTH_SHORT).show()
                }
                selectedTown.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select a town", Toast.LENGTH_SHORT).show()
                }
                selectedPropertyArea.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select a property area", Toast.LENGTH_SHORT).show()
                }
                selectedPropertyType.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select an option in Property Type", Toast.LENGTH_SHORT).show()
                }

//                selectedPropertyType == "Building" && coveredAreaValue == null -> {
//                    Toast.makeText(this, "Please enter a valid Covered Area for Building", Toast.LENGTH_SHORT).show()
//                }
                else -> {
                    // All inputs are valid; proceed to the next activity
                    val intent = Intent(this, Page2Activity::class.java)
                    intent.putExtra("selectedDistrict", selectedDistrict)
                    intent.putExtra("selectedTown", selectedTown)
                    intent.putExtra("selectedPropertyArea", selectedPropertyArea)
                    intent.putExtra("selectedLandType", selectedLandType)
                    intent.putExtra("plotValueDC", plotValueDC)
//                    intent.putExtra("selectedPropertyType", selectedPropertyType)
//                    if (selectedPropertyType == "Building"){
//                        intent.putExtra("coveredArea", coveredAreaValue)
//                    }
                    intent.putExtra("kanalValue", kanalValue)
                    intent.putExtra("marlaValue", marlaValue)
                    intent.putExtra("sqftValue", sqftValue)
                    startActivity(intent)
                }
            }
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
        val districtTownMap: MutableMap<String, MutableList<String>> by lazy {
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

            // Retrieve the towns for the selected district
            val towns = districtTownMap[selectedDistrict] ?: mutableListOf()

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
        val townAreaMap: MutableMap<String, MutableList<String>> by lazy {
            PreferencesManager.getTownAreaMap()
        }

// On Town Selection
        binding.townDropdown.setOnItemClickListener { _, _, _, _ ->

            // Clear dependent dropdowns
            binding.propertyAreaDropdown.text.clear()
            binding.landTypeDropdown.text.clear()

            // Normalize the selected town to avoid formatting issues
            selectedTown = binding.townDropdown.text.toString().trim()

            // Retrieve the areas for the selected town
            val areas = townAreaMap[selectedTown] ?: mutableListOf()

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
        val areaLandMap: MutableMap<String, MutableList<String>> by lazy {
            PreferencesManager.getAreaLandMap()
        }

// On Town Selection
        binding.propertyAreaDropdown.setOnItemClickListener { _, _, _, _ ->

            // Clear dependent dropdowns
            binding.landTypeDropdown.text.clear()

            // Normalize the selected town to avoid formatting issues
            selectedPropertyArea = binding.propertyAreaDropdown.text.toString().trim()

            // Retrieve the areas for the selected town
            val lands = areaLandMap[selectedPropertyArea] ?: mutableListOf()

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