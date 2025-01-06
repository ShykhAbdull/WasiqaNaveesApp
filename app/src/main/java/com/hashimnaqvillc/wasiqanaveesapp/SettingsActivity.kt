package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

private lateinit var districtList: MutableList<String>
private lateinit var townList: MutableList<String>
private lateinit var areaList: MutableList<String>

private lateinit var districtAdapter: ArrayAdapter<String>
private lateinit var townAdapter: ArrayAdapter<String>
private lateinit var areaAdapter: ArrayAdapter<String>


private lateinit var binding: ActivitySettingsBinding

@SuppressLint("ClickableViewAccessibility", "InflateParams")
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
binding = ActivitySettingsBinding.inflate(layoutInflater)
setContentView(binding.root)

//    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)



//   Logic for showing and Hiding the Views on Settings Activity

val settingIcon = findViewById<ImageButton>(R.id.nav_settings_icon)
settingIcon.visibility = View.GONE

val dateMonth = findViewById<TextView>(R.id.date_month_day)
dateMonth.visibility = View.GONE

val dateYear = findViewById<TextView>(R.id.date_year)
dateYear.visibility = View.GONE

val wasiqaLogo = findViewById<ImageView>(R.id.nav_wasiqa_logo)
wasiqaLogo.visibility = View.GONE

val settingText = findViewById<TextView>(R.id.nav_settings_text)
settingText.visibility = View.VISIBLE

val profileEditBtn = findViewById<Button>(R.id.profile_edit_btn)
profileEditBtn.visibility = View.VISIBLE

profileEditBtn.setOnClickListener {
    val intent = Intent(this, ProfileActivtySettings::class.java)
    startActivity(intent)
}

val backBtn = findViewById<ImageButton>(R.id.nav_back)
backBtn.setOnClickListener {
    finish()
}

//        Logic for District DropDownClick
val districtDropDown = binding.districtDropdown
    districtDropDown.setOnClickListener {
        binding.districtDropdown.dismissDropDown()
        binding.districtDropdown.showDropDown()
        binding.districtDropdown.requestFocus()
    }

    districtDropDown.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            binding.districtDropdown.showDropDown()
            binding.districtIconContainer.visibility = View.VISIBLE
            binding.townIconContainer.visibility = View.GONE
            binding.propertyAreaIconContainer.visibility = View.GONE
        }
    }

    binding.townDropdown.setOnClickListener {
        if (binding.districtDropdown.text.toString().isNotEmpty()) {
            // Show the dropdown only if a district is selected
            binding.townDropdown.dismissDropDown()
            binding.townDropdown.showDropDown()
            binding.townDropdown.requestFocus()
        } else {
            Toast.makeText(this, "Please select a district first", Toast.LENGTH_SHORT).show()
        }
    }

    binding.townDropdown.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus && binding.districtDropdown.text.toString().isNotEmpty()) {
            binding.townDropdown.showDropDown()
            binding.districtIconContainer.visibility = View.GONE
            binding.townIconContainer.visibility = View.VISIBLE
            binding.propertyAreaIconContainer.visibility = View.GONE
        } else if (hasFocus) {
            binding.townDropdown.clearFocus() // Prevent focus if no district is selected
            Toast.makeText(this, "Please select a district first", Toast.LENGTH_SHORT).show()
        }
    }

    binding.propertyAreaDropdown.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus && binding.townDropdown.text.toString().isNotEmpty()) {
            binding.propertyAreaDropdown.showDropDown()
            binding.townIconContainer.visibility = View.GONE
            binding.districtIconContainer.visibility = View.GONE
            binding.propertyAreaIconContainer.visibility = View.VISIBLE
        } else if (hasFocus) {
            binding.propertyAreaDropdown.clearFocus() // Prevent focus if no town is selected
            Toast.makeText(this, "Please select a town first", Toast.LENGTH_SHORT).show()
        }
    }
    binding.propertyAreaDropdown.setOnClickListener {
    binding.propertyAreaDropdown.showDropDown()
}










    // Optional: Disable default focus stealing by the clear_text icon
    binding.landTypeDropdown.setOnFocusChangeListener { _, hasFocus ->

        if (hasFocus) {
            binding.landTypeDropdown.showDropDown()
        }
    }

//    // Optional: Disable default focus stealing by the clear_text icon
//    binding.propertyTypeDropdown.setOnFocusChangeListener { _, hasFocus ->
//
//        if (hasFocus) {
//            binding.propertyTypeDropdown.showDropDown()
//        }
//    }

//        Focus from District ---> Town
districtDropDown.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
        // Move focus to the next view (e.g., another dropdown)
        binding.townDropdown.requestFocus()

        true // Return true to consume the action
    } else {
        false
    }
}

//        Focus from Town ---> Property Area
binding.townDropdown.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
        // Move focus to the next view (e.g., another dropdown)
        binding.propertyAreaDropdown.requestFocus()

        true // Return true to consume the action
    } else {
        false
    }
}

