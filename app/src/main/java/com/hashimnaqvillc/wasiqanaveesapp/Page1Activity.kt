package com.hashimnaqvillc.wasiqanaveesapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage1Binding

class Page1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityPage1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPage1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingButton = findViewById<ImageButton>(com.hashimnaqvillc.wasiqanaveesapp.R.id.nav_settings_icon)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<ImageButton>(com.hashimnaqvillc.wasiqanaveesapp.R.id.nav_back)
        backButton.visibility = View.GONE

        val dateMonth = findViewById<TextView>(com.hashimnaqvillc.wasiqanaveesapp.R.id.date_month_day)
        dateMonth.visibility = View.GONE
        val dateYear = findViewById<TextView>(com.hashimnaqvillc.wasiqanaveesapp.R.id.date_year)
        dateYear.visibility = View.GONE




        binding.findButton.setOnClickListener {
            val intent = Intent(this, Page2Activity::class.java)
            startActivity(intent)
        }



// Data for districts to towns
        val districtToTowns = mapOf(
            "Lahore" to listOf("Allama Iqbal Town", "Ravi Town"),
            "Islamabad" to listOf("F-8", "G-6"),
            "Karachi" to listOf("Rainbow Town", "Baloch Town"),
            "Pindi" to listOf("Saddar", "Satellite Town")
        )

// Data for towns to property areas
        val townsToPropertyAreas = mapOf(
            "Allama Iqbal Town" to listOf("Sukh Chayn Gardens", "Canal Gardens", "Bahria Town"),
            "Ravi Town" to listOf("Samanabad", "Gulshan Ravi"),
            "Rainbow Town" to listOf("Block A", "Block B"),
            "Baloch Town" to listOf("Zone 1", "Zone 2"),
            "F-8" to listOf("Street 1", "Street 2"),
            "Saddar" to listOf("Street X", "Street Y")
        )

// District Dropdown
        val districts = districtToTowns.keys.toList()
        val districtAdapter = ArrayAdapter(
            this,
            com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item,
            districts
        )
        binding.districtDropdown.setAdapter(districtAdapter)

// Handle District selection
        binding.districtDropdown.setOnClickListener {
            binding.districtDropdown.showDropDown()
        }
        binding.districtDropdown.setOnItemClickListener { parent, _, position, _ ->
            val selectedDistrict = parent.getItemAtPosition(position) as String
            val towns = districtToTowns[selectedDistrict] ?: emptyList()

            // Update Town Dropdown
            val townAdapter = ArrayAdapter(
                this,
                com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item,
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
            val selectedTown = parent.getItemAtPosition(position) as String
            val propertyAreas = townsToPropertyAreas[selectedTown] ?: emptyList()

            val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("TOWN", selectedTown)
                apply()
            }

            // Update Property Area Dropdown
            val propertyAreaAdapter = ArrayAdapter(
                this,
                com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item,
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
            val selectedPropertyArea = parent.getItemAtPosition(position) as String
            println("Selected Property Area: $selectedPropertyArea")
        }

        val propertyTypeAutoComplete: AutoCompleteTextView = findViewById(com.hashimnaqvillc.wasiqanaveesapp.R.id.propertyTypeDropdown)
        val landTypeAutoComplete: AutoCompleteTextView = findViewById(com.hashimnaqvillc.wasiqanaveesapp.R.id.landTypeDropdown)

// Data for the dropdowns
        val propertyTypeOptions = listOf("Plot", "Building")
        val landTypeOptions = listOf("Residential", "Commercial", "Agricultural", "Industrial", "Apartment", "Shop")

// Adapters for the dropdowns
        val propertyTypeAdapter = ArrayAdapter(this, com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item,
            propertyTypeOptions)
        val landTypeAdapter = ArrayAdapter(this, com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item,
            landTypeOptions)

// Set adapters to AutoCompleteTextViews
        propertyTypeAutoComplete.setAdapter(propertyTypeAdapter)
        landTypeAutoComplete.setAdapter(landTypeAdapter)

        binding.landTypeDropdown.setOnClickListener {
            landTypeAutoComplete.showDropDown()
        }
// Handle selection actions for "Land Type"
        landTypeAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val selectedLandType = landTypeOptions[position]


            val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("LAND_TYPE", selectedLandType)
                apply()
            }

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
            val selectedPropertyType = propertyTypeOptions[position]
            val coveredArea = findViewById<TextView>(com.hashimnaqvillc.wasiqanaveesapp.R.id.coveredArea_text)
            val coveredAreaEditText = findViewById<TextView>(com.hashimnaqvillc.wasiqanaveesapp.R.id.coveredAreaEditText)

            // Save the selected property type in SharedPreferences
            val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("PROPERTY_TYPE", selectedPropertyType)
                apply()
            }

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












    }
}