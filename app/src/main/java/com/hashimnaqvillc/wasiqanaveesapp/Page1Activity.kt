package com.hashimnaqvillc.wasiqanaveesapp

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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


//    Getting the data back from SettingsActivity
private val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        // Retrieve the updated data from the result intent
        districtList = result.data?.getStringArrayListExtra("updatedDistrictList") ?: listOf()
        townList = result.data?.getStringArrayListExtra("updatedTownList") ?: listOf()
        areaList = result.data?.getStringArrayListExtra("updatedAreaList") ?: listOf()

        // Log the updated data
        Log.d("updatedData", "Districts: $districtList")
        Log.d("updatedData", "Towns: $townList")
        Log.d("updatedData", "Areas: $areaList")


//        // Update dropdown adapters here if needed
//        updateDropdownAdapters()
    }
}






    // Variables for dynamically updated data
    private var districtToTowns: MutableMap<String, List<String>> = mutableMapOf()
    private var townsToPropertyAreas: MutableMap<String, List<String>> = mutableMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)


        binding = ActivityPage1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingButton = findViewById<ImageButton>(R.id.nav_settings_icon)
        // Launch SettingsActivity
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            settingsLauncher.launch(intent)
        }

        val backButton = findViewById<ImageButton>(R.id.nav_back)
        backButton.visibility = View.GONE

        val dateMonth = findViewById<TextView>(R.id.date_month_day)
        dateMonth.visibility = View.GONE
        val dateYear = findViewById<TextView>(R.id.date_year)
        dateYear.visibility = View.GONE

//        // Construct districtToTowns and townsToPropertyAreas mappings
//        constructMappings(updatedDistrictList, updatedTownList, updatedAreaList)

// District Dropdown
        val districts = districtToTowns.keys.toList()
        val districtAdapter = ArrayAdapter(
            this,
            R.layout.custom_dropdown_item,
            districts
        )
        binding.districtDropdown.setAdapter(districtAdapter)

// Handle District selection
        binding.districtDropdown.setOnClickListener {
            binding.districtDropdown.showDropDown()
        }
        binding.districtDropdown.setOnItemClickListener { parent, _, position, _ ->
            selectedDistrict = parent.getItemAtPosition(position) as String
            val towns = districtToTowns[selectedDistrict] ?: emptyList()

            // Update Town Dropdown
            val townAdapter = ArrayAdapter(
                this,
                R.layout.custom_dropdown_item,
                towns
            )
            binding.townDropdown.setAdapter(townAdapter)
            // Clear previous selections
            binding.townDropdown.text.clear()
            binding.propertyAreaDropdown.text.clear()
        }

// Handle Town selection
        binding.townDropdown.setOnClickListener {
            binding.townDropdown.showDropDown()
        }
        binding.townDropdown.setOnItemClickListener { parent, _, position, _ ->
            selectedTown = parent.getItemAtPosition(position) as String
            val propertyAreas = townsToPropertyAreas[selectedTown] ?: emptyList()

            // Update Property Area Dropdown
            val propertyAreaAdapter = ArrayAdapter(
                this,
                R.layout.custom_dropdown_item,
                propertyAreas
            )
            binding.propertyAreaDropdown.setAdapter(propertyAreaAdapter)

            // Clear previous selection
            binding.propertyAreaDropdown.text.clear()
        }

// Handle Property Area selection
        binding.propertyAreaDropdown.setOnClickListener {
            binding.propertyAreaDropdown.showDropDown()
        }
        binding.propertyAreaDropdown.setOnItemClickListener { parent, _, position, _ ->
            selectedPropertyArea = parent.getItemAtPosition(position) as String
            println("Selected Property Area: $selectedPropertyArea")
        }






        val propertyTypeAutoComplete = binding.propertyTypeDropdown
        val landTypeAutoComplete = binding.landTypeDropdown

// Data for the dropdowns
        val propertyTypeOptions = listOf("Plot", "Building")
        val landTypeOptions = listOf("Residential", "Commercial", "Agricultural", "Industrial", "Apartment", "Shop")

// Adapters for the dropdowns
        val propertyTypeAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item,
            propertyTypeOptions)
        val landTypeAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item,
            landTypeOptions)

// Set adapters to AutoCompleteTextViews
        propertyTypeAutoComplete.setAdapter(propertyTypeAdapter)
        landTypeAutoComplete.setAdapter(landTypeAdapter)

        landTypeAutoComplete.setOnClickListener {
            // Close the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            landTypeAutoComplete.showDropDown()
        }
// Handle selection actions for "Land Type"
        landTypeAutoComplete.setOnItemClickListener { _, _, position, _ ->
            selectedLandType = landTypeOptions[position]
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
            // Retrieve numeric values
            val kanalValue = binding.kanalEditText.text.toString().toDoubleOrNull()
            val marlaValue = binding.marlaEditText.text.toString().toDoubleOrNull()
            val sqftValue = binding.sqftEditText.text.toString().toDoubleOrNull()

            val coveredAreaValue = binding.coveredAreaEditText.text.toString().toDoubleOrNull()


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
                selectedLandType.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select an option in Land Type", Toast.LENGTH_SHORT).show()
                }
                selectedPropertyType.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select an option in Property Type", Toast.LENGTH_SHORT).show()
                }
                kanalValue == null && marlaValue == null && sqftValue == null -> {
                    Toast.makeText(this, "Please enter a valid Land Area", Toast.LENGTH_SHORT).show()
                }
                selectedPropertyType == "Building" && coveredAreaValue == null -> {
                    Toast.makeText(this, "Please enter a valid Covered Area for Building", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // All inputs are valid; proceed to the next activity
                    val intent = Intent(this, Page2Activity::class.java)
                    intent.putExtra("selectedDistrict", selectedDistrict)
                    intent.putExtra("selectedTown", selectedTown)
                    intent.putExtra("selectedPropertyArea", selectedPropertyArea)
                    intent.putExtra("selectedLandType", selectedLandType)
                    intent.putExtra("selectedPropertyType", selectedPropertyType)
                    if (selectedPropertyType == "Building"){
                        intent.putExtra("coveredArea", coveredAreaValue)
                    }
                    intent.putExtra("kanalValue", kanalValue)
                    intent.putExtra("marlaValue", marlaValue)
                    intent.putExtra("sqftValue", sqftValue)
                    startActivity(intent)
                }
            }
        }


















    }





//    // Function to construct mappings
//    private fun constructMappings(
//        updatedDistrictList: List<String>,
//        updatedTownList: List<String>,
//        updatedAreaList: List<String>
//    ) {
//        // Example logic to dynamically populate districtToTowns and townsToPropertyAreas
//        districtToTowns.clear()
//        townsToPropertyAreas.clear()
//
//        updatedDistrictList.forEach { district ->
//            // Example: Map each district to a subset of towns
//            districtToTowns[district] = updatedTownList.filter { it.startsWith(district.first()) }
//        }
//
//        updatedTownList.forEach { town ->
//            // Example: Map each town to a subset of areas
//            townsToPropertyAreas[town] = updatedAreaList.filter { it.startsWith(town.first()) }
//        }
//    }

    }