//        Focus from Property Area ---> Land Type
binding.propertyAreaDropdown.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
        // Move focus to the next view (e.g., another dropdown)
        binding.landTypeDropdown.requestFocus()
        true // Return true to consume the action
    } else {
        false
    }
}

////        Focus from Land Type ---> Khasra Number
//
binding.landTypeDropdown.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
        // Move focus to the next view (e.g., another dropdown)
        binding.khasraEditText.requestFocus()
        true // Return true to consume the action
    } else {
        false
    }
}

binding.khasraEditText.setOnEditorActionListener { _, actionId, _ ->
    // Close the keyboard
    // Return true to consume the action
    actionId == EditorInfo.IME_ACTION_DONE
}


// Optional: Customize the keyboard action to show "Next" instead of "Done"

binding.districtDropdown.imeOptions = EditorInfo.IME_ACTION_NEXT
binding.districtDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.townDropdown.imeOptions = EditorInfo.IME_ACTION_NEXT
binding.townDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.propertyAreaDropdown.imeOptions = EditorInfo.IME_ACTION_NEXT
binding.propertyAreaDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.landTypeDropdown.imeOptions = EditorInfo.IME_ACTION_NEXT
binding.landTypeDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.khasraEditText.imeOptions = EditorInfo.IME_ACTION_DONE
binding.khasraEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.khasraEditText.imeOptions = EditorInfo.IME_ACTION_DONE
binding.khasraEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)










    val districtAddIcon = binding.addDestrictIcon

// Map to hold district-to-town associations
val districtTownMap = mutableMapOf<String, MutableList<String>>()

// Sample data for the dropdown
districtList = PreferencesManager.getDropdownList().toMutableList()

    districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
// Set adapter to AutoCompleteTextView
districtDropDown.setAdapter(districtAdapter)
// Start searching after typing one character
districtDropDown.threshold = 1

// Apply filter and count the visible items
val itemCount = districtAdapter.count // Gets the current filtered item count
if (itemCount <= 3) {
    districtDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    Log.d("DistrictList", "Updated district list: $districtList")
} else {
    // Set a fixed height for more than 3 items
    districtDropDown.dropDownHeight =
        (districtDropDown.context.resources.displayMetrics.density * 150).toInt()
    Log.d("DistrictList", "Updated district list: $districtList")
}





// Set up a listener to track the filtered results and dynamically set the dropdown height
districtDropDown.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        // Apply filter and count the visible items
        districtAdapter.filter.filter(s) {
            val districtVisibleItemCount = districtAdapter.count // Gets the current filtered item count

            if (districtVisibleItemCount <= 3) {
                districtDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                // Set a fixed height for more than 3 items
                districtDropDown.dropDownHeight =
                    (districtDropDown.context.resources.displayMetrics.density * 150).toInt()
            }
        }
    }
})

// TownDropdown setup
val townDropDown = binding.townDropdown // ID of AutoCompleteTextView for towns
val townAreaMap = mutableMapOf<String, MutableList<String>>()
    townList = mutableListOf()
townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
townDropDown.setAdapter(townAdapter)
townDropDown.threshold = 1


// Apply filter and count the visible items
val townItemCount = townAdapter.count // Gets the current filtered item count
if (townItemCount <= 3) {
    townDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    Log.d("TownList", "Updated town list: $townList")

} else {
    // Set a fixed height for more than 3 items
    townDropDown.dropDownHeight = (townDropDown.context.resources.displayMetrics.density * 150).toInt()
    Log.d("TownList", "Updated town list: $townList")


}


// Set up a listener to track the filtered results and dynamically set the dropdown height
townDropDown.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        // Apply filter and count the visible items
        townAdapter.filter.filter(s) {
            val townVisibleItemCount = townAdapter.count // Gets the current filtered item count
            if (townVisibleItemCount <= 3) {
                townDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                // Set a fixed height for more than 3 items
                townDropDown.dropDownHeight =
                    (townDropDown.context.resources.displayMetrics.density * 150).toInt()
            }
        }
    }
})


    // Area Dropdown setup
    val areaDropDown = binding.propertyAreaDropdown // ID of AutoCompleteTextView for Area
    areaList = mutableListOf()
    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
    areaDropDown.setAdapter(areaAdapter)
    areaDropDown.threshold = 1
// On Town Selection

    townDropDown.setOnItemClickListener { _, _, _, _ ->
        val selectedTown = townDropDown.text.toString()

        // Clear and reset areas
        val areas = townAreaMap[selectedTown] ?: mutableListOf()
        areaList = areas
        areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
        areaDropDown.setAdapter(areaAdapter)
        areaDropDown.text.clear() // Clear selected area

        Log.d("TownSelection", "Selected town: $selectedTown, areas: $areas")
    }


// Apply filter and count the visible items
    val areaItemCount = areaAdapter.count // Gets the current filtered item count
    if (areaItemCount <= 3) {
        areaDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        Log.d("AreaList", "Updated area list: $areaList")

    } else {
        // Set a fixed height for more than 3 items
        areaDropDown.dropDownHeight =
            (areaDropDown.context.resources.displayMetrics.density * 150).toInt()
        Log.d("AreaList", "Updated area list: $areaList")


    }


