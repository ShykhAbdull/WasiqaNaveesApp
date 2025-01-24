package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
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

    private var tMACorpAmount: Double = 0.0
    private var stampDutyAmount: Double = 0.0
    private var regFeeAmount: Double = 0.0
    private var fbr236kAmount: Double = 0.0
    private var fbr236cAmount: Double = 0.0

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

        findViewById<TextView>(R.id.date_month_day).text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))







//        setupCheckBoxes()

    }


    override fun onResume() {
        super.onResume()

        setupCheckBoxes()
    }

    @SuppressLint("SetTextI18n")
    private fun setupCheckBoxes() {




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
//                    Seller Taxes are "C"

                    binding.seller236CTax.text = fbr236CFiler

                    // Perform actions for Filer
                    val fbr236cT = fbr236CFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

                    binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()

                }
                "Late Filer" -> {
                    binding.seller236CTax.text = fbr236CLateFiler

                    // Perform actions for Late Filer
                    val fbr236cT = fbr236CLateFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

                    binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()


                }
                "Non-Filer" -> {

                    binding.seller236CTax.text = fbr236CNonFiler

                    // Perform actions for Filer
                    val fbr236cT = fbr236CNonFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

                    binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()


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

//                    Purchaser Taxes are "K"
                    // Perform actions for Filer
                    binding.fbr236kTax.text = fbr236kFiler

                    val fbr236kT = fbr236kFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

                    binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()


                }
                "Late Filer" -> {
                    binding.fbr236kTax.text = fbr236kLateFiler


                    val fbr236kT = fbr236kLateFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

                    binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()

                }
                "Non-Filer" -> {
                    binding.fbr236kTax.text = fbr236kNonFiler


                    val fbr236kT = fbr236kNonFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

                    binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()



                }
            }
        }

        val registryOptions = mutableListOf("گفت ڈیڈ","برائے رجسٹری","سوسائٹی ٹرانسفر")
        val registryAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item, registryOptions)
        binding.registryTransferDropdown.setAdapter(registryAdapter)

        binding.registryTransferDropdown.setOnItemClickListener { _, _, position, _ ->

            val selectedOption = registryOptions[position]

            when (selectedOption) {
                "برائے رجسٹری" -> {
                    binding.plraRow.visibility = View.VISIBLE
                    binding.plraSeparator.visibility = View.VISIBLE

                    binding.fbr236kRow.visibility = View.VISIBLE
                    binding.fbrSeperator.visibility = View.VISIBLE
                    binding.seller236CRow.visibility = View.VISIBLE
                    binding.seller236CSeparator.visibility = View.VISIBLE

                    binding.stampDutyTaxPKR.text = (stampDutyAmount.toInt()+1000).toString()
                }

                "سوسائٹی ٹرانسفر" -> {
                    binding.plraRow.visibility = View.GONE
                    binding.plraSeparator.visibility = View.GONE

                    binding.fbr236kRow.visibility = View.VISIBLE
                    binding.fbrSeperator.visibility = View.VISIBLE
                    binding.seller236CRow.visibility = View.VISIBLE
                    binding.seller236CSeparator.visibility = View.VISIBLE



                    binding.stampDutyTaxPKR.text = (stampDutyAmount.toInt()-1000).toString()

                }

                "گفت ڈیڈ" ->{
                    binding.plraRow.visibility = View.VISIBLE
                    binding.plraSeparator.visibility = View.VISIBLE

                    binding.fbr236kRow.visibility = View.GONE
                    binding.fbrSeperator.visibility = View.GONE
                    binding.seller236CRow.visibility = View.GONE
                    binding.seller236CSeparator.visibility = View.GONE

                }
            }
        }

        binding.registryTransferDropdown.setOnClickListener {
            binding.registryTransferDropdown.showDropDown()
            binding.registryTransferDropdown.requestFocus()
        }










//        Default Values upon Activity creation

        val stampDutyT = stampDuty.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        stampDutyAmount = totalDC.toDouble() * (stampDutyT / 100) + 1000
        binding.stampDutyTaxPKR.text = stampDutyAmount.toInt().toString()


        val tMACorpT = tmaCorp.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        tMACorpAmount = totalDC.toDouble() * (tMACorpT / 100)
        binding.tmaCorpTaxPKR.text = tMACorpAmount.toInt().toString()


        val regFeeT = regFee.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        regFeeAmount = totalFBR.toDouble() * (regFeeT / 100)
        if (regFeeAmount >= 3000) {
            binding.regsTaxPKR.text = regFeeAmount.toInt().toString()
        }else{
            binding.regsTaxPKR.text = "3000"
        }


        binding.fbr236kTax.text = fbr236kFiler
        val fbr236kT = fbr236kFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
        binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()


        binding.seller236CTax.text = fbr236CFiler
        val fbr236cT = fbr236CFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
        binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()








        sellerDropDown.setOnClickListener {
            sellerDropDown.showDropDown()
        }

        purchaserDropDown.setOnClickListener {
            purchaserDropDown.showDropDown()
        }


        binding.totalFINALAMOUNT.text = (stampDutyAmount + tMACorpAmount + regFeeAmount + fbr236kAmount + fbr236cAmount).toInt().toString()





// Check Boxes
        val headerCheckbox: CheckBox = binding.typesRadioBtn
        val checkboxesWithValues = mapOf(
            binding.stampDutyRadioBtn to stampDutyAmount.toInt(),
            binding.tmaCorpRadioBtn to tMACorpAmount.toInt(),
            binding.regsRadioBtn to regFeeAmount.toInt(),
            binding.fbr236kRadioBtn to fbr236kAmount.toInt(),
            binding.seller236CRadioBtn to fbr236cAmount.toInt(),
//            binding.sellerNOCRadioBtn to sellerNOCRateAmount.toInt(),
//            binding.transferFeeRadioBtn to transferFeeAmount.toInt(),
//            binding.wasiqaFeeRadioBtn to wasiqaFeeAmount.toInt()
        )

        var totalSum = 0 // Use Int for total sum

// Header Checkbox Logic
        headerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            checkboxesWithValues.keys.forEach { checkbox ->
                checkbox.isChecked = isChecked // Toggle all child checkboxes
            }

            // Update the total directly when header checkbox is toggled
            totalSum = if (isChecked) {
                checkboxesWithValues.values.sum() // Sum all integer values
            } else {
                0
            }

            binding.totalFINALAMOUNT.text = totalSum.toString() // Update UI
        }

// Individual Checkbox Logic
        checkboxesWithValues.forEach { (checkbox, value) ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    totalSum += value // Add the value when checkbox is checked
                } else {
                    totalSum -= value // Subtract the value when checkbox is unchecked
                }

                binding.totalFINALAMOUNT.text = totalSum.toString() // Update UI dynamically
            }
        }




    }
}