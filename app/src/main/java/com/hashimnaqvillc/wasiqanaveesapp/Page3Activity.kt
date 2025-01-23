package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage3Binding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Page3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPage3Binding

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


//        Get Dropdown selection from Page 2
        val selectedPropertyType = intent.getStringExtra("selectedPropertyType")
        val selectedPropertyArea = intent.getStringExtra("selectedPropertyArea")
        val selectedDistrict = intent.getStringExtra("selectedDistrict")

//        Get Land Area Values from Page 2
        val kanalValue = intent.getIntExtra("kanalValue", 0)
        val marlaValue = intent.getIntExtra("marlaValue", 0)
        val sqftValue = intent.getIntExtra("sqftValue", 0)
        val coveredArea = intent.getIntExtra("coveredArea", 0)

        val totalDC = intent.getIntExtra("totalDC", 0)
        val totalFBR = intent.getIntExtra("totalFBR", 0)

        super.onCreate(savedInstanceState)
        binding = ActivityPage3Binding.inflate(layoutInflater)
        setContentView(binding.root)


// Create a string to display land area
        val landAreaStringBuilder = StringBuilder()

        if (kanalValue != 0) {
            landAreaStringBuilder.append("$kanalValue Kanal, ")
        }
        if (marlaValue != 0) {
            landAreaStringBuilder.append("$marlaValue Marla, ")
        }
        if (sqftValue != 0) {
            landAreaStringBuilder.append("$sqftValue Sqft")
        }
        // Remove the trailing comma and space, if any
        val landAreaDisplayText = landAreaStringBuilder.toString().trimEnd(',', ' ')

        // Set the text if there's any value, otherwise show a placeholder
        binding.landAreaTextPg3.text = landAreaDisplayText.ifEmpty {
            "No land area specified"
        }

        if(selectedPropertyType == "Plot"){
            binding.coveredAreaTextPg3.visibility = View.GONE
        }else{
            binding.coveredAreaTextPg3.visibility = View.VISIBLE
        }


        binding.coveredAreaTextPg3.text = coveredArea.toString()
        binding.propertyTypeTextPg3.text = selectedPropertyType
        binding.propertyAreaTextPg3.text = "$selectedPropertyArea, $selectedDistrict"

//        Fetching updated Tax % Data from Shared Preference

        val stampDuty = PreferencesManager.getData("stampDutyEditTextInner")
        val tmaCorp = PreferencesManager.getData("tmaCorpEditTextInner")
        val regFee = PreferencesManager.getData("regsFeeEditTextInner")

        val fbr236kFiler = PreferencesManager.getData("fbr236KFilerEditTextInner")
        val fbr236kNonFiler = PreferencesManager.getData("fbr236KNonfilerEditTextInner")
        val fbr236kLateFiler = PreferencesManager.getData("fbr236KLatefilerEditTextInner")

        val fbr236CFiler = PreferencesManager.getData("fbr236CFilerEditTextInner")
        val fbr236CNonFiler = PreferencesManager.getData("fbr236CNonfilerEditTextInner")
        val fbr236CLateFiler = PreferencesManager.getData("fbr236ClatefilerEditTextInner")
//
        val officeName = PreferencesManager.getData("officeNameEditTextInner")
        val officePhone = PreferencesManager.getData("officePhoneEditTextInner")



//        val stampDuty = 1
//        val tmaCorp = 1
//        val regFee =  0.1
//
//        val fbr236kFiler = 3
//        val fbr236kNonFiler = 6
//        val fbr236kLateFiler = 10.5
//
//        val fbr236CFiler = 3
//        val fbr236CNonFiler = 6
//        val fbr236CLateFiler = 10.5




        binding.stampDutyTax.text = stampDuty
        binding.tmaCorpTax.text = tmaCorp
        binding.regsTax.text = regFee

        binding.fbr236kTax.text = fbr236kFiler
        binding.seller236CTax.text = fbr236CFiler

        binding.officeNamePg3.text = "Naqvi's Brothers"
        binding.officePhonePg3.text = " : 0333-4415786"










        val settingIcon = findViewById<ImageButton>(R.id.nav_settings_icon)
        settingIcon.visibility = View.GONE

        val backBtn = findViewById<ImageButton>(R.id.nav_back)
        backBtn.setOnClickListener {
            finish()
        }




        // Set up the dropdown options
        val sellerOptions = mutableListOf("Filer", "Late Filer", "Non-Filer")

// Create an ArrayAdapter for the AutoCompleteTextView
        val sellerAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item, sellerOptions)

// Set the adapter for the sellerFilerDropdown
        val sellerDropDown = binding.sellerDropdown
        sellerDropDown.setAdapter(sellerAdapter)

// Set a listener to handle selection changes
        sellerDropDown.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = sellerOptions[position]
            when (selectedOption) {
                "Filer" -> {
                    // Perform actions for Filer
                    binding.seller236CTax.text = fbr236CFiler
                }
                "Late Filer" -> {
                    // Perform actions for Late Filer
                    binding.seller236CTax.text = fbr236CLateFiler

                }
                "Non-Filer" -> {
                    // Perform actions for Non-Filer
                    binding.seller236CTax.text = fbr236CNonFiler

                }
            }
        }


        // Set up the dropdown options
        val purchaserOptions = mutableListOf("Filer", "Late Filer", "Non-Filer")

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
                    binding.fbr236kTax.text = fbr236kFiler


                }
                "Late Filer" -> {
                    // Perform actions for Late Filer
                    binding.fbr236kTax.text = fbr236kLateFiler
                    Log.d("LateFiler", fbr236kLateFiler)

                }
                "Non-Filer" -> {
                    // Perform actions for Non-Filer
                    binding.fbr236kTax.text = fbr236kNonFiler
                    Log.d("NonFiler", fbr236kNonFiler)

                }
            }
        }

        val stampDutyT = stampDuty.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val stampDutyAmount = totalDC.toDouble() * (stampDutyT / 100)
        binding.stampDutyTaxPKR.text = stampDutyAmount.toInt().toString()


        val tMACorpT = tmaCorp.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val tMACorpAmount = totalDC.toDouble() * (tMACorpT / 100)
        binding.tmaCorpTaxPKR.text = tMACorpAmount.toInt().toString()


        val regFeeT = regFee.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val regFeeAmount = totalFBR.toDouble() * (regFeeT / 100)
        binding.regsTaxPKR.text = regFeeAmount.toInt().toString()


        val fbr236kT = fbr236kFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
        binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()


        val fbr236cT = fbr236CFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
        binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()








        sellerDropDown.setOnClickListener {
            sellerDropDown.showDropDown()
        }

        purchaserDropDown.setOnClickListener {
            purchaserDropDown.showDropDown()
        }







//        Check Boxes

        val headerCheckbox: CheckBox = binding.typesRadioBtn
        val checkboxes: MutableList<CheckBox> = mutableListOf(
            binding.stampDutyRadioBtn,
            binding.tmaCorpRadioBtn,
            binding.fbr236kRadioBtn,
            binding.seller236CRadioBtn,
            binding.sellerNOCRadioBtn,
            binding.transferFeeRadioBtn,
            binding.wasiqaFeeRadioBtn
        )

        headerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            checkboxes.forEach { checkbox ->
                checkbox.isChecked = isChecked
            }
        }


        findViewById<TextView>(R.id.date_month_day).text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))









    }
}