// Set up a listener to track the filtered results and dynamically set the dropdown height
    areaDropDown.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Apply filter and count the visible items
            areaAdapter.filter.filter(s) {
                val areaVisibleItemCount = areaAdapter.count // Gets the current filtered item count
                if (areaVisibleItemCount <= 3) {
                    areaDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    // Set a fixed height for more than 3 items
                    areaDropDown.dropDownHeight =
                        (areaDropDown.context.resources.displayMetrics.density * 150).toInt()
                }
            }
        }
    })


    districtDropDown.setOnItemClickListener { _, _, _, _ ->
        val selectedDistrict = districtDropDown.text.toString()

        // Clear and reset towns and areas
        val towns = districtTownMap[selectedDistrict] ?: mutableListOf()
        townList = towns
        townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
        townDropDown.setAdapter(townAdapter)
        townDropDown.text.clear() // Clear selected town
        areaDropDown.text.clear() // Clear selected area

        Log.d("DistrictSelection", "Selected district: $selectedDistrict, towns: $towns, areas cleared.")
    }






//    When the district text clears all other field text clear as well
    binding.districtInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        districtDropDown.text.clear()
        townDropDown.text.clear()
        areaDropDown.text.clear()
        // Request focus back to the district dropdown
        districtDropDown.requestFocus()
    }

    //    When the town text clears all other field text clear as well
    binding.townInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        townDropDown.text.clear()
        areaDropDown.text.clear()
        // Request focus back to the district dropdown
        townDropDown.requestFocus()

    }






//        Logic for Adding new district/town from the user and saving that in their adapter

