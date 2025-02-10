package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.InputType
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
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage3Binding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Page3Activity : AppCompatActivity() {

    private var tMACorpAmount: Double = 0.0
    private var stampDutyAmount: Double = 0.0
    private var regFeeAmount: Double = 0.0
    private var fbr236kAmount: Double = 0.0
    private var fbr236cAmount: Double = 0.0
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
        val marlaValue = intent.getFloatExtra("marlaValue", 0f)
        val sqftValue = intent.getIntExtra("sqftValue", 0)
        val coveredArea = intent.getIntExtra("coveredArea", 0)

        val totalDC = intent.getIntExtra("totalDC", 0)
        val totalFBR = intent.getIntExtra("totalFBR", 0)




// Create a string to display land area
        val landAreaStringBuilder = StringBuilder()

        if (kanalValue != 0) {
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
//        val fbr236kNonFiler = 6
//        val fbr236kLateFiler = 10.5
//
//        val fbr236CFiler = 3
//        val fbr236CNonFiler = 6
//        val fbr236CLateFiler = 10.5



//        Default Values upon Activity creation

        val stampDutyT = stampDuty.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        stampDutyAmount = totalDC.toDouble() * (stampDutyT / 100)
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
            regFeeAmount = 3000.0
        }


        binding.fbr236kTax.text = fbr236kFiler
        var fbr236kT = fbr236kFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
        binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()


        binding.seller236CTax.text = fbr236CFiler
        var fbr236cT = fbr236CFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
        fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
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
                    fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
                        updateTotalAmount()
                    }
                }
                "Late Filer" -> {
                    binding.seller236CTax.text = fbr236CLateFiler

                    // Perform actions for Late Filer
                    val fbr236cT = fbr236CLateFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
                        updateTotalAmount()
                    }

                }
                "Non-Filer" -> {

                    binding.seller236CTax.text = fbr236CNonFiler

                    // Perform actions for Filer
                    val fbr236cT = fbr236CNonFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236cAmount = totalFBR.toDouble() * (fbr236cT / 100)
                    binding.seller236CTaxPKR.text = fbr236cAmount.toInt().toString()

                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
                        updateTotalAmount()
                    }

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

                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
                        updateTotalAmount()
                    }


                }
                "Late Filer" -> {
                    binding.fbr236kTax.text = fbr236kLateFiler


                    val fbr236kT = fbr236kLateFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
                        updateTotalAmount()
                    }
                }
                "Non-Filer" -> {
                    binding.fbr236kTax.text = fbr236kNonFiler


                    val fbr236kT = fbr236kNonFiler.toDoubleOrNull() ?: 0.0 // Safely convert to Double
                    fbr236kAmount = totalFBR.toDouble() * (fbr236kT / 100)
                    binding.fbr236kTaxPKR.text = fbr236kAmount.toInt().toString()

                    if (binding.registryTransferDropdown.text.toString() == "برائے رجسٹری" || binding.registryTransferDropdown.text.toString() == "سوسائٹی ٹرانسفر" ) {
                        updateTotalAmount()
                    }

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


// Update stamp duty value
                    val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.regsTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.fbr236kTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.seller236CTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)

                    binding.totalFINALAMOUNT.text = totalSum.toString()

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


                    val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +

                            (binding.fbr236kTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.seller236CTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)

                    binding.totalFINALAMOUNT.text = totalSum.toString()
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



                    val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.regsTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +

                            (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                            (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)

                    binding.totalFINALAMOUNT.text = totalSum.toString()
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
            updateTotalAmount()
        }

        setupEditText(e7EditButton, e7EditText) { value ->
            transferFeeAmount = value
            updateTotalAmount()
        }

        setupEditText(wasiqaEditButton, wasiqaEditText) { value ->
            wasiqaFeeAmount = value
            updateTotalAmount()
        }


        
        
        




//        nocEditButton.setOnClickListener {
//            // Enable the EditText
//            nocEditText.isEnabled = true
//            nocEditText.isFocusable = true
//            nocEditText.isFocusableInTouchMode = true
//            nocEditText.inputType = InputType.TYPE_CLASS_NUMBER
//
//            // Immediately show the keyboard and request focus
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            nocEditText.post {
//                nocEditText.requestFocus()
//                imm.showSoftInput(nocEditText, InputMethodManager.SHOW_IMPLICIT)
//            }
//        }
//
//        nocEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                val nocInputValue = nocEditText.text.toString().toIntOrNull() ?: 0
//                nocAmount = nocInputValue
//
//                updateTotalAmount()
//
//                // Hide the keyboard
//                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(nocEditText.windowToken, 0)
//
//                // Disable the EditText
//                nocEditText.isEnabled = false
//                nocEditText.isFocusable = false
//                nocEditText.isFocusableInTouchMode = false
//
//                true
//            } else {
//                false
//            }
//        }
//
//        nocEditText.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                val nocInputValue = nocEditText.text.toString().toIntOrNull() ?: 0
//                nocAmount = nocInputValue
//
//                updateTotalAmount()
//
//                // Disable the EditText when it loses focus
//                nocEditText.isEnabled = false
//                nocEditText.isFocusable = false
//                nocEditText.isFocusableInTouchMode = false
//            }
//        }












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

        var totalSum = 0 // Use Int for total sum

        // Function to update the total sum and UI
        fun updateTotalSum() {
            totalSum = checkboxesWithValues
                .filter { (checkbox, _) -> checkbox.isChecked } // Filter checked checkboxes
                .values
                .sum() // Sum their values

            binding.totalFINALAMOUNT.text = totalSum.toString() // Update UI
        }

// Header Checkbox Logic
        headerCheckbox.setOnCheckedChangeListener { _, isChecked ->
            checkboxesWithValues.keys.forEach { checkbox ->
                checkbox.isChecked = isChecked // Toggle all child checkboxes
            }
            updateTotalSum() // Update the total sum and UI
        }

// Individual Checkbox Logic
        checkboxesWithValues.forEach { (checkbox, _) ->
            checkbox.setOnCheckedChangeListener { _, _ ->
                updateTotalSum() // Update the total sum and UI when any checkbox is toggled

                // Update header checkbox state based on child checkboxes
                headerCheckbox.isChecked = checkboxesWithValues.keys.all { it.isChecked }
            }
        }




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


    @SuppressLint("SetTextI18n")
    private fun updateTotalAmount() {
        val totalSum = (binding.stampDutyTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.tmaCorpTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.regsTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.fbr236kTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.seller236CTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.sellerNOCTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.transferFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0) +
                (binding.wasiqaFeeTaxPKR.text?.toString()?.toIntOrNull() ?: 0)

        binding.totalFINALAMOUNT.text = totalSum.toString()
    }

    private fun initializeDefaults() {
        val stampDuty =  PreferencesManager.getData("stampDutyEditTextInner")
        if (stampDuty.isEmpty()) {
            PreferencesManager.saveData("stampDutyEditTextInner", "1")
            PreferencesManager.saveData("tmaCorpEditTextInner", "1")
            PreferencesManager.saveData("regsFeeEditTextInner", "0.1")

            PreferencesManager.saveData("fbr236KFilerEditTextInner", "3")
            PreferencesManager.saveData("fbr236KNonfilerEditTextInner", "6")
            PreferencesManager.saveData("fbr236KLateFilerEditTextInner", "10.5")

            PreferencesManager.saveData("fbr236CFilerEditTextInner", "3")
            PreferencesManager.saveData("fbr236CNonfilerEditTextInner", "6")
            PreferencesManager.saveData("fbr236ClatefilerEditTextInner", "10.5")
        }
    }

}