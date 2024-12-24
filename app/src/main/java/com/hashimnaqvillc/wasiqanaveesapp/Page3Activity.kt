package com.hashimnaqvillc.wasiqanaveesapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage3Binding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Page3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPage3Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPage3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingIcon = findViewById<ImageButton>(R.id.nav_settings_icon)
        settingIcon.visibility = View.GONE

        val backBtn = findViewById<ImageButton>(R.id.nav_back)
        backBtn.setOnClickListener {
            finish()
        }




        // Set up the dropdown options
        val sellerOptions = listOf("Filer", "Late Filer", "Non-Filer")

// Create an ArrayAdapter for the AutoCompleteTextView
        val sellerAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item, sellerOptions)

// Set the adapter for the sellerFilerDropdown
        val sellerDropDown =binding.sellerDropdown
        sellerDropDown.setAdapter(sellerAdapter)

// Set a listener to handle selection changes
        sellerDropDown.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = sellerOptions[position]
            when (selectedOption) {
                "Filer" -> {
                    // Perform actions for Filer
                    Toast.makeText(this, "Filer selected", Toast.LENGTH_SHORT).show()
                    // Add your specific actions here
                }
                "Late Filer" -> {
                    // Perform actions for Late Filer
                    Toast.makeText(this, "Late Filer selected", Toast.LENGTH_SHORT).show()
                    // Add your specific actions here
                }
                "Non-Filer" -> {
                    // Perform actions for Non-Filer
                    Toast.makeText(this, "Non-Filer selected", Toast.LENGTH_SHORT).show()
                    // Add your specific actions here
                }
            }
        }


        // Set up the dropdown options
        val purchaserOptions = listOf("Filer", "Late Filer", "Non-Filer")

// Create an ArrayAdapter for the AutoCompleteTextView
        val purchaserAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item, purchaserOptions)

// Set the adapter for the purchaserDropdown
        val purchaserDropDown = binding.purchaserDropdown
        purchaserDropDown.setAdapter(purchaserAdapter)

// Set a listener to handle selection changes
        purchaserDropDown.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = purchaserOptions[position]
            when (selectedOption) {
                "Filer" -> {
                    // Perform actions for Filer
                    Toast.makeText(this, "Purchaser is Filer", Toast.LENGTH_SHORT).show()
                    // Add specific actions here for Filer
                }
                "Late Filer" -> {
                    // Perform actions for Late Filer
                    Toast.makeText(this, "Purchaser is Late Filer", Toast.LENGTH_SHORT).show()
                    // Add specific actions here for Late Filer
                }
                "Non-Filer" -> {
                    // Perform actions for Non-Filer
                    Toast.makeText(this, "Purchaser is Non-Filer", Toast.LENGTH_SHORT).show()
                    // Add specific actions here for Non-Filer
                }
            }
        }

        sellerDropDown.setOnClickListener {
            sellerDropDown.showDropDown()
        }

        purchaserDropDown.setOnClickListener {
            purchaserDropDown.showDropDown()
        }







//        Check Boxes

        val headerCheckbox: CheckBox = findViewById(R.id.typesRadioBtn)
        val checkboxes: List<CheckBox> = listOf(
            findViewById(R.id.stampDutyRadioBtn),
            findViewById(R.id.tmaCorpRadioBtn),
            findViewById(R.id.fbr236kRadioBtn),
            findViewById(R.id.seller236CRadioBtn),
            findViewById(R.id.sellerNOCRadioBtn),
            findViewById(R.id.transferFeeRadioBtn),
            findViewById(R.id.wasiqaFeeRadioBtn),
        )

        headerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            checkboxes.forEach { checkbox ->
                checkbox.isChecked = isChecked
            }
        }


        // Get references to the TextViews
        val dateMonthDay = findViewById<TextView>(R.id.date_month_day)
        val dateYear = findViewById<TextView>(R.id.date_year)

        // Get the current date
        val currentDate = LocalDate.now()

        // Format the date (e.g., "Nov 22")
        val formatter = DateTimeFormatter.ofPattern("MMM dd")
        val formattedDate = currentDate.format(formatter)

        // Get the current year
        val currentYear = currentDate.year.toString()

        // Set the values to the TextViews
        dateMonthDay.text = formattedDate
        dateYear.text = currentYear


//        val myCheckbox = findViewById<CheckBox>(R.id.seller_checkbox)
//
//// Set a listener to toggle state
//        myCheckbox.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Do something when checked
//                println("Checkbox selected")
//            } else {
//                // Do something when unchecked
//                println("Checkbox unselected")
//            }
//        }








    }
}