districtAddIcon.setOnClickListener {

        // Inflate the custom add alert view for the dialog
        val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
        val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
        val addInputEditText = dialogView.findViewById<EditText>(R.id.AddinputEditText)

        // Set the message text
        addMessage.text = "Add a New District"
        // Set the Edit Text Hint
    addInputEditText.hint = "Enter new District Name"

    // Create a confirmation dialog to add a new district
    val addDistrictDialog = MaterialAlertDialogBuilder(this)
        .setView(dialogView)
        .setPositiveButton("Add") { _, _ ->
            val newDistrictName = addInputEditText.text.toString().trim()

            when {
                newDistrictName.isEmpty() -> {
                    Toast.makeText(this, "District name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                districtList.contains(newDistrictName) -> {
                    Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Add the new district to the list
                    districtDropDown.setText(newDistrictName, false)
                    districtDropDown.setSelection(newDistrictName.length) // Move cursor to the end


                    districtList.add(newDistrictName)

                    townDropDown.text.clear() // Clear dropdown text.
                    areaDropDown.text.clear() // Clear dropdown text.

                    // Refresh adapter by recreating it
                    districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                    districtDropDown.setAdapter(districtAdapter)

                    // Clear and reset towns and areas
                    val towns = districtTownMap[newDistrictName] ?: mutableListOf()
                    townList = towns
                    townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                    townDropDown.setAdapter(townAdapter)
                    townDropDown.text.clear() // Clear selected town

//                    Adding in as SharedPreference
                    PreferencesManager.saveDropdownList(districtList)

                    Log.d("DistrictList", "Updated district list: $districtList")
                    Toast.makeText(this, "District added successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
        .setNegativeButton("Cancel") { _, _ -> }
        .create() // Call create() here only once

// Show the dialog
    addDistrictDialog.setOnShowListener {
        val positiveButtonAdd = addDistrictDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButtonAdd = addDistrictDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        // Load the custom font
        val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

        // Apply the font to the buttons
        positiveButtonAdd.typeface = customFont
        negativeButtonAdd.typeface = customFont

        // Optional: Set text color or style
        positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green)) // For "Add"
        positiveButtonAdd.textSize = 16f
        negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey)) // For "Cancel"
        negativeButtonAdd.textSize = 16f
    }
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    addInputEditText.requestFocus()

    // Use a Handler to ensure the keyboard opens after the dialog is fully visible
    addInputEditText.postDelayed({
        imm.showSoftInput(addInputEditText, InputMethodManager.SHOW_IMPLICIT)
    }, 100) // Delay by 100ms

    addDistrictDialog.show()
}




// Logic for Adding New Town

val townAddIcon = binding.addTownIcon // Assuming town add icon ID

    townAddIcon.setOnClickListener {
    val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
    val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
    val addInputEditText = dialogView.findViewById<EditText>(R.id.AddinputEditText)
    val selectedDistrict = districtDropDown.text.toString().trim()

    // Set the Edit Text Hint
    addInputEditText.hint = "Enter new Town Name"

    if (selectedDistrict.isEmpty()) {
        // Close the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        // Show Toast message
        Toast.makeText(this, "Select the district first", Toast.LENGTH_SHORT).show()
    } else {


        // Set the message text
        addMessage.text = "Add a Town for $selectedDistrict"

        // Create a confirmation dialog to add a new town
        val addTownDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val newTownName = addInputEditText.text.toString().trim()
                    when {

                    newTownName.isEmpty() -> {
                        Toast.makeText(
                            this,
                            "Town name cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        // Add the new town to the district's list in the map
                        val towns =
                            districtTownMap.getOrPut(selectedDistrict) { mutableListOf() }
                        if (towns.contains(newTownName)) {
                            Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT)
                                .show()
                        } else {

                            townDropDown.setText(newTownName, false)
                            townDropDown.setSelection(newTownName.length) // Move cursor to the end
                            towns.add(newTownName)
                            // Update the town list and adapter for the current district
                            townList = towns
                            townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                            townDropDown.setAdapter(townAdapter)

                            // Clear and reset areas
                            val areas = townAreaMap[newTownName] ?: mutableListOf()
                            areaList = areas
                            areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                            areaDropDown.setAdapter(areaAdapter)
                            areaDropDown.text.clear() // Clear area dropdown


                            Toast.makeText(
                                this,
                                "Town added successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Save the updated districtTownMap
                            PreferencesManager.saveDistrictTownMap(districtTownMap)
                        }
                        Log.d(
                            "TownList",
                            "Updated town list for $selectedDistrict: $townList"
                        )
                    }
                }
            }
        .setNegativeButton("Cancel") { _, _ -> }
        .create()

        // Change font style and color
        addTownDialog.setOnShowListener {
            val positiveButtonAdd = addTownDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButtonAdd = addTownDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Load the custom font
            val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

            // Apply the font to the buttons
            positiveButtonAdd.typeface = customFont
            negativeButtonAdd.typeface = customFont

            // Optional: Set text color or style
            positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green)) // For "Add"
            positiveButtonAdd.textSize = 16f
            negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey)) // For "Cancel"
            negativeButtonAdd.textSize = 16f
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        addInputEditText.requestFocus()

        // Use a Handler to ensure the keyboard opens after the dialog is fully visible
        addInputEditText.postDelayed({
            imm.showSoftInput(addInputEditText, InputMethodManager.SHOW_IMPLICIT)
        }, 100) // Delay by 100ms

    addTownDialog.show()
    }
}


    // Logic for Adding New Property Area

    val areaAddIcon = binding.addAreaIcon // Assuming town add icon ID
    areaAddIcon.setOnClickListener {
        val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
        val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
        val addInputEditText = dialogView.findViewById<EditText>(R.id.AddinputEditText)
        // Set the Edit Text Hint
        addInputEditText.hint = "Enter new Property Area"

        val selectedTown = townDropDown.text.toString().trim()
        if (selectedTown.isEmpty()) {
            // Close the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            // Show Toast message
            Toast.makeText(this, "Select the Town first", Toast.LENGTH_SHORT).show()
        }
        else
        {

// Set the message text
            addMessage.text = "Add Property Area for $selectedTown"


            // Create a confirmation dialog to add a new town
            val addAreaDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val newAreaName = addInputEditText.text.toString().trim()
                    when {
                        newAreaName.isEmpty() -> {
                            Toast.makeText(
                                this,
                                "Property Area cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // Add the new area to the town's list in the map
                            val area = townAreaMap.getOrPut(selectedTown) { mutableListOf() }
                            if (area.contains(newAreaName)) {
                                Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                area.add(newAreaName) // Add the new area to the list

                                // Update the area list and adapter for the current town
                                areaList = area
                                areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                                areaDropDown.setAdapter(areaAdapter)

                                // Set the new area as the selected text
                                areaDropDown.setText(newAreaName, false)
                                areaDropDown.setSelection(newAreaName.length)

                                Toast.makeText(
                                    this,
                                    "Property Area added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            Log.d("AreaList", "Updated area list for $selectedTown: $areaList")
                        }
                    }
                }

                .setNegativeButton("Cancel") { _, _ -> }
                .create()

            // Change font style and color
            addAreaDialog.setOnShowListener {
                val positiveButtonAdd = addAreaDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButtonAdd = addAreaDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // Load the custom font
                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

                // Apply the font to the buttons
                positiveButtonAdd.typeface = customFont
                negativeButtonAdd.typeface = customFont

                // Optional: Set text color or style
                positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green)) // For "Add"
                positiveButtonAdd.textSize = 16f
                negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey)) // For "Cancel"
                negativeButtonAdd.textSize = 16f
            }

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            addInputEditText.requestFocus()

            // Use a Handler to ensure the keyboard opens after the dialog is fully visible
            addInputEditText.postDelayed({
                imm.showSoftInput(addInputEditText, InputMethodManager.SHOW_IMPLICIT)
            }, 100) // Delay by 100ms

            addAreaDialog.show()
        }
    }



//        --------------- Add district/Town/Area Logic Above-------------


// ============= Edit District/Town Logic Below ================


val districtEditIcon = binding.editDistrictIcon

