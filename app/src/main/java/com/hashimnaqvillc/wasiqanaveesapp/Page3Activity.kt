package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage3Binding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Page3Activity : AppCompatActivity() {

    private var regFeeAmount: Double = 0.0

    private  var nocAmount: Int = 0
    private  var transferFeeAmount: Int = 0
    private  var wasiqaFeeAmount: Int = 0


    private lateinit var binding: ActivityPage3Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPage3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDefaults()



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

        initializeDefaults()
        setupCheckBoxes()
        updateTotalAmount()

    }

    @SuppressLint("SetTextI18n")
    private fun setupCheckBoxes() {





//        Get Dropdown selection from Page 2
        val selectedPropertyType = intent.getStringExtra("selectedPropertyType")
        val selectedPropertyArea = intent.getStringExtra("selectedPropertyArea")
        val selectedDistrict = intent.getStringExtra("selectedDistrict")

//        Get Land Area Values from Page 2
        val kanalValue = intent.getFloatExtra("kanalValue", 0f)
        val marlaValue = intent.getFloatExtra("marlaValue", 0f)
        val sqftValue = intent.getIntExtra("sqftValue", 0)
        val coveredArea = intent.getIntExtra("coveredArea", 0)

        val totalDC = intent.getIntExtra("totalDC", 0)
        val totalFBR = intent.getIntExtra("totalFBR", 0)




// Create a string to display land area
        val landAreaStringBuilder = StringBuilder()

        if (kanalValue != 0.0.toFloat()) {
            landAreaStringBuilder.append("$kanalValue Kanal, ")
        }
        if (marlaValue != 0.0.toFloat()) {
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

        Log.d("taxes", "$stampDuty, $tmaCorp, $regFee, $fbr236kFiler, $fbr236kNonFiler, $fbr236kLateFiler, $fbr236CFiler, $fbr236CNonFiler, $fbr236CLateFiler")



//        val stampDuty = 1
//        val tmaCorp = 1
//        val regFee =  0.1
//
//        val fbr236kFiler = 3
//        val fbr236kLateFiler = 6
//        val fbr236kNonFiler = 10.5
//
//        val fbr236CFiler = 3
//        val fbr236CLateFiler = 6
//        val fbr236CNonFiler = 10.5



//        Default Values upon Activity creation

        val stampDutyT = stampDuty.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val stampDutyAmount = totalDC.toDouble() * (stampDutyT / 100)
        binding.stampDutyTaxPKR.text = stampDutyAmount.toInt().toString()


        val tMACorpT = tmaCorp.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val tMACorpAmount = totalDC.toDouble() * (tMACorpT / 100)
        binding.tmaCorpTaxPKR.text = tMACorpAmount.toInt().toString()


        val regFeeT = regFee.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        regFeeAmount = tMACorpAmount * (regFeeT / 100)
        if (regFeeAmount >= 3000 ) {
            binding.regsTaxPKR.text = regFeeAmount.toInt().toString()
        }else{
            binding.regsTaxPKR.text = "3300"
            regFeeAmount = 3300.0
        }


        binding.fbr236kTax.text = fbr236kFiler
        val fbr236kT = fbr236kFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
        binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()


        binding.seller236CTax.text = fbr236CFiler
        val fbr236cT = fbr236CFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        val fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
        binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()








        

        binding.stampDutyTax.text = stampDuty
        binding.tmaCorpTax.text = tmaCorp
        binding.regsTax.text = regFee

        binding.fbr236kTax.text = fbr236kFiler
        binding.seller236CTax.text = fbr236CFiler

        binding.officeNamePg3.text = "Naqvi's Brothers"
        binding.officePhonePg3.text = " : 0333-4415786"




        binding.plraRow.visibility = View.GONE
        binding.plraSeparator.visibility = View.GONE

        binding.fbr236kRow.visibility = View.VISIBLE
        binding.fbrSeperator.visibility = View.VISIBLE
        binding.seller236CRow.visibility = View.VISIBLE
        binding.seller236CSeparator.visibility = View.VISIBLE





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
                    val fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

//                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
//                        updateTotalAmount()
//                    }
                    updateTotalAmount()
                }
                "Late Filer" -> {
                    binding.seller236CTax.text = fbr236CLateFiler

                    // Perform actions for Late Filer
                    val fbr236cT = fbr236CLateFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    val fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

//                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
//                        updateTotalAmount()
//                    }
                    updateTotalAmount()


                }
                "Non-Filer" -> {

                    binding.seller236CTax.text = fbr236CNonFiler

                    // Perform actions for Filer
                    val fbr236cT = fbr236CNonFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    val fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

//                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
//                        updateTotalAmount()
//                    }
                    updateTotalAmount()


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
                    val fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

//                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
//                        updateTotalAmount()
//                    }
                    updateTotalAmount()



                }
                "Late Filer" -> {
                    binding.fbr236kTax.text = fbr236kLateFiler


                    val fbr236kT = fbr236kLateFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    val fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

//                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
//                        updateTotalAmount()
//                    }
                    updateTotalAmount()
                }
                "Non-Filer" -> {
                    binding.fbr236kTax.text = fbr236kNonFiler


                    val fbr236kT = fbr236kNonFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    val fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

//                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
//                        updateTotalAmount()
//                    }
                    updateTotalAmount()

                }
            }
        }

        val registryOptions = mutableListOf("گفت ڈیڈ","برائے رجسٹری","سوسائٹی ٹرانسفر")
        val registryAdapter = ArrayAdapter(this, R.layout.custom_dropdown_item, registryOptions)
        binding.registryTransferDropdown.setAdapter(registryAdapter)