districtEditIcon.setOnClickListener {
    val selectedDistrict = districtDropDown.text.toString().trim()

    if (selectedDistrict.isNotEmpty() && districtList.contains(selectedDistrict)) {
        // Inflate the custom edit alert view for the dialog
        val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
        val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
        val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)

        // Set the Edit Text Hint
        inputEditText.hint = "Edit District Name"

        val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)

        // Set the message text
        editMessage.text = "Edit the name of the selected district"

        // Set the EditText initial value
        inputEditText.setText(selectedDistrict)
        inputEditText.setSelection(selectedDistrict.length) // Move cursor to the end of the text

        // Set the title dynamically
        centeredTitle.text = "District   ''$selectedDistrict''"

        // Create a confirmation dialog to edit the selected district
        val editDistrictDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newDistrictName = inputEditText.text.toString().trim()
                when {
                    newDistrictName.isEmpty() -> {
                        Toast.makeText(this, "New district name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    newDistrictName == selectedDistrict -> {
                        Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                    }
                    districtList.contains(newDistrictName) -> {
                        Toast.makeText(this, "New district name already exists", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Update the district name in the list
                        val index = districtList.indexOf(selectedDistrict)
                        val oldDistrictName = districtList[index]
                        Log.d("newDistrictList", "Old district name : $oldDistrictName")

                        districtList[index] = newDistrictName

                        districtDropDown.setText(newDistrictName, false)
                        districtDropDown.setSelection(newDistrictName.length) // Move cursor to the end

                        townDropDown.text.clear() // Clear dropdown text.
                        areaDropDown.text.clear() // Clear dropdown text.


                        // Refresh adapter by recreating it
                        districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                        districtDropDown.setAdapter(districtAdapter)



                        Log.d("newDistrictList", "After district list: $newDistrictName")
                        Log.d("townList", "After town list: $townList")
                        Toast.makeText(this, "District updated successfully", Toast.LENGTH_SHORT).show()


                        if (oldDistrictName != newDistrictName && districtTownMap.containsKey(oldDistrictName))
                        {
                            // Retrieve towns associated with the old district name
                            val towns = districtTownMap.remove(oldDistrictName)

                            // Update the map with the new district name and its towns
                            if (towns != null) {
                                districtTownMap[newDistrictName] = towns
                            }

                            // Optionally, update the selected district in the UI if this is the currently selected district
                            if (districtDropDown.text.toString() == oldDistrictName) {
                                districtDropDown.setText(newDistrictName, false)
                            }

                        }


//                    Adding in as SharedPreference
                        PreferencesManager.saveDropdownList(districtList)

                    }
                }
            }


            .setNegativeButton("Cancel") { _, _ -> }
            .create() // Call create() here only once

        // Show the dialog
        editDistrictDialog.setOnShowListener {
            val positiveButtonEdit = editDistrictDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButtonEdit = editDistrictDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Load the custom font
            val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

            // Apply the font to the buttons
            positiveButtonEdit.typeface = customFont
            negativeButtonEdit.typeface = customFont

            // Optional: Set text color or style
            positiveButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_green)) // For "Save"
            positiveButtonEdit.textSize = 16f
            negativeButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
            negativeButtonEdit.textSize = 16f

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputEditText.requestFocus()

            // Use a Handler to ensure the keyboard opens after the dialog is fully visible
            inputEditText.postDelayed({
                imm.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
            }, 100) // Delay by 100ms

        }
        editDistrictDialog.show()
    } else {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        Toast.makeText(this, "Please select a district to edit", Toast.LENGTH_SHORT).show()
    }
}


    val townEditIcon = binding.editTownIcon
    townEditIcon.setOnClickListener {
        val selectedTown = townDropDown.text.toString().trim()

        if (selectedTown.isNotEmpty() && townList.contains(selectedTown)) {
            // Inflate the custom edit alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
            val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
            val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)
            // Set the Edit Text Hint
            inputEditText.hint = "Edit Town Name"
            val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)

            // Set the message text
            editMessage.text = "Edit the name of the selected town"

            // Set the EditText initial value
            inputEditText.setText(selectedTown)
            inputEditText.setSelection(selectedTown.length) // Move cursor to the end of the text

            // Set the title dynamically
            centeredTitle.text = "Town   ''$selectedTown''"

            // Create a confirmation dialog to edit the selected district
            val editTownDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newTownName = inputEditText.text.toString().trim()
                    when {
                        newTownName.isEmpty() -> {
                            Toast.makeText(this, "New Town name cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        newTownName == selectedTown -> {
                            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                        }
                        townList.contains(newTownName) -> {
                            Toast.makeText(this, "New Town name already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Update the district name in the list
                            val index = townList.indexOf(selectedTown)
                            val oldTownName = townList[index]
                            townList[index] = newTownName

                            // Set the new town name as the selected text
                            townDropDown.setText(newTownName, false)
                            townDropDown.setSelection(newTownName.length) // Move cursor to the end


                            areaDropDown.text.clear() // Clear dropdown text.

                            // Refresh adapter by recreating it
                            townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                            townDropDown.setAdapter(townAdapter)



                            Log.d("TownList", "Updated Town list: $townList")
                            Toast.makeText(this, "Town updated successfully", Toast.LENGTH_SHORT).show()


                            if (oldTownName != newTownName && townAreaMap.containsKey(oldTownName)) {
                                // Retrieve towns associated with the old district name
                                val areas = townAreaMap.remove(oldTownName)

                                // Update the map with the new district name and its towns
                                if (areas != null) {
                                    townAreaMap[newTownName] = areas
                                }

                                // Optionally, update the selected district in the UI if this is the currently selected district
                                if (townDropDown.text.toString() == oldTownName) {
                                    townDropDown.setText(newTownName, false)
                                }
                            }


                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create() // Call create() here only once

            // Show the dialog
            editTownDialog.setOnShowListener {
                val positiveButtonEdit = editTownDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButtonEdit = editTownDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // Load the custom font
                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

                // Apply the font to the buttons
                positiveButtonEdit.typeface = customFont
                negativeButtonEdit.typeface = customFont

                // Optional: Set text color or style
                positiveButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_green)) // For "Save"
                positiveButtonEdit.textSize = 16f
                negativeButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
                negativeButtonEdit.textSize = 16f

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputEditText.requestFocus()

                // Use a Handler to ensure the keyboard opens after the dialog is fully visible
                inputEditText.postDelayed({
                    imm.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
                }, 100) // Delay by 100ms
            }
            editTownDialog.show()
        } else {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            Toast.makeText(this, "Please select a town to edit", Toast.LENGTH_SHORT).show()
        }
    }




    val areaEditIcon = binding.editAreaIcon

    areaEditIcon.setOnClickListener {
        val selectedArea = areaDropDown.text.toString().trim()

        if (selectedArea.isNotEmpty() && areaList.contains(selectedArea)) {
            // Inflate the custom edit alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
            val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
            val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)
            // Set the Edit Text Hint
            inputEditText.hint = "Edit Property Area"
            val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)

            // Set the message text
            editMessage.text = "Edit the name of the selected area"

            // Set the EditText initial value
            inputEditText.setText(selectedArea)
            inputEditText.setSelection(selectedArea.length) // Move cursor to the end of the text

            // Set the title dynamically
            centeredTitle.text = "Property Area ''$selectedArea''"

            // Create a confirmation dialog to edit the selected district
            val editAreaDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newAreaName = inputEditText.text.toString().trim()
                    when {
                        newAreaName.isEmpty() -> {
                            Toast.makeText(this, "New Town name cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        newAreaName == selectedArea -> {
                            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                        }
                        areaList.contains(newAreaName) -> {
                            Toast.makeText(this, "New Town name already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Update the area name in the list
                            val index = areaList.indexOf(selectedArea)
                            areaList[index] = newAreaName

                            areaDropDown.setText(newAreaName, false)
                            areaDropDown.setSelection(newAreaName.length) // Move cursor to the end

                            // Refresh adapter by recreating it
                            areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                            areaDropDown.setAdapter(areaAdapter)

                            Log.d("AreaList", "Updated Area list: $areaList")
                            Toast.makeText(this, "Area updated successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create() // Call create() here only once

            // Show the dialog
            editAreaDialog.setOnShowListener {

                val positiveButtonEdit = editAreaDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButtonEdit = editAreaDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // Load the custom font
                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

                // Apply the font to the buttons
                positiveButtonEdit.typeface = customFont
                negativeButtonEdit.typeface = customFont

                // Optional: Set text color or style
                positiveButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_green)) // For "Save"
                positiveButtonEdit.textSize = 16f
                negativeButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
                negativeButtonEdit.textSize = 16f

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputEditText.requestFocus()

                // Use a Handler to ensure the keyboard opens after the dialog is fully visible
                inputEditText.postDelayed({
                    imm.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
                }, 100) // Delay by 100ms
            }
            editAreaDialog.show()
        } else {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            Toast.makeText(this, "Please select property area to edit", Toast.LENGTH_SHORT).show()
        }
    }


// ============= Delete  district Logic Below ================


binding.deleteDistrictIcon.setOnClickListener {

    val selectedDistrict = districtDropDown.text.toString().trim()
    if (selectedDistrict.isNotEmpty()) {
        // Inflate the custom alert view for the dialog
        val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
        val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = "Warning deleting ''$selectedDistrict'' will delete all towns and areas associated with it as well!"

        // Create a new title view for the dialog
        val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
        val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
        centeredTitle.text = "Do you still want to delete this district?"

        // Ensure that the titleView is not attached to any parent
        if (titleView.parent != null) {
            (titleView.parent as ViewGroup).removeView(titleView)
        }

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        // Create a confirmation dialog to delete the selected district
        val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
            .setView(titleView)
            .setCustomTitle(dialogView)

        deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->

            // Check if the district exists in the list
            if (districtList.contains(selectedDistrict)) {
                districtList.remove(selectedDistrict)

                // Remove all associated towns
                val towns = districtTownMap.remove(selectedDistrict)

                // Remove all areas for the removed towns
                towns?.forEach { town ->
                    townAreaMap.remove(town)
                }

                districtDropDown.text.clear() // Clear dropdown text.
                townDropDown.text.clear() // Clear dropdown text.
                areaDropDown.text.clear() // Clear dropdown text.

                townAdapter.clear()
                areaAdapter.clear()


                // Refresh adapter by recreating it
                districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                districtDropDown.setAdapter(districtAdapter)


//              Adding in as SharedPreference
                    PreferencesManager.saveDropdownList(districtList)

                Log.d("DistrictList", "Deleted district list: $districtList")
                Toast.makeText(this, "District deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Selected district does not exist", Toast.LENGTH_SHORT).show()
            }
        }

        deleteConfirmationDialog.setNegativeButton("Cancel") { _, _ -> }

        // Show the dialog
        val dialog = deleteConfirmationDialog.show()

        // Access the positive and negative buttons after the dialog is shown
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        // Load the custom font
        val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

        // Apply the font to the buttons
        positiveButton.typeface = customFont
        negativeButton.typeface = customFont

        // Optional: Set text color or style
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_red)) // For "Delete"
        positiveButton.textSize = 16f // For "Delete")
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
        negativeButton.textSize = 16f

    } else {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(districtDropDown.windowToken, 0)

        // Show a Toast message if no district is selected
        Toast.makeText(this, "Please select a district first", Toast.LENGTH_SHORT).show()
    }
}



// ============= Delete Town Logic Below ================

    binding.deleteTownIcon.setOnClickListener {

        val selectedTown = townDropDown.text.toString().trim()
        if (selectedTown.isNotEmpty()) {
            // Inflate the custom alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
            val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
            alertMessage.text = "Warning deleting ''$selectedTown'' will delete all property areas associated with it as well!"

            // Create a new title view for the dialog
            val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
            val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
            centeredTitle.text = "Are you sure you want to delete this town?"

            // Ensure that the titleView is not attached to any parent
            if (titleView.parent != null) {
                (titleView.parent as ViewGroup).removeView(titleView)
            }

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            // Create a confirmation dialog to delete the selected district
            val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
                .setView(titleView)
                .setCustomTitle(dialogView)

            deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->
                // Check if the town exists in the list
                if (townList.contains(selectedTown)) {
                    // Remove the town from the list
                    townList.remove(selectedTown)

                    // Remove all associated areas
                    val areas = townAreaMap.remove(selectedTown)

                    townDropDown.text.clear()
                    areaDropDown.text.clear()
                    areaAdapter.clear()

                    // Refresh the town adapter
                    townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                    townDropDown.setAdapter(townAdapter)
                    // Clear the area dropdown and its adapter since the town was deleted

                    Log.d("TownList", "Deleted town: $selectedTown")
                    Log.d("AreaList", "Deleted areas for town $selectedTown: $areas")
                    Log.d("TownAreaMap", "Updated townAreaMap: $townAreaMap")

                    Toast.makeText(this, "Town and its associated areas deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Selected Town does not exist", Toast.LENGTH_SHORT).show()
                }
            }


            deleteConfirmationDialog.setNegativeButton("Cancel") { _, _ -> }

            // Show the dialog
            val dialog = deleteConfirmationDialog.show()

            // Access the positive and negative buttons after the dialog is shown
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Load the custom font
            val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

            // Apply the font to the buttons
            positiveButton.typeface = customFont
            negativeButton.typeface = customFont

            // Optional: Set text color or style
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_red)) // For "Delete"
            positiveButton.textSize = 16f // For "Delete")
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
            negativeButton.textSize = 16f

        } else {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(townDropDown.windowToken, 0)

            // Show a Toast message if no district is selected
            Toast.makeText(this, "Please select a town first", Toast.LENGTH_SHORT).show()
        }
    }

    // ============= Delete Property Area Logic Below ================

    binding.deleteAreaIcon.setOnClickListener {

        val selectedArea = areaDropDown.text.toString().trim()
        if (selectedArea.isNotEmpty()) {
            // Inflate the custom alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
            val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
            alertMessage.text = "Are you sure you want to delete this Area?"

            // Create a new title view for the dialog
            val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
            val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
            centeredTitle.text = "Property Area   ''$selectedArea''"

            // Ensure that the titleView is not attached to any parent
            if (titleView.parent != null) {
                (titleView.parent as ViewGroup).removeView(titleView)
            }

            // Create a confirmation dialog to delete the selected district
            val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
                .setView(titleView)
                .setCustomTitle(dialogView)

            deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->
                // Check if the district exists in the list
                if (areaList.contains(selectedArea)) {
                    areaList.remove(selectedArea)
                    areaDropDown.setText("")

                    // Refresh adapter by recreating it
                    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                    areaDropDown.setAdapter(areaAdapter)


                    Log.d("AreaList", "Deleted town list: $areaList")
                    Toast.makeText(this, "Area deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Selected Area does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            deleteConfirmationDialog.setNegativeButton("Cancel") { _, _ -> }

            // Show the dialog
            val dialog = deleteConfirmationDialog.show()

            // Access the positive and negative buttons after the dialog is shown
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Load the custom font
            val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

            // Apply the font to the buttons
            positiveButton.typeface = customFont
            negativeButton.typeface = customFont

            // Optional: Set text color or style
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_red)) // For "Delete"
            positiveButton.textSize = 16f // For "Delete")
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
            negativeButton.textSize = 16f

        } else {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(areaDropDown.windowToken, 0)

            // Show a Toast message if no district is selected
            Toast.makeText(this, "Please select area first", Toast.LENGTH_SHORT).show()
        }
    }