// Default stamp duty amount
        binding.registryTransferDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = registryOptions[position]

            when (selectedOption) {
                "برائے رجسٹری" -> {
                    // Show PLRA-related rows
                    binding.plraRow.visibility = View.VISIBLE
                    binding.plraSeparator.visibility = View.VISIBLE

                    // Show FBR-related rows
                    binding.fbr236kRow.visibility = View.VISIBLE
                    binding.fbrSeperator.visibility = View.VISIBLE
                    binding.seller236CRow.visibility = View.VISIBLE
                    binding.seller236CSeparator.visibility = View.VISIBLE

//                    if (stampDutyAmount == tMACorpAmount ) {
//                        binding.stampDutyTaxPKR.text = (defaultStampDuty.toInt() + 1000).toString()
//                        stampDutyAmount += 1000
//                    }

                    if (binding.stampDutyTaxPKR.text.toString().toInt() == binding.tmaCorpTaxPKR.text.toString().toInt() ) {
                        binding.stampDutyTaxPKR.text = (binding.stampDutyTaxPKR.text.toString().toInt() + 1000).toString()
                    }
                    Log.d("taxes", "Stamp Duty: ${binding.stampDutyTaxPKR.text}")



                    updateTotalAmount()
//// Update stamp duty value
//                    val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.regsTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.fbr236kTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.seller236CTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)
//
//                    binding.totalFINALAMOUNT.text = totalSum.toString()

                }

                "سوسائٹی ٹرانسفر" -> {
                    // Hide PLRA-related rows
                    binding.plraRow.visibility = View.GONE
                    binding.plraSeparator.visibility = View.GONE

                    // Show FBR-related rows
                    binding.fbr236kRow.visibility = View.VISIBLE
                    binding.fbrSeperator.visibility = View.VISIBLE
                    binding.seller236CRow.visibility = View.VISIBLE
                    binding.seller236CSeparator.visibility = View.VISIBLE

//                    if (stampDutyAmount > tMACorpAmount ) {
//                        binding.stampDutyTaxPKR.text = (stampDutyAmount.toInt() - 1000).toString()
//                        stampDutyAmount -= 1000
//                    }

                    if (binding.stampDutyTaxPKR.text.toString().toInt() > binding.tmaCorpTaxPKR.text.toString().toInt() ) {
                        binding.stampDutyTaxPKR.text = (binding.stampDutyTaxPKR.text.toString().toInt() - 1000).toString()
                    }

                    updateTotalAmount()
//
//                    val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//
//                            (binding.fbr236kTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.seller236CTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)
//
//                    binding.totalFINALAMOUNT.text = totalSum.toString()
                }

                "گفت ڈیڈ" -> {
                    // Show PLRA-related rows
                    binding.plraRow.visibility = View.VISIBLE
                    binding.plraSeparator.visibility = View.VISIBLE

                    // Hide FBR-related rows
                    binding.fbr236kRow.visibility = View.GONE
                    binding.fbrSeperator.visibility = View.GONE
                    binding.seller236CRow.visibility = View.GONE
                    binding.seller236CSeparator.visibility = View.GONE

//                    if (stampDutyAmount > tMACorpAmount ) {
//                        binding.stampDutyTaxPKR.text = (stampDutyAmount.toInt() - 1000).toString()
//                        stampDutyAmount -= 1000
//                    }

                    if (binding.stampDutyTaxPKR.text.toString().toInt() > binding.tmaCorpTaxPKR.text.toString().toInt() ) {
                        binding.stampDutyTaxPKR.text = (binding.stampDutyTaxPKR.text.toString().toInt() - 1000).toString()
                    }


                    updateTotalAmount()

//                    val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.regsTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//
//                            (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
//                            (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)
//
//                    binding.totalFINALAMOUNT.text = totalSum.toString()
                }
            }
        }


        binding.registryTransferDropdown.setOnClickListener {
            binding.registryTransferDropdown.showDropDown()
            binding.registryTransferDropdown.requestFocus()
        }


        val nocEditButton =  binding.sellerNOCEditBtn
        val nocEditText = binding.sellerNOCTaxPKR

        val e7EditButton =  binding.transferFeeEditBtn
        val e7EditText = binding.transferFeeTaxPKR

        val wasiqaEditButton =  binding.wasiqaFeeEditBtn
        val wasiqaEditText = binding.wasiqaFeeTaxPKR


        setupEditText(nocEditButton, nocEditText) { value ->
            nocAmount = value
        }

        setupEditText(e7EditButton, e7EditText) { value ->
            transferFeeAmount = value
        }

        setupEditText(wasiqaEditButton, wasiqaEditText) { value ->
            wasiqaFeeAmount = value
//            updateTotalCharges()

        }











        sellerDropDown.setOnClickListener {
            sellerDropDown.showDropDown()
        }

        purchaserDropDown.setOnClickListener {
            purchaserDropDown.showDropDown()
        }



        val finalSumDefault = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.fbr236kTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.seller236CTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)

        binding.totalFINALAMOUNT.text = finalSumDefault.toString()


        val headerCheckbox: CheckBox = binding.typesRadioBtn
        val checkboxesWithValues: Map<CheckBox, Int> = mapOf(
            binding.stampDutyRadioBtn to (binding.stampDutyTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.tmaCorpRadioBtn to (binding.tmaCorpTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.regsRadioBtn to (binding.regsTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.fbr236kRadioBtn to (binding.fbr236kTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.seller236CRadioBtn to (binding.seller236CTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.sellerNOCRadioBtn to (binding.sellerNOCTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.transferFeeRadioBtn to (binding.transferFeeTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.wasiqaFeeRadioBtn to (binding.wasiqaFeeTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0)
        )



        // Function to handle TextWatcher for dynamic value updates
        fun setupTextWatchers() {
            val watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateTotalAmount() // Recalculate total sum whenever text changes
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            // Add TextWatcher to all EditText fields
            binding.stampDutyTaxPKR.addTextChangedListener(watcher)
            binding.tmaCorpTaxPKR.addTextChangedListener(watcher)
            binding.regsTaxPKR.addTextChangedListener(watcher)
            binding.fbr236kTaxPKR.addTextChangedListener(watcher)
            binding.seller236CTaxPKR.addTextChangedListener(watcher)
            binding.sellerNOCTaxPKR.addTextChangedListener(watcher)
            binding.transferFeeTaxPKR.addTextChangedListener(watcher)
            binding.wasiqaFeeTaxPKR.addTextChangedListener(watcher)
        }




        var isUpdatingCheckboxes = false // Member variable in your class
        val originalValues = mutableMapOf<CheckBox, String>()


// Set up the checkboxes' logic (header checkbox and individual checkboxes)
        headerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (!isUpdatingCheckboxes) { // Only execute if not programmatically updating
                isUpdatingCheckboxes = true

                // Toggle all child checkboxes
                checkboxesWithValues.keys.forEach { checkbox ->
                    checkbox.isChecked = isChecked

                    if (!isChecked) {
                        // Store original value before setting to "0"
                        when (checkbox) {
                            binding.stampDutyRadioBtn -> {
                                originalValues[checkbox] = binding.stampDutyTaxPKR.text.toString()
                                binding.stampDutyTaxPKR.setText("0")
                            }
                            binding.tmaCorpRadioBtn -> {
                                originalValues[checkbox] = binding.tmaCorpTaxPKR.text.toString()
                                binding.tmaCorpTaxPKR.setText("0")
                            }
                            binding.regsRadioBtn -> {
                                originalValues[checkbox] = binding.regsTaxPKR.text.toString()
                                binding.regsTaxPKR.setText("0")
                            }
                            binding.fbr236kRadioBtn -> {
                                originalValues[checkbox] = binding.fbr236kTaxPKR.text.toString()
                                binding.fbr236kTaxPKR.setText("0")
                            }
                            binding.seller236CRadioBtn -> {
                                originalValues[checkbox] = binding.seller236CTaxPKR.text.toString()
                                binding.seller236CTaxPKR.setText("0")
                            }
                            binding.sellerNOCRadioBtn -> {
                                originalValues[checkbox] = binding.sellerNOCTaxPKR.text.toString()
                                binding.sellerNOCTaxPKR.setText("0")
                            }
                            binding.transferFeeRadioBtn -> {
                                originalValues[checkbox] = binding.transferFeeTaxPKR.text.toString()
                                binding.transferFeeTaxPKR.setText("0")
                            }
                            binding.wasiqaFeeRadioBtn -> {
                                originalValues[checkbox] = binding.wasiqaFeeTaxPKR.text.toString()
                                binding.wasiqaFeeTaxPKR.setText("0")
                            }
                        }
                    } else {
                        // Restore original value when checked
                        originalValues[checkbox]?.let { originalValue ->
                            when (checkbox) {
                                binding.stampDutyRadioBtn -> binding.stampDutyTaxPKR.setText(originalValue)
                                binding.tmaCorpRadioBtn -> binding.tmaCorpTaxPKR.setText(originalValue)
                                binding.regsRadioBtn -> binding.regsTaxPKR.setText(originalValue)
                                binding.fbr236kRadioBtn -> binding.fbr236kTaxPKR.setText(originalValue)
                                binding.seller236CRadioBtn -> binding.seller236CTaxPKR.setText(originalValue)
                                binding.sellerNOCRadioBtn -> binding.sellerNOCTaxPKR.setText(originalValue)
                                binding.transferFeeRadioBtn -> binding.transferFeeTaxPKR.setText(originalValue)
                                binding.wasiqaFeeRadioBtn -> binding.wasiqaFeeTaxPKR.setText(originalValue)
                            }
                        }
                    }
                }

                updateTotalAmount() // Recalculate the total sum after toggling
                isUpdatingCheckboxes = false
            }
        }






// Set up individual checkboxes to update total when toggled
        checkboxesWithValues.forEach { (checkbox, _) ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (!isUpdatingCheckboxes) { // Only execute if not programmatically updating

                    if (!isChecked) {
                        // Store original value before setting to "0"
                        when (checkbox) {
                            binding.stampDutyRadioBtn -> {
                                originalValues[checkbox] = binding.stampDutyTaxPKR.text.toString()
                                binding.stampDutyTaxPKR.setText("0")
                            }
                            binding.tmaCorpRadioBtn -> {
                                originalValues[checkbox] = binding.tmaCorpTaxPKR.text.toString()
                                binding.tmaCorpTaxPKR.setText("0")
                            }
                            binding.regsRadioBtn -> {
                                originalValues[checkbox] = binding.regsTaxPKR.text.toString()
                                binding.regsTaxPKR.setText("0")
                            }
                            binding.fbr236kRadioBtn -> {
                                originalValues[checkbox] = binding.fbr236kTaxPKR.text.toString()
                                binding.fbr236kTaxPKR.setText("0")
                            }
                            binding.seller236CRadioBtn -> {
                                originalValues[checkbox] = binding.seller236CTaxPKR.text.toString()
                                binding.seller236CTaxPKR.setText("0")
                            }
                            binding.sellerNOCRadioBtn -> {
                                originalValues[checkbox] = binding.sellerNOCTaxPKR.text.toString()
                                binding.sellerNOCTaxPKR.setText("0")
                            }
                            binding.transferFeeRadioBtn -> {
                                originalValues[checkbox] = binding.transferFeeTaxPKR.text.toString()
                                binding.transferFeeTaxPKR.setText("0")
                            }
                            binding.wasiqaFeeRadioBtn -> {
                                originalValues[checkbox] = binding.wasiqaFeeTaxPKR.text.toString()
                                binding.wasiqaFeeTaxPKR.setText("0")
                            }
                        }
                    } else {
                        // Restore original value when checked
                        originalValues[checkbox]?.let { originalValue ->
                            when (checkbox) {
                                binding.stampDutyRadioBtn -> binding.stampDutyTaxPKR.setText(originalValue)
                                binding.tmaCorpRadioBtn -> binding.tmaCorpTaxPKR.setText(originalValue)
                                binding.regsRadioBtn -> binding.regsTaxPKR.setText(originalValue)
                                binding.fbr236kRadioBtn -> binding.fbr236kTaxPKR.setText(originalValue)
                                binding.seller236CRadioBtn -> binding.seller236CTaxPKR.setText(originalValue)
                                binding.sellerNOCRadioBtn -> binding.sellerNOCTaxPKR.setText(originalValue)
                                binding.transferFeeRadioBtn -> binding.transferFeeTaxPKR.setText(originalValue)
                                binding.wasiqaFeeRadioBtn -> binding.wasiqaFeeTaxPKR.setText(originalValue)
                            }
                        }
                    }

                    updateTotalAmount() // Recalculate total sum when any checkbox is toggled

                    isUpdatingCheckboxes = true // Set flag before updating headerCheckbox
                    // Update the header checkbox state based on child checkboxes
                    headerCheckbox.isChecked = checkboxesWithValues.keys.all { it.isChecked }
                    isUpdatingCheckboxes = false // Reset flag after updating headerCheckbox
                }
            }
        }

        setupTextWatchers()












    }

    private fun setupEditText(button: View, editText: EditText, onValueSet: (Int) -> Unit) {
        val enableAndShowKeyboard: () -> Unit = {
            // Enable the EditText
            editText.isEnabled = true
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
            editText.inputType = InputType.TYPE_CLASS_NUMBER

            // Immediately show the keyboard and request focus
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            editText.post {
                editText.requestFocus()
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        button.setOnClickListener {
            enableAndShowKeyboard()
        }

        editText.setOnClickListener {
            enableAndShowKeyboard()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputValue = editText.text.toString().toIntOrNull() ?: 0
                onValueSet(inputValue)

                // Hide the keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                true

            } else {
                false
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val inputValue = editText.text.toString().toIntOrNull() ?: 0
                onValueSet(inputValue)
            }
        }
    }


    private fun updateTotalAmount() {

        var totalSum2 = 0

        // Sum values only for checked checkboxes, and consider visibility condition
        val checkboxesWithValues: Map<CheckBox, Int> = mapOf(
            binding.stampDutyRadioBtn to (binding.stampDutyTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.tmaCorpRadioBtn to (binding.tmaCorpTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),

            // Only add value if the corresponding row is visible
            binding.regsRadioBtn to (if (binding.plraRow.visibility == View.VISIBLE) {
                binding.regsTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0
            } else 0),

            binding.fbr236kRadioBtn to (if (binding.fbr236kRow.visibility == View.VISIBLE) {
                binding.fbr236kTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0
            } else 0),

            binding.seller236CRadioBtn to (if (binding.seller236CRow.visibility == View.VISIBLE) {
                binding.seller236CTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0
            } else 0),

            binding.sellerNOCRadioBtn to (binding.sellerNOCTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.transferFeeRadioBtn to (binding.transferFeeTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0),
            binding.wasiqaFeeRadioBtn to (binding.wasiqaFeeTaxPKR.text?.toString()?.trim()?.toIntOrNull() ?: 0)


        )

        // Now iterate and sum only the checked checkboxes
        checkboxesWithValues.forEach { (checkbox, value) ->
            if (checkbox.isChecked) {
                totalSum2 += value
            }
        }

        // Update total in the UI
        binding.totalFINALAMOUNT.text = totalSum2.toString()
    }

    private fun initializeDefaults() {
        val stampDuty =  PreferencesManager.getData("stampDutyEditTextInner")
        if (stampDuty.isEmpty()) {
            PreferencesManager.saveData("stampDutyEditTextInner", "1")
            PreferencesManager.saveData("tmaCorpEditTextInner", "1")
            PreferencesManager.saveData("regsFeeEditTextInner", "0.1")

            PreferencesManager.saveData("fbr236KFilerEditTextInner", "1.5")
            PreferencesManager.saveData("fbr236KLateFilerEditTextInner", "4.5")
            PreferencesManager.saveData("fbr236KNonfilerEditTextInner", "10.5")

            PreferencesManager.saveData("fbr236CFilerEditTextInner", "4.5")
            PreferencesManager.saveData("fbr236ClatefilerEditTextInner", "7.5")
            PreferencesManager.saveData("fbr236CNonfilerEditTextInner", "11.5")
        }
    }





}