// Sample data for the dropdown (Land Type)
val landList = listOf("Residential", "Commercial", "Agricultural", "Industrial")

// Create an ArrayAdapter and set it on the AutoCompleteTextView
    val landTypeDropdown = binding.landTypeDropdown
    val landAdapter = ArrayAdapter(this, custom_dropdown_item, landList)
    landTypeDropdown.setAdapter(landAdapter)

    landTypeDropdown.setOnClickListener {
        landTypeDropdown.showDropDown()
    }


// Optional: Set threshold to start showing dropdown from first character
    landTypeDropdown.threshold = 1

// Handle selection events
    landTypeDropdown.setOnItemClickListener { parent, _, position, _ ->
        val selectedLand = parent.getItemAtPosition(position) as String
        Toast.makeText(this, "You selected: $selectedLand", Toast.LENGTH_SHORT).show()

        // Perform your action based on the selected item
        when (selectedLand) {
            "Residential" -> {
                binding.khasraText.visibility = View.VISIBLE
                binding.khasraEditText.visibility = View.VISIBLE            }
            "Commercial" -> {
                binding.khasraText.visibility = View.VISIBLE
                binding.khasraEditText.visibility = View.VISIBLE
            }
            "Agricultural" -> {
                binding.khasraText.visibility = View.VISIBLE
                binding.khasraEditText.visibility = View.VISIBLE
            }
            "Industrial" -> {
                binding.khasraText.visibility = View.VISIBLE
                binding.khasraEditText.visibility = View.VISIBLE
            }
        }
    }



//    When add icon of Land clicked then new values would be added to the list

    binding.addLandIcon.setOnClickListener {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayoutSettings) // Replace with your actual TableLayout ID

        // Iterate through each child (rows in this case) of the TableLayout
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i)
            if (row is TableRow) {
                // Iterate through each child of the TableRow
                for (j in 0 until row.childCount) {
                    val view = row.getChildAt(j)
                    if (view is EditText) {
                        // Clear the current value
                        view.text.clear()

                        // Optionally, set a new value
                        // view.setText("New Value") // Uncomment and replace "New Value" with your value
                    }
                }
            }
        }

        // Optionally, clear the Khasra EditText
        binding.khasraEditText.text.clear()

        // Optionally, clear or reset the AutoCompleteTextView
        binding.landTypeDropdown.text.clear()
    }




//    // Sample data for the property type dropdown
//    val propertyTypeList = listOf("Plot","House", "Apartment", "Shop")
//
//// Find the AutoCompleteTextView
//    val propertyTypeDropdown = findViewById<AutoCompleteTextView>(R.id.propertyTypeDropdown)
//
//// Create an ArrayAdapter using a custom layout or a built-in Android layout
//    val propertyTypeAdapter = ArrayAdapter(this, custom_dropdown_item, propertyTypeList)
//
//// Set the adapter on the AutoCompleteTextView
//    propertyTypeDropdown.setAdapter(propertyTypeAdapter)
//

//
//// Optional: Set threshold if you want the dropdown to show after typing only 1 character
//    propertyTypeDropdown.threshold = 1
//
//// Handle selection events
//    propertyTypeDropdown.setOnItemClickListener { parent, _, position, _ ->
//        val selectedPropertyType = parent.getItemAtPosition(position) as String
//        Toast.makeText(this, "Selected: $selectedPropertyType", Toast.LENGTH_SHORT).show()
//
//        // Perform actions based on the selected property type
//        when (selectedPropertyType) {
//            "Plot" -> {
//                // Action for House
//            }
//            "Apartment" -> {
//                // Action for Apartment
//            }
//            "Building" -> {
//                // Action for Villa
//            }
//            "Farmhouse" -> {
//                // Action for Farmhouse
//            }
//        }
//    }



//    Save the lists when save button is pressed
    binding.saveButton.setOnClickListener {
        // Log the data for debugging
        Log.d("distList", "District List: $districtList+$townList+$areaList")


        // Show confirmation dialog
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Save")
            .setMessage(
                "Are you sure you want to save the above data?\n\n"
            )
            .setPositiveButton("Save") { _, _ ->


            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Close the dialog without saving
            }
            .show()
    }


}


}