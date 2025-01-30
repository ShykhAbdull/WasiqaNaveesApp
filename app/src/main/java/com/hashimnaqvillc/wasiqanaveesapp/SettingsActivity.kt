package com.hashimnaqvillc.wasiqanaveesapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
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
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

private lateinit var districtList: MutableList<String>
private lateinit var townList: MutableList<String>
private lateinit var areaList: MutableList<String>
private lateinit var landList: MutableList<String>

private lateinit var districtAdapter: ArrayAdapter<String>
private lateinit var townAdapter: ArrayAdapter<String>
private lateinit var areaAdapter: ArrayAdapter<String>
private lateinit var landAdapter: ArrayAdapter<String>


private lateinit var binding: ActivitySettingsBinding


//-------------------------------------------------------------------------
//private val initialDistricts = mutableListOf("Lahore", "Kasur", "Sheikhupura", "Faisalabad","Multan")




//------------------------------------------------------------------------

@SuppressLint("ClickableViewAccessibility", "InflateParams", "CutPasteId")
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
binding = ActivitySettingsBinding.inflate(layoutInflater)
setContentView(binding.root)

lateinit var selectedLandType: String
landList = mutableListOf()

selectedLandType = ""


    val districtTownMap = PreferencesManager.getDistrictTownMap()
    val townAreaMap = PreferencesManager.getTownAreaMap()

    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            handleBackPress()
        }
    })


//    ------------------------------------------------------ UI CHANGES -------------------------------------------------------------------

    addRippleEffect(binding.headingTextSettings)
    addRippleEffect(binding.taxRateBtn)





//   Logic for showing and Hiding the Views on Settings Activity

    val settingIcon = findViewById<ImageButton>(R.id.nav_settings_icon)
    settingIcon.visibility = View.GONE

    val dateMonth = findViewById<TextView>(R.id.date_month_day)
    dateMonth.visibility = View.GONE

//    val dateYear = findViewById<TextView>(R.id.date_year)
//    dateYear.visibility = View.GONE

    val wasiqaLogo = findViewById<ImageView>(R.id.nav_wasiqa_logo)
    wasiqaLogo.visibility = View.GONE

    val settingText = findViewById<TextView>(R.id.nav_settings_text)
    settingText.visibility = View.VISIBLE

    val profileEditBtn = findViewById<Button>(R.id.profile_edit_btn)
    profileEditBtn.visibility = View.VISIBLE

//    ------------------------------------------UI CHANGES ----------------------------------------------------------


//    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

profileEditBtn.setOnClickListener {
    val intent = Intent(this, ProfileActivtySettings::class.java)
    startActivity(intent)
}

val backBtn = findViewById<ImageButton>(R.id.nav_back)
backBtn.setOnClickListener {
    handleBackPress()
}


//        Logic for District DropDownClick
val districtDropDown = binding.districtDropdown

    val savedDistrictList = PreferencesManager.getDropdownList()
    //        Logic for District DropDownClick
    districtList = mutableListOf()

    districtList = savedDistrictList.toMutableList()

    districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
    districtDropDown.setAdapter(districtAdapter)
// Start searching after typing one character
    districtDropDown.threshold = 1


    districtDropDown.setOnClickListener {
        binding.districtDropdown.showDropDown()
        binding.districtDropdown.requestFocus()
    }

    districtDropDown.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            binding.districtIconContainer.visibility = View.VISIBLE
            binding.districtDropdown.showDropDown()
        }
        else{
            binding.districtIconContainer.visibility = View.GONE
        }
    }

    // TownDropdown setup
    val townDropDown = binding.townDropdown // ID of AutoCompleteTextView for towns
    townList = mutableListOf()
    townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
    townDropDown.setAdapter(townAdapter)

    binding.townDropdown.setOnClickListener {
            // Show the dropdown only if a district is selected
            binding.townDropdown.showDropDown()
            binding.townDropdown.requestFocus()
    }

    binding.townDropdown.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus && binding.districtDropdown.text.toString().isNotEmpty() && districtList.contains(binding.districtDropdown.text.toString().trim())) {
            binding.townIconContainer.visibility = View.VISIBLE
            binding.townDropdown.showDropDown()
        } else if (hasFocus) {
            binding.townDropdown.clearFocus() // Prevent focus if no district is selected
            Toast.makeText(this, "Add or Select District first!", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.townIconContainer.visibility = View.GONE
        }
    }

    val areaDropDown = binding.propertyAreaDropdown
    areaList = mutableListOf()
    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
    areaDropDown.setAdapter(areaAdapter)

    areaDropDown.setOnClickListener {
        areaDropDown.showDropDown()
        areaDropDown.requestFocus()
    }

    binding.propertyAreaDropdown.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus && binding.townDropdown.text.toString().isNotEmpty() && townList.contains(binding.townDropdown.text.toString().trim() )) {
            areaDropDown.showDropDown()
            binding.propertyAreaIconContainer.visibility = View.VISIBLE

        } else if (hasFocus) {

            areaDropDown.clearFocus() // Prevent focus if no town is selected
            Toast.makeText(this, "Add or Select Town first!", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.propertyAreaIconContainer.visibility = View.GONE
        }
    }


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




// Optional: Customize the keyboard action to show "Next" instead of "Done"

binding.districtDropdown.imeOptions = EditorInfo.IME_ACTION_NEXT
binding.districtDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.townDropdown.imeOptions = EditorInfo.IME_ACTION_NEXT
binding.townDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)

binding.propertyAreaDropdown.imeOptions = EditorInfo.IME_ACTION_DONE
binding.propertyAreaDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)



val districtAddIcon = binding.addDestrictIcon

//// Sample data for the dropdown
//districtList = PreferencesManager.getDropdownList()
//districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
//districtDropDown.setAdapter(districtAdapter)
//// Start searching after typing one character
//districtDropDown.threshold = 1

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


//    // Fetch the districtTownMap once when the activity starts
//    val districtTownMap: MutableMap<Int, MutableList<String>> by lazy {
//        PreferencesManager.getDistrictTownMap()
//    }


//    // Fetch the TownAreaMap once when the activity starts
//    val townAreaMap: MutableMap<Int, MutableList<String>> by lazy {
//        PreferencesManager.getTownAreaMap()
//    }

    binding.districtDropdown.setOnItemClickListener { _, _, _, _ ->
        val selectedDistrict = binding.districtDropdown.text.toString().trim()
        val districtHashKey = selectedDistrict.hashCode()

        if (!districtList.contains(selectedDistrict)) {
            districtList.add(selectedDistrict)
            Toast.makeText(this, "$selectedDistrict added successfully", Toast.LENGTH_SHORT).show()

            binding.townDropdown.text.clear()
            binding.propertyAreaDropdown.text.clear()

            townList = districtTownMap[districtHashKey] ?: mutableListOf()

            val townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
            binding.townDropdown.setAdapter(townAdapter)
            binding.townDropdown.threshold = 1
            binding.townDropdown.requestFocus()


        } else {
            Toast.makeText(this, "$selectedDistrict is already selected", Toast.LENGTH_SHORT).show()
        }

        // Ensure the dropdown adapter remains linked to initialDistricts
        districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
        binding.districtDropdown.setAdapter(districtAdapter)
        binding.districtDropdown.threshold = 1
    }





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





// On Town Selection
    townDropDown.setOnItemClickListener { _, _, _, _ ->

        areaDropDown.text.clear()

        // Normalize the selected town to avoid formatting issues
        val currentDistrictHashKey = districtDropDown.text.toString().trim().hashCode()
        val currentTownName = townDropDown.text.toString().trim()
        val currentTownHashKey = "$currentDistrictHashKey-$currentTownName".hashCode()

        // Retrieve the areas for the selected town using the town hash key
        val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()


        // Clear and reset areas
        areaList = areas
        areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
        areaDropDown.setAdapter(areaAdapter)
        areaDropDown.threshold = 1


        Log.d("TownSelection", "Selected town: $districtList, $townList, $areaList")
    }



    // Fetch the AreaLandMap once when the activity starts
    val areaLandMap: MutableMap<Int, MutableList<String>> by lazy {
        PreferencesManager.getAreaLandMap()
    }

//    // On Area Selection
//    areaDropDown.setOnItemClickListener { _, _, position, _ ->
//
//        val currentSelectedDistrict = districtDropDown.text.toString().trim()
//        val currentSelectedTown = townDropDown.text.toString().trim()
//        val currentSelectedArea = areaDropDown.text.toString().trim()
//
//        // Create hash keys similarly to how you're doing for other mappings
//        val districtHashKey = currentSelectedDistrict.hashCode()
//        val townHashKey = "$districtHashKey-$currentSelectedTown".hashCode()
//        val areaHashKey = "$townHashKey-$currentSelectedArea".hashCode()
//
//        // Clear and reset land types
//        val landTypes = areaLandMap[areaHashKey] ?: mutableListOf()
//         landList = landTypes
//        landAdapter = ArrayAdapter(this, custom_dropdown_item, landList)
//
//        Log.d("AreaSelection", "Selected area: $currentSelectedArea, land types: $landTypes")
//    }









// Apply filter and count the visible items
    val areaItemCount = areaAdapter.count // Gets the current filtered item count
    if (areaItemCount <= 3) {
        areaDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT

    } else {
        // Set a fixed height for more than 3 items
        areaDropDown.dropDownHeight =
            (areaDropDown.context.resources.displayMetrics.density * 150).toInt()

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




    //    When the town text clears all other field text clear as well
    binding.districtInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        districtDropDown.text.clear()
        townDropDown.text.clear()
        areaDropDown.text.clear()
        districtDropDown.requestFocus()

    }

    //    When the town text clears all other field text clear as well
    binding.townInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        townDropDown.text.clear()
        areaDropDown.text.clear()
        townDropDown.requestFocus()
    }

    //    When the town text clears all other field text clear as well
    binding.propertyAreaInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        areaDropDown.text.clear()
        areaDropDown.requestFocus()
    }

//--------------------------------------------------------------

    setupDropdowns()

    districtAddIcon.setOnClickListener {
////        // Show the keyboard
//        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.showSoftInput(binding.districtDropdown, InputMethodManager.SHOW_IMPLICIT)


            // Proceed with the dialog to confirm adding the district
            val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
            val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
            val inputEditText = dialogView.findViewById<EditText>(R.id.addInputEditText)

            // Set the message text
            addMessage.text = "Add a New District?"

            // Create a confirmation dialog
            val addDistrictDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->

                    val currentDistrictName = inputEditText.text.toString().trim()

                    if (districtList.contains(currentDistrictName)){
                        Toast.makeText(this, "District Already Exists!", Toast.LENGTH_SHORT).show()
                    } else if (currentDistrictName.isEmpty()){
                        Toast.makeText(this, "District cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    else {

                        // Add the new district to the list
                        savedDistrictList.add(currentDistrictName)

                        districtList = savedDistrictList.toMutableList()


                        // Refresh adapter by recreating it
                        districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                        districtDropDown.setAdapter(districtAdapter)


                        // Normalize the selected district to avoid formatting issues
                        val currentDistrictHashKey = currentDistrictName.hashCode()


                        // Retrieve the towns for the selected district using the district hash key
                        val towns = districtTownMap[currentDistrictHashKey] ?: mutableListOf()


                        // Update the town dropdown
                        townList = towns
                        townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                        townDropDown.setAdapter(townAdapter)
                        townDropDown.threshold = 1


                        townDropDown.text.clear() // Clear towns dropdown
                        areaDropDown.text.clear() // Clear areas dropdown


                        Toast.makeText(this, "District added successfully", Toast.LENGTH_SHORT)
                            .show()

                    }

                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()

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
                positiveButtonAdd.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.wasiqa_light_green
                    )
                ) // For "Add"
                positiveButtonAdd.textSize = 16f
                negativeButtonAdd.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.wasiqa_dark_grey
                    )
                ) // For "Cancel"
                negativeButtonAdd.textSize = 16f

                // Make the background dim darker
                val window = addDistrictDialog.window
                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
            }

            addDistrictDialog.show()

    }






    val townAddIcon = binding.addTownIcon // Assuming town add icon ID

    townAddIcon.setOnClickListener {
        // Close the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        val currentDistrictName = districtDropDown.text.toString().trim()

            // Inflate the custom add alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
            val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
            val inputEditText = dialogView.findViewById<EditText>(R.id.addInputEditText)

            // Set the message text and pre-fill the input field with the dropdown text
            addMessage.text = "Add New Town for $currentDistrictName District?"

// Create a confirmation dialog to add a new town
            val addTownDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->

                    val currentSelectedTown = inputEditText.text.toString().trim()

                    // Generate district hash code
                    val districtHashCode = currentDistrictName.hashCode()

                    // Generate town hash code (dependent on district hash code)
                    val townHashCode = "$districtHashCode-$currentSelectedTown".hashCode()

                    when {
                        currentSelectedTown.isEmpty() -> {
                            Toast.makeText(this, "Town cannot be empty", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            // Fetch the current list of towns for the selected district
                            val towns = districtTownMap[districtHashCode] ?: mutableListOf()

                            // Check if the town already exists
                            if (towns.any { "$districtHashCode-${it}".hashCode() == townHashCode }) {
                                Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
                            }else {
                                // Add the new town to the list
                                towns.add(currentSelectedTown)

                                // Update the districtTownMap
                                districtTownMap[districtHashCode] = towns

                                // Ensure the townDropDown reflects the new town
                                townDropDown.setText(currentSelectedTown, false)
                                townDropDown.setSelection(currentSelectedTown.length) // Move cursor to the end

                                // Update the town list and adapter
                                townList = towns
                                townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                                townDropDown.setAdapter(townAdapter)

                                // Clear areas dropdown
                                areaDropDown.text.clear()
                                areaList = mutableListOf()
                                areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                                areaDropDown.setAdapter(areaAdapter)


                                Toast.makeText(this, "Town added successfully", Toast.LENGTH_SHORT).show()
                            }
                            Log.d("TownList", "Updated town list for district $districtHashCode: $townList")
                        }
                    }
                }



                .setNegativeButton("Cancel") { _, _ -> }
                .create()

            // Customize dialog button styles
            addTownDialog.setOnShowListener {
                val positiveButtonAdd = addTownDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButtonAdd = addTownDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

                positiveButtonAdd.typeface = customFont
                negativeButtonAdd.typeface = customFont

                positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green))
                positiveButtonAdd.textSize = 16f
                negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey))
                negativeButtonAdd.textSize = 16f
                // Make the background dim darker
                val window = addTownDialog.window
                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
            }

            addTownDialog.show()

    }








    // Logic for Adding New Property Area

    val areaAddIcon = binding.addAreaIcon // Assuming area add icon ID
    areaAddIcon.setOnClickListener {
        // Close the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
        val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
        val inputEditText = dialogView.findViewById<EditText>(R.id.addInputEditText)

        val currentDistrictHashKey = districtDropDown.text.toString().trim().hashCode()

        val currentSelectedTown = townDropDown.text.toString().trim()
        val currentTownHashKey = "$currentDistrictHashKey-$currentSelectedTown".hashCode()


            // Set the message text
            addMessage.text = "Add new Area for $currentSelectedTown Town?"

            // Create a confirmation dialog to add a new property area
            val addAreaDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->


                    val currentArea = inputEditText.text.toString().trim()
                    val currentAreaHashKey = "$currentTownHashKey-$currentArea".hashCode()

                    when {
                        currentArea.isEmpty() -> {
                            Toast.makeText(
                                this,
                                "Property Area cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // Add the new area to the town's list in the map
                            val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()
                            if (areas.contains(currentArea)) {
                                Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                areas.add(currentArea) // Add the new area to the list

                                // Update the townAreaMap
                                townAreaMap[currentTownHashKey] = areas

                                val lands = areaLandMap[currentAreaHashKey] ?: mutableListOf()
                                landList = lands
                                landAdapter = ArrayAdapter(this, custom_dropdown_item, landList)

                                // Update the area list and adapter for the current town
                                areaList = areas
                                areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                                areaDropDown.setAdapter(areaAdapter)

                                // Set the new area as the selected text
                                areaDropDown.setText(currentArea, false)
                                areaDropDown.setSelection(currentArea.length)

                                // Provide feedback
                                Toast.makeText(
                                    this,
                                    "Property Area added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Log.d("AreaList", "Updated area list for $currentTownHashKey: $areaList")
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()

            // Customize dialog button styles
            addAreaDialog.setOnShowListener {
                val positiveButtonAdd = addAreaDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButtonAdd = addAreaDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                // Load the custom font
                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)

                // Apply the font to the buttons
                positiveButtonAdd.typeface = customFont
                negativeButtonAdd.typeface = customFont

                // Set text color and size
                positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green))
                positiveButtonAdd.textSize = 16f
                negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey))
                negativeButtonAdd.textSize = 16f
                // Make the background dim darker
                val window = addAreaDialog.window
                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
            }

            addAreaDialog.show()
    }



//        --------------- Add district/Town/Area Logic Above-------------


// ============= Edit District/Town Logic Below ================




    val districtEditIcon = binding.editDistrictIcon
    districtEditIcon.setOnClickListener {
    val oldDistrictName = districtDropDown.text.toString().trim()
    val oldDistrictHashKey = oldDistrictName.hashCode()
    val currentSelectedTown = townDropDown.text.toString().trim()
    val currentArea = areaDropDown.text.toString().trim()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


    if (oldDistrictName.isNotEmpty() && districtList.contains(oldDistrictName)) {
        // Inflate the custom edit alert view for the dialog
        val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
        val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
        val inputEditText = dialogView.findViewById<EditText>(R.id.editInputEditText)

        // Set the Edit Text Hint
        inputEditText.hint = "Edit District Name"

        val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)

        // Set the message text
        editMessage.text = "Edit the name of the selected district"

        // Set the EditText initial value
        inputEditText.setText(oldDistrictName)
        inputEditText.setSelection(oldDistrictName.length) // Move cursor to the end of the text

        // Set the title dynamically
        centeredTitle.text = "District   ''$oldDistrictName''"

        // Create a confirmation dialog to edit the selected district
        val editDistrictDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newDistrictName = inputEditText.text.toString().trim()

                when {
                    newDistrictName.isEmpty() -> {
                        Toast.makeText(this, "District cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    newDistrictName == oldDistrictName -> {
                        Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                    }
                    districtList.contains(newDistrictName) -> {
                        Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Hash-based key for district (UNIQUE KEYS)
                        val newDistrictHashKey = newDistrictName.hashCode()

                        // Update the district name in the list using hash-based keys
                        val index = districtList.indexOf(oldDistrictName)

                        if (index != -1) {
                            districtList[index] = newDistrictName
                        }

                        // Update the district in the map using hash codes to avoid dependency issues
                        val towns = districtTownMap.remove(oldDistrictHashKey) ?: mutableListOf()
                        districtTownMap[newDistrictHashKey] = towns





                        // Update ALL town-area mappings for this district
                        towns.forEach { townName ->
                            val oldTownHashKey = "$oldDistrictHashKey-$townName".hashCode()
                            val newTownHashKey = "$newDistrictHashKey-$townName".hashCode()



                            // Transfer areas from old town hash to new town hash
                            val areas = townAreaMap.remove(oldTownHashKey) ?: mutableListOf()
                            townAreaMap[newTownHashKey] = areas

                            areaList = areas
                            areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                            areaDropDown.setAdapter(areaAdapter)
                            areaDropDown.threshold = 1
                        }

                        // Generate the key for this new district selection
                        val oldFinalKey = generateFinalKey(oldDistrictName, currentSelectedTown, currentArea, selectedLandType)
                        val newFinalKey = generateFinalKey(newDistrictName, currentSelectedTown, currentArea, selectedLandType)

                        Log.d("newFinalKey", "newFinalKey: $oldFinalKey, $newFinalKey")

                        val currentValues = PreferencesManager.getLandOptionRates()
                        val existingLandValues = currentValues[oldFinalKey] ?: return@setPositiveButton
                        currentValues.remove(oldFinalKey) // Remove old key
                        currentValues[newFinalKey] = existingLandValues // Add with new key
                        PreferencesManager.saveLandRates(currentValues)


                        districtDropDown.setText(newDistrictName, false)
                        districtDropDown.setSelection(newDistrictName.length) // Move cursor to the end

                        townDropDown.text.clear() // Clear dropdown text.
                        areaDropDown.text.clear() // Clear dropdown text.


                        // Refresh adapter by recreating it
                        districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                        districtDropDown.setAdapter(districtAdapter)


                        townList = towns
                        townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                        townDropDown.setAdapter(townAdapter)
                        townDropDown.threshold = 1



                        Log.d("newDistrictList", "After district list: $newDistrictName")
                        Log.d("townList", "After town list: $townList")
                        Toast.makeText(this, "District updated successfully", Toast.LENGTH_SHORT).show()



                    }
                }
                // Close the keyboard
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
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
            // Make the background dim darker
            val window = editDistrictDialog.window
            window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

            // Use a Handler to ensure the keyboard opens after the dialog is fully visible
            inputEditText.postDelayed({
                inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
            }, 100) // Delay by 100ms

        }
        editDistrictDialog.show()
    } else {
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        Toast.makeText(this, "Add or Select District to edit", Toast.LENGTH_SHORT).show()
    }
}


    val townEditIcon = binding.editTownIcon
    townEditIcon.setOnClickListener {
        val currentSelectedDistrict = districtDropDown.text.toString().trim()
        val currentTownName = townDropDown.text.toString().trim()
        val currentArea = areaDropDown.text.toString().trim()


        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentTownName.isNotEmpty() && townList.contains(currentTownName)) {
            // Inflate the custom edit alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
            val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
            val inputEditText = dialogView.findViewById<EditText>(R.id.editInputEditText)
            // Set the Edit Text Hint
            inputEditText.hint = "Edit Town Name"
            val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)

            // Set the message text
            editMessage.text = "Edit the name of the selected town"

            // Set the EditText initial value
            inputEditText.setText(currentTownName)
            inputEditText.setSelection(currentTownName.length) // Move cursor to the end of the text

            // Set the title dynamically
            centeredTitle.text = "Town   ''$currentTownName''"

            // Create a confirmation dialog to edit the selected district
            val editTownDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->

                    val newTownName = inputEditText.text.toString().trim()

                    when {
                        newTownName.isEmpty() -> {
                            Toast.makeText(this, "Town cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        newTownName == currentTownName -> {
                            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                        }
                        townList.contains(newTownName) -> {
                            Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Hash-based keys for town (UNIQUE KEYS)
                            val districtHashCode = currentSelectedDistrict.hashCode()
                            val oldTownHashCode = "$districtHashCode-$currentTownName".hashCode()
                            val newTownHashCode = "$districtHashCode-$newTownName".hashCode()

                            // Fetch the current list of towns for the selected district
                            val towns = districtTownMap[districtHashCode] ?: mutableListOf()

                            // Check if the new town name already exists (excluding the current town)
                            if (towns.any { it != currentTownName && "$districtHashCode-${it}".hashCode() == newTownHashCode }) {
                                Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                // Update the town name in the list
                                val index = towns.indexOf(currentTownName)
                                if (index != -1) {
                                    towns[index] = newTownName
                                }

                                // Update the districtTownMap
                                districtTownMap[districtHashCode] = towns

                                // Transfer areas from old town hash to new town hash
                                val areas = townAreaMap.remove(oldTownHashCode) ?: mutableListOf()
                                townAreaMap[newTownHashCode] = areas

                                // Update preferences with new keys
                                val oldFinalKey = generateFinalKey(currentSelectedDistrict, currentTownName, currentArea, selectedLandType)
                                val newFinalKey = generateFinalKey(currentSelectedDistrict, newTownName, currentArea, selectedLandType)

                                val currentValues = PreferencesManager.getLandOptionRates()
                                val existingLandValues = currentValues[oldFinalKey] ?: return@setPositiveButton
                                currentValues.remove(oldFinalKey)
                                currentValues[newFinalKey] = existingLandValues
                                PreferencesManager.saveLandRates(currentValues)

                                // Update UI elements
                                townDropDown.setText(newTownName, false)
                                townDropDown.setSelection(newTownName.length)

                                // Update the town list and adapter
                                townList = towns
                                townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                                townDropDown.setAdapter(townAdapter)
                                townDropDown.threshold = 1

                                // Update area dropdown with transferred areas
                                areaDropDown.text.clear()
                                areaList = areas
                                areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                                areaDropDown.setAdapter(areaAdapter)
                                areaDropDown.threshold = 1

                                Log.d("TownList", "Updated town list for district $districtHashCode: $townList")
                                Log.d("Areas", "Transferred areas for town $newTownName: $areas")

                                Toast.makeText(this, "Town updated successfully", Toast.LENGTH_SHORT).show()
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
                // Make the background dim darker
                val window = editTownDialog.window
                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)


                // Use a Handler to ensure the keyboard opens after the dialog is fully visible
                inputEditText.postDelayed({
                    inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
                }, 100) // Delay by 100ms
            }
            editTownDialog.show()
        } else {
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            Toast.makeText(this, "Please select a town to edit", Toast.LENGTH_SHORT).show()
        }
    }




    val areaEditIcon = binding.editAreaIcon

    areaEditIcon.setOnClickListener {
        val currentSelectedDistrict = districtDropDown.text.toString().trim()
        val currentSelectedTown = townDropDown.text.toString().trim()
        val currentTownHashKey = "$currentSelectedDistrict-$currentSelectedTown".hashCode()
        val currentAreaName = areaDropDown.text.toString().trim()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentAreaName.isNotEmpty() && areaList.contains(currentAreaName)) {
            // Inflate the custom edit alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
            val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
            val inputEditText = dialogView.findViewById<EditText>(R.id.editInputEditText)
            // Set the Edit Text Hint
            inputEditText.hint = "Edit Property Area"
            val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)

            // Set the message text
            editMessage.text = "Edit the name of the selected area"

            // Set the EditText initial value
            inputEditText.setText(currentAreaName)
            inputEditText.setSelection(currentAreaName.length) // Move cursor to the end of the text

            // Set the title dynamically
            centeredTitle.text = "Property Area ''$currentAreaName''"

// Create a confirmation dialog to edit the selected district
            val editAreaDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newAreaName = inputEditText.text.toString().trim()

                    when {
                        newAreaName.isEmpty() -> {
                            Toast.makeText(this, "Area cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        newAreaName == currentAreaName -> {
                            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                        }
                        areaList.contains(newAreaName) -> {
                            Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {

                            // Update the area in the list
                            val index = areaList.indexOf(currentAreaName)
                            if (index != -1) {
                                areaList[index] = newAreaName
                            }

                            // Update the area mapping in townAreaMap
                            val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()
                            val areaIndex = areas.indexOf(currentAreaName)
                            if (areaIndex != -1) {
                                areas[areaIndex] = newAreaName
                                townAreaMap[currentTownHashKey] = areas
                            }

                            // Update preferences with new keys
                            val oldFinalKey = generateFinalKey(currentSelectedDistrict, currentSelectedTown, currentAreaName, selectedLandType)
                            val newFinalKey = generateFinalKey(currentSelectedDistrict, currentSelectedTown, newAreaName, selectedLandType)

                            val currentValues = PreferencesManager.getLandOptionRates()
                            val existingLandValues = currentValues[oldFinalKey] ?: return@setPositiveButton
                            currentValues.remove(oldFinalKey)
                            currentValues[newFinalKey] = existingLandValues
                            PreferencesManager.saveLandRates(currentValues)

                            // Update UI elements
                            areaDropDown.setText(newAreaName, false)
                            areaDropDown.setSelection(newAreaName.length)

                            // Refresh adapter
                            areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                            areaDropDown.setAdapter(areaAdapter)
                            areaDropDown.threshold = 1

                            Log.d("AreaList", "Updated area list for town $currentTownHashKey: $areaList")
                            Log.d("Preferences", "Updated keys from $oldFinalKey to $newFinalKey")

                            Toast.makeText(this, "Area updated successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()

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

                // Make the background dim darker
                val window = editAreaDialog.window
                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

                // Use a Handler to ensure the keyboard opens after the dialog is fully visible
                inputEditText.postDelayed({
                    inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
                }, 100) // Delay by 100ms
            }
            editAreaDialog.show()
        } else {
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            Toast.makeText(this, "Please select property area to edit", Toast.LENGTH_SHORT).show()
        }
    }


// ================ Delete  district Logic Below ================




binding.deleteDistrictIcon.setOnClickListener {

    val currentSelectedDistrict = districtDropDown.text.toString().trim()
    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    if (currentSelectedDistrict.isNotEmpty()) {
        // Inflate the custom alert view for the dialog
        val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
        val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = "Warning deleting ''$currentSelectedDistrict'' will delete all towns and areas associated with it as well!"

        // Create a new title view for the dialog
        val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
        val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
        centeredTitle.text = "Do you still want to delete this district?"

        // Ensure that the titleView is not attached to any parent
        if (titleView.parent != null) {
            (titleView.parent as ViewGroup).removeView(titleView)
        }

        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        // Create a confirmation dialog to delete the selected district
        val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
            .setView(titleView)
            .setCustomTitle(dialogView)

        deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->

            // Check if the district exists in the list
            if (districtList.contains(currentSelectedDistrict)) {

                val currentDistrictHashKey = currentSelectedDistrict.hashCode()
                // Get all towns for this district
                val townsToDelete = districtTownMap[currentDistrictHashKey] ?: emptyList()

                // Remove district and its towns
                districtList.remove(currentSelectedDistrict)
                districtTownMap.remove(currentDistrictHashKey)

                // Remove areas for each town in this district
                townsToDelete.forEach { town ->
                    val townKey = "$currentDistrictHashKey-$town".hashCode()
                    townAreaMap.remove(townKey)  // Just remove, don't put back
                }



                districtDropDown.text.clear() // Clear dropdown text.
                townDropDown.text.clear() // Clear dropdown text.
                areaDropDown.text.clear() // Clear dropdown text.

                townAdapter.clear()
                areaAdapter.clear()


                // Refresh adapter by recreating it
                districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                districtDropDown.setAdapter(districtAdapter)


                Log.d("DistrictList", "Deleted district list: $districtList, $districtTownMap, $townList, $townAreaMap ,$areaList")
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
        // Make the background dim darker
        val window = dialog.window
        window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

    } else {
        inputMethodManager.hideSoftInputFromWindow(districtDropDown.windowToken, 0)

        // Show a Toast message if no district is selected
        Toast.makeText(this, "Please select a district first", Toast.LENGTH_SHORT).show()
    }
}



// ============= Delete Town Logic Below ================

    binding.deleteTownIcon.setOnClickListener {

        val currentSelectedTown = townDropDown.text.toString().trim()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentSelectedTown.isNotEmpty()) {
            // Inflate the custom alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
            val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
            alertMessage.text = "Warning deleting ''$currentSelectedTown'' will delete all property areas associated with it as well!"

            // Create a new title view for the dialog
            val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
            val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
            centeredTitle.text = "Are you sure you want to delete this town?"

            // Ensure that the titleView is not attached to any parent
            if (titleView.parent != null) {
                (titleView.parent as ViewGroup).removeView(titleView)
            }

            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            // Create a confirmation dialog to delete the selected district
            val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
                .setView(titleView)
                .setCustomTitle(dialogView)

            deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->

                val currentSelectedDistrict = districtDropDown.text.toString().trim()
                if (townList.contains(currentSelectedTown)) {
                    val currentDistrictHashKey = currentSelectedDistrict.hashCode()
                    val currentTownHashKey = "$currentDistrictHashKey-$currentSelectedTown".hashCode()

                    // Remove the town from the townList and districtTownMap
                    townList.remove(currentSelectedTown)
                    val districtTowns = districtTownMap[currentDistrictHashKey] ?: mutableListOf()
                    districtTowns.remove(currentSelectedTown)
                    districtTownMap[currentDistrictHashKey] = districtTowns

                    // Remove all areas associated with this town
                    townAreaMap.remove(currentTownHashKey)  // This removes all areas for this town


                    townDropDown.text.clear()
                    areaDropDown.text.clear()
                    areaAdapter.clear()

                    // Refresh the town adapter
                    townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                    townDropDown.setAdapter(townAdapter)
                    // Clear the area dropdown and its adapter since the town was deleted

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
            // Make the background dim darker
            val window = dialog.window
            window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

        } else {
            inputMethodManager.hideSoftInputFromWindow(townDropDown.windowToken, 0)

            // Show a Toast message if no district is selected
            Toast.makeText(this, "Please select a town first", Toast.LENGTH_SHORT).show()
        }
    }

    // ============= Delete Property Area Logic Below ================

    binding.deleteAreaIcon.setOnClickListener {

        val currentSelectedArea = areaDropDown.text.toString().trim()
        if (currentSelectedArea.isNotEmpty()) {
            // Inflate the custom alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
            val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
            alertMessage.text = "Are you sure you want to delete this Area?"

            // Create a new title view for the dialog
            val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
            val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
            centeredTitle.text = "Property Area   ''$currentSelectedArea''"

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
                if (areaList.contains(currentSelectedArea)) {
                    areaList.remove(currentSelectedArea)
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
            // Make the background dim darker
            val window = dialog.window
            window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

        } else {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(areaDropDown.windowToken, 0)

            // Show a Toast message if no district is selected
            Toast.makeText(this, "Please select area first", Toast.LENGTH_SHORT).show()
        }
    }




    val taxRateBtn = binding.taxRateBtn

    taxRateBtn.setOnClickListener {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_rates, null)

        // Populate static fields
        dialogView.findViewById<TextView>(R.id.district_display_DialogDisplay).text = districtDropDown.text.toString()
        dialogView.findViewById<TextView>(R.id.town_DialogDisplay).text = townDropDown.text.toString()
        dialogView.findViewById<TextView>(R.id.propertyArea_DialogDisplay).text = areaDropDown.text.toString()

        // Access the AutoCompleteTextView for land types
        val currentSelectedDistrict = districtDropDown.text.toString().trim()
        val currentSelectedTown = townDropDown.text.toString().trim()
        val currentSelectedArea = areaDropDown.text.toString().trim()
        val landTypeDropdown = dialogView.findViewById<AutoCompleteTextView>(R.id.landTypeDropdownDialogDisplay)
        selectedLandType = landTypeDropdown.text.toString().trim()

        // Ensure all necessary selections are made
        if (currentSelectedDistrict.isEmpty() || currentSelectedTown.isEmpty() || currentSelectedArea.isEmpty()) {
            Toast.makeText(this, "Please select all fields (District, Town, and Area) first.", Toast.LENGTH_SHORT).show()
            // Close the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            return@setOnClickListener
        }

        // Configure dropdown for land types
        val landOptions = mutableListOf( "Residential", "Commercial","Agriculture", "Industrial")

        landAdapter = ArrayAdapter(this, custom_dropdown_item, landOptions)
        landTypeDropdown.setAdapter(landAdapter)

        landTypeDropdown.setOnClickListener {
            landTypeDropdown.showDropDown()
            landTypeDropdown.requestFocus()
        }

        landTypeDropdown.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !landTypeDropdown.isPopupShowing) {
                landTypeDropdown.showDropDown()
            }
        }



// Disable EditText fields initially
        dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.isEnabled = false
        dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.isEnabled = false
        dialogView.findViewById<EditText>(R.id.covereAreaDC)?.isEnabled = false
        dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.isEnabled = false
        dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.isEnabled = false


        landTypeDropdown.setOnItemClickListener { _, _, position, _ ->
            // Get the selected land type
            selectedLandType = landOptions[position]


            // Enable the fields now that a valid selection has been made
            dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.isEnabled = true
            dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.isEnabled = true
            dialogView.findViewById<EditText>(R.id.covereAreaDC)?.isEnabled = true
            dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.isEnabled = true
            dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.isEnabled = true



            val currentDistrict = districtDropDown.text.toString().trim()
            val currentDistrictHashKey = currentDistrict.hashCode()

            val currentTownName = binding.townDropdown.text.toString().trim()
            val currentTownHashKey = "$currentDistrictHashKey-$currentTownName".hashCode()

            val currentAreaName = binding.propertyAreaDropdown.text.toString().trim()
            val currentAreaHashKey = "$currentTownHashKey-$currentAreaName".hashCode()
            val newFinalKey = generateFinalKey(currentDistrict, currentSelectedTown, currentAreaName, selectedLandType)

            // Store the selected land type in the areaLandMap
            val landTypes = areaLandMap.getOrPut(currentAreaHashKey) { mutableListOf() }
            if (!landTypes.contains(selectedLandType)) {
                landTypes.add(selectedLandType)
                Log.d("AreaLandMap", "Updated land types for $currentAreaHashKey: $landTypes")
            }

            // Retrieve and prepopulate existing values for the newFinalKey
            val existingValues = PreferencesManager.getLandOptionRates()[newFinalKey]

            if (existingValues != null) {
                // Populate EditText fields with existing values
                dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.setText(existingValues.marlaDC)
                dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.setText(existingValues.marlaFBR)
                dialogView.findViewById<EditText>(R.id.covereAreaDC)?.setText(existingValues.coveredAreaDC)
                dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.setText(existingValues.coveredAreaFBR)
                dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.setText(existingValues.khasraNumber)
            } else {
                // Clear the EditText fields if no existing values are found
                dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.setText("")
                dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.setText("")
                dialogView.findViewById<EditText>(R.id.covereAreaDC)?.setText("")
                dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.setText("")
                dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.setText("")
            }

            Log.d("LandTypeSelection", "Selected land type: $selectedLandType, Final Key: $newFinalKey")
        }



        // Create the dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                if (selectedLandType.isEmpty()) {
                    Toast.makeText(this, "Please select a land type.", Toast.LENGTH_SHORT).show()
                }

                // Fetch values from EditText fields
                val marlaDc = dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.text.toString().trim()
                val marlaFbr = dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.text.toString().trim()
                val coveredAreaDc = dialogView.findViewById<EditText>(R.id.covereAreaDC)?.text.toString().trim()
                val coveredAreaFbr = dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.text.toString().trim()
                val khasraNumber = dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.text.toString().trim()

                if (marlaDc.isEmpty() || marlaFbr.isEmpty() || coveredAreaDc.isEmpty() || coveredAreaFbr.isEmpty()) {
                    Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
                }


                val finalKey = generateFinalKey(currentSelectedDistrict, currentSelectedTown, currentSelectedArea, selectedLandType)

                // Create LandValues object
                val landValues = PreferencesManager.LandValues(
                    marlaDC = marlaDc,
                    marlaFBR = marlaFbr,
                    coveredAreaDC = coveredAreaDc,
                    coveredAreaFBR = coveredAreaFbr,
                    khasraNumber = khasraNumber
                )



                try {
                    // Save the updated list in SharedPreferences
                    PreferencesManager.saveDropdownList(districtList)

                    // Save updated district-town map in SharedPreference
                    PreferencesManager.saveDistrictTownMap(districtTownMap)

                    // Save updated Town-Area map in SharedPreference
                    PreferencesManager.saveTownAreaMap(townAreaMap)

                    PreferencesManager.saveAreaLandMap(areaLandMap)

                    val currentValues = PreferencesManager.getLandOptionRates()

                    currentValues[finalKey] = landValues

                    PreferencesManager.saveLandRates(currentValues)


                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()



                    Log.d("SaveSuccess", "Land rates saved successfully! Data: $currentValues, $finalKey")
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("SaveError", "Error saving land rates", e)
                }
            }
            .setNegativeButton("BACK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.window?.setDimAmount(0.8f)
        dialog.show()
    }







//    Save the lists when save button is pressed
    binding.saveButton.setOnClickListener {
        // Log the data for debugging
        Log.d("distList", "District List: $districtList+$townList+$areaList")


        // Show confirmation dialog
        val saveAllDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Save")
            .setMessage(
                "Are you sure you want to save the above data?\n\n"
            )
            .setPositiveButton("Save") { _, _ ->

                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Close the dialog without saving
            }
            .create()

        // Make the background dim darker
        val window = saveAllDialog.window
        window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

        saveAllDialog.show()
    }
}

    private fun handleBackPress() {
        val districtTownMap = PreferencesManager.getDistrictTownMap()
        val townAreaMap = PreferencesManager.getTownAreaMap()

        if (binding.districtDropdown.text.toString().isNotEmpty() ||
            binding.townDropdown.text.toString().isNotEmpty() ||
            binding.propertyAreaDropdown.text.toString().isNotEmpty()) {

            val districtKey = binding.districtDropdown.text.toString().trim().hashCode()
            val townName = binding.townDropdown.text.toString().trim()
            val areaName = binding.propertyAreaDropdown.text.toString().trim()
            val townKey = "$districtKey-$townName".hashCode()

            // Properly checking if the town exists in the districtTownMap
            val townExists = districtTownMap[districtKey]?.contains(townName) ?: false
            val areaExists = townAreaMap[townKey]?.contains(areaName) ?: false

            if (!townExists || !areaExists) {
                showUnsavedChangesDialog()
            } else {
                finish() // Go back normally if town and area exist
            }
        } else {
            finish() // Go back normally if no input fields are filled
        }
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Unsaved Changes")
            .setMessage("Unchanged data would be lost. Are you sure you want to go back?")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null) // Dismiss dialog
            .show()
    }


//    End of OnCreate


    override fun onResume() {
        super.onResume()

        setupDropdowns()

    }
//    End of OnResume

    private fun addRippleEffect(view: View, scaleFactor: Float = 1.1f, duration: Long = 1000L) {
        // Scale X Animation
        val rippleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scaleFactor).apply {
            this.duration = duration
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }

        // Scale Y Animation
        val rippleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scaleFactor).apply {
            this.duration = duration
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }

        // Play Animations Together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rippleAnimatorX, rippleAnimatorY)
        animatorSet.start()
    }

//    private fun dropdownButtons(){
//
//        val districtAddIcon = binding.addDestrictIcon
//        val districtDropDown = binding.districtDropdown
//        val townDropDown = binding.townDropdown
//        val areaDropDown = binding.propertyAreaDropdown
//        val selectedLandType = ""
//
//        val districtTownMap = PreferencesManager.getDistrictTownMap()
//        val townAreaMap = PreferencesManager.getTownAreaMap()
//
//
//
//        districtAddIcon.setOnClickListener {
//            // Close the keyboard
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//            // Check if the dropdown has text written
//            val currentDistrictName = districtDropDown.text.toString().trim()
//
//
//            if (currentDistrictName.isEmpty()) {
//                // Show a message if the dropdown is empty
//                Toast.makeText(this, "Empty District!", Toast.LENGTH_SHORT).show()
//            } else {
//                // Proceed with the dialog to confirm adding the district
//                val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
//                val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
//
//                // Set the message text
//                addMessage.text = "Add '$currentDistrictName' to the District List?"
//
//                // Create a confirmation dialog
//                val addDistrictDialog = MaterialAlertDialogBuilder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("Add") { _, _ ->
//                        when {
//                            districtList.contains(currentDistrictName) -> {
//                                Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
//                            }
//                            else -> {
//
//                                // Add the new district to the list
//                                districtList.add(currentDistrictName)
//
//                                // Refresh adapter by recreating it
//                                districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
//                                districtDropDown.setAdapter(districtAdapter)
//
//
//                                // Normalize the selected district to avoid formatting issues
//                                val currentDistrictHashKey = currentDistrictName.hashCode()
//
//
//                                // Retrieve the towns for the selected district using the district hash key
//                                val towns = districtTownMap[currentDistrictHashKey] ?: mutableListOf()
//
//
//                                // Update the town dropdown
//                                townList = towns
//                                townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
//                                townDropDown.setAdapter(townAdapter)
//                                townDropDown.threshold = 1
//
//
//                                townDropDown.text.clear() // Clear towns dropdown
//                                areaDropDown.text.clear() // Clear areas dropdown
//
//
//                                Toast.makeText(this, "District added successfully", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    .create()
//
//                // Show the dialog
//                addDistrictDialog.setOnShowListener {
//                    val positiveButtonAdd = addDistrictDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    val negativeButtonAdd = addDistrictDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                    // Load the custom font
//                    val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                    // Apply the font to the buttons
//                    positiveButtonAdd.typeface = customFont
//                    negativeButtonAdd.typeface = customFont
//
//                    // Optional: Set text color or style
//                    positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green)) // For "Add"
//                    positiveButtonAdd.textSize = 16f
//                    negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey)) // For "Cancel"
//                    negativeButtonAdd.textSize = 16f
//
//                    // Make the background dim darker
//                    val window = addDistrictDialog.window
//                    window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//                }
//
//                addDistrictDialog.show()
//            }
//        }
//
//
//
//
//
//
//        val townAddIcon = binding.addTownIcon // Assuming town add icon ID
//
//        townAddIcon.setOnClickListener {
//            // Close the keyboard
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//            val currentDistrictName = districtDropDown.text.toString().trim()
//            val currentSelectedTown = townDropDown.text.toString().trim()
//
//
//            if (currentSelectedTown.isEmpty()) {
//                // Show a Toast message prompting the user to enter a town name
//                Toast.makeText(this, "Empty Town!", Toast.LENGTH_SHORT).show()
//            } else {
//                // Generate district hash code
//                val districtHashCode = currentDistrictName.hashCode()
//
//                // Generate town hash code (dependent on district hash code)
//                val townHashCode = "$districtHashCode-$currentSelectedTown".hashCode()
//
//                // Inflate the custom add alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
//                val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
//
//                // Set the message text and pre-fill the input field with the dropdown text
//                addMessage.text = "Add $currentSelectedTown for $currentDistrictName District?"
//
//// Create a confirmation dialog to add a new town
//                val addTownDialog = MaterialAlertDialogBuilder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("Add") { _, _ ->
//                        when {
//                            currentSelectedTown.isEmpty() -> {
//                                Toast.makeText(this, "Town cannot be empty", Toast.LENGTH_SHORT).show()
//                            }
//
//                            else -> {
//                                // Fetch the current list of towns for the selected district
//                                val towns = districtTownMap[districtHashCode] ?: mutableListOf()
//
//                                // Check if the town already exists
//                                if (towns.any { "$districtHashCode-${it}".hashCode() == townHashCode }) {
//                                    Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
//                                }else {
//                                    // Add the new town to the list
//                                    towns.add(currentSelectedTown)
//
//                                    // Update the districtTownMap
//                                    districtTownMap[districtHashCode] = towns
//
//                                    // Ensure the townDropDown reflects the new town
//                                    townDropDown.setText(currentSelectedTown, false)
//                                    townDropDown.setSelection(currentSelectedTown.length) // Move cursor to the end
//
//                                    // Update the town list and adapter
//                                    townList = towns
//                                    townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
//                                    townDropDown.setAdapter(townAdapter)
//
//                                    // Clear areas dropdown
//                                    areaDropDown.text.clear()
//                                    areaList = mutableListOf()
//                                    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
//                                    areaDropDown.setAdapter(areaAdapter)
//
//
//                                    Toast.makeText(this, "Town added successfully", Toast.LENGTH_SHORT).show()
//                                }
//                                Log.d("TownList", "Updated town list for district $districtHashCode: $townList")
//                            }
//                        }
//                    }
//
//
//
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    .create()
//
//                // Customize dialog button styles
//                addTownDialog.setOnShowListener {
//                    val positiveButtonAdd = addTownDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    val negativeButtonAdd = addTownDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                    val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                    positiveButtonAdd.typeface = customFont
//                    negativeButtonAdd.typeface = customFont
//
//                    positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green))
//                    positiveButtonAdd.textSize = 16f
//                    negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey))
//                    negativeButtonAdd.textSize = 16f
//                    // Make the background dim darker
//                    val window = addTownDialog.window
//                    window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//                }
//
//                addTownDialog.show()
//            }
//        }
//
//
//
//
//
//
//
//
//        // Logic for Adding New Property Area
//
//        val areaAddIcon = binding.addAreaIcon // Assuming area add icon ID
//        areaAddIcon.setOnClickListener {
//            // Close the keyboard
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//            val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
//            val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
//
//            val currentDistrictHashKey = districtDropDown.text.toString().trim().hashCode()
//
//            val currentSelectedTown = townDropDown.text.toString().trim()
//            val currentTownHashKey = "$currentDistrictHashKey-$currentSelectedTown".hashCode()
//
//            val currentArea = areaDropDown.text.toString().trim()
//            val currentAreaHashKey = "$currentDistrictHashKey-$currentTownHashKey-$currentArea".hashCode()
//
//            if (currentArea.isEmpty()) {
//                // Close the keyboard
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//                // Show Toast message
//                Toast.makeText(this, "Select a Town first", Toast.LENGTH_SHORT).show()
//            } else {
//                // Set the message text
//                addMessage.text = "Add $currentArea for $currentSelectedTown Town?"
//
//                // Create a confirmation dialog to add a new property area
//                val addAreaDialog = MaterialAlertDialogBuilder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("Add") { _, _ ->
//                        when {
//                            currentArea.isEmpty() -> {
//                                Toast.makeText(
//                                    this,
//                                    "Property Area cannot be empty",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//
//                            else -> {
//                                // Add the new area to the town's list in the map
//                                val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()
//                                if (areas.contains(currentArea)) {
//                                    Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
//                                } else {
//                                    areas.add(currentArea) // Add the new area to the list
//
//                                    // Update the townAreaMap
//                                    townAreaMap[currentTownHashKey] = areas
//
//                                    // Update the area list and adapter for the current town
//                                    areaList = areas
//                                    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
//                                    areaDropDown.setAdapter(areaAdapter)
//
//                                    // Set the new area as the selected text
//                                    areaDropDown.setText(currentArea, false)
//                                    areaDropDown.setSelection(currentArea.length)
//
//                                    // Provide feedback
//                                    Toast.makeText(
//                                        this,
//                                        "Property Area added successfully",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//
//                                    Log.d("AreaList", "Updated area list for $currentTownHashKey: $areaList")
//                                }
//                            }
//                        }
//                    }
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    .create()
//
//                // Customize dialog button styles
//                addAreaDialog.setOnShowListener {
//                    val positiveButtonAdd = addAreaDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    val negativeButtonAdd = addAreaDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                    // Load the custom font
//                    val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                    // Apply the font to the buttons
//                    positiveButtonAdd.typeface = customFont
//                    negativeButtonAdd.typeface = customFont
//
//                    // Set text color and size
//                    positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green))
//                    positiveButtonAdd.textSize = 16f
//                    negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey))
//                    negativeButtonAdd.textSize = 16f
//                    // Make the background dim darker
//                    val window = addAreaDialog.window
//                    window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//                }
//
//                addAreaDialog.show()
//            }
//        }
//
//
//
////        --------------- Add district/Town/Area Logic Above-------------
//
//
//// ============= Edit District/Town Logic Below ================
//
//
//
//
//        val districtEditIcon = binding.editDistrictIcon
//        districtEditIcon.setOnClickListener {
//            val oldDistrictName = districtDropDown.text.toString().trim()
//            val oldDistrictHashKey = oldDistrictName.hashCode()
//            val currentSelectedTown = townDropDown.text.toString().trim()
//            val currentArea = areaDropDown.text.toString().trim()
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//
//            if (oldDistrictName.isNotEmpty() && districtList.contains(oldDistrictName)) {
//                // Inflate the custom edit alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
//                val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
//                val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)
//
//                // Set the Edit Text Hint
//                inputEditText.hint = "Edit District Name"
//
//                val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)
//
//                // Set the message text
//                editMessage.text = "Edit the name of the selected district"
//
//                // Set the EditText initial value
//                inputEditText.setText(oldDistrictName)
//                inputEditText.setSelection(oldDistrictName.length) // Move cursor to the end of the text
//
//                // Set the title dynamically
//                centeredTitle.text = "District   ''$oldDistrictName''"
//
//                // Create a confirmation dialog to edit the selected district
//                val editDistrictDialog = MaterialAlertDialogBuilder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("Save") { _, _ ->
//                        val newDistrictName = inputEditText.text.toString().trim()
//
//                        when {
//                            newDistrictName.isEmpty() -> {
//                                Toast.makeText(this, "District cannot be empty", Toast.LENGTH_SHORT).show()
//                            }
//                            newDistrictName == oldDistrictName -> {
//                                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
//                            }
//                            districtList.contains(newDistrictName) -> {
//                                Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
//                            }
//                            else -> {
//                                // Hash-based key for district (UNIQUE KEYS)
//                                val newDistrictHashKey = newDistrictName.hashCode()
//
//                                // Update the district name in the list using hash-based keys
//                                val index = districtList.indexOf(oldDistrictName)
//
//                                if (index != -1) {
//                                    districtList[index] = newDistrictName
//                                }
//
//                                // Update the district in the map using hash codes to avoid dependency issues
//                                val towns = districtTownMap.remove(oldDistrictHashKey) ?: mutableListOf()
//                                districtTownMap[newDistrictHashKey] = towns
//
//
//
//
//
//                                // Update ALL town-area mappings for this district
//                                towns.forEach { townName ->
//                                    val oldTownHashKey = "$oldDistrictHashKey-$townName".hashCode()
//                                    val newTownHashKey = "$newDistrictHashKey-$townName".hashCode()
//
//
//
//                                    // Transfer areas from old town hash to new town hash
//                                    val areas = townAreaMap.remove(oldTownHashKey) ?: mutableListOf()
//                                    townAreaMap[newTownHashKey] = areas
//
//                                    areaList = areas
//                                    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
//                                    areaDropDown.setAdapter(areaAdapter)
//                                    areaDropDown.threshold = 1
//                                }
//
//                                // Generate the key for this new district selection
//                                val oldFinalKey = generateFinalKey(oldDistrictName, currentSelectedTown, currentArea, selectedLandType)
//                                val newFinalKey = generateFinalKey(newDistrictName, currentSelectedTown, currentArea, selectedLandType)
//
//                                Log.d("newFinalKey", "newFinalKey: $oldFinalKey, $newFinalKey")
//
//                                val currentValues = PreferencesManager.getLandOptionRates()
//                                val existingLandValues = currentValues[oldFinalKey] ?: return@setPositiveButton
//                                currentValues.remove(oldFinalKey) // Remove old key
//                                currentValues[newFinalKey] = existingLandValues // Add with new key
//                                PreferencesManager.saveLandRates(currentValues)
//
//
//                                districtDropDown.setText(newDistrictName, false)
//                                districtDropDown.setSelection(newDistrictName.length) // Move cursor to the end
//
//                                townDropDown.text.clear() // Clear dropdown text.
//                                areaDropDown.text.clear() // Clear dropdown text.
//
//
//                                // Refresh adapter by recreating it
//                                districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
//                                districtDropDown.setAdapter(districtAdapter)
//
//
//                                townList = towns
//                                townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
//                                townDropDown.setAdapter(townAdapter)
//                                townDropDown.threshold = 1
//
//
//
//                                Log.d("newDistrictList", "After district list: $newDistrictName")
//                                Log.d("townList", "After town list: $townList")
//                                Toast.makeText(this, "District updated successfully", Toast.LENGTH_SHORT).show()
//
//
//
//                            }
//                        }
//                        // Close the keyboard
//                        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//                    }
//
//
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    .create() // Call create() here only once
//
//                // Show the dialog
//                editDistrictDialog.setOnShowListener {
//                    val positiveButtonEdit = editDistrictDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    val negativeButtonEdit = editDistrictDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                    // Load the custom font
//                    val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                    // Apply the font to the buttons
//                    positiveButtonEdit.typeface = customFont
//                    negativeButtonEdit.typeface = customFont
//
//                    // Optional: Set text color or style
//                    positiveButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_green)) // For "Save"
//                    positiveButtonEdit.textSize = 16f
//                    negativeButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
//                    negativeButtonEdit.textSize = 16f
//                    // Make the background dim darker
//                    val window = editDistrictDialog.window
//                    window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//                    // Use a Handler to ensure the keyboard opens after the dialog is fully visible
//                    inputEditText.postDelayed({
//                        inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
//                    }, 100) // Delay by 100ms
//
//                }
//                editDistrictDialog.show()
//            } else {
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//                Toast.makeText(this, "Add or Select District to edit", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//        val townEditIcon = binding.editTownIcon
//        townEditIcon.setOnClickListener {
//            val currentSelectedDistrict = districtDropDown.text.toString().trim()
//            val currentTownName = townDropDown.text.toString().trim()
//            val currentArea = areaDropDown.text.toString().trim()
//
//
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//            if (currentTownName.isNotEmpty() && townList.contains(currentTownName)) {
//                // Inflate the custom edit alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
//                val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
//                val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)
//                // Set the Edit Text Hint
//                inputEditText.hint = "Edit Town Name"
//                val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)
//
//                // Set the message text
//                editMessage.text = "Edit the name of the selected town"
//
//                // Set the EditText initial value
//                inputEditText.setText(currentTownName)
//                inputEditText.setSelection(currentTownName.length) // Move cursor to the end of the text
//
//                // Set the title dynamically
//                centeredTitle.text = "Town   ''$currentTownName''"
//
//                // Create a confirmation dialog to edit the selected district
//                val editTownDialog = MaterialAlertDialogBuilder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("Save") { _, _ ->
//
//                        val newTownName = inputEditText.text.toString().trim()
//
//                        when {
//                            newTownName.isEmpty() -> {
//                                Toast.makeText(this, "Town cannot be empty", Toast.LENGTH_SHORT).show()
//                            }
//                            newTownName == currentTownName -> {
//                                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
//                            }
//                            townList.contains(newTownName) -> {
//                                Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
//                            }
//                            else -> {
//                                // Hash-based keys for town (UNIQUE KEYS)
//                                val districtHashCode = currentSelectedDistrict.hashCode()
//                                val oldTownHashCode = "$districtHashCode-$currentTownName".hashCode()
//                                val newTownHashCode = "$districtHashCode-$newTownName".hashCode()
//
//                                // Fetch the current list of towns for the selected district
//                                val towns = districtTownMap[districtHashCode] ?: mutableListOf()
//
//                                // Check if the new town name already exists (excluding the current town)
//                                if (towns.any { it != currentTownName && "$districtHashCode-${it}".hashCode() == newTownHashCode }) {
//                                    Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
//                                } else {
//                                    // Update the town name in the list
//                                    val index = towns.indexOf(currentTownName)
//                                    if (index != -1) {
//                                        towns[index] = newTownName
//                                    }
//
//                                    // Update the districtTownMap
//                                    districtTownMap[districtHashCode] = towns
//
//                                    // Transfer areas from old town hash to new town hash
//                                    val areas = townAreaMap.remove(oldTownHashCode) ?: mutableListOf()
//                                    townAreaMap[newTownHashCode] = areas
//
//                                    // Update preferences with new keys
//                                    val oldFinalKey = generateFinalKey(currentSelectedDistrict, currentTownName, currentArea, selectedLandType)
//                                    val newFinalKey = generateFinalKey(currentSelectedDistrict, newTownName, currentArea, selectedLandType)
//
//                                    val currentValues = PreferencesManager.getLandOptionRates()
//                                    val existingLandValues = currentValues[oldFinalKey] ?: return@setPositiveButton
//                                    currentValues.remove(oldFinalKey)
//                                    currentValues[newFinalKey] = existingLandValues
//                                    PreferencesManager.saveLandRates(currentValues)
//
//                                    // Update UI elements
//                                    townDropDown.setText(newTownName, false)
//                                    townDropDown.setSelection(newTownName.length)
//
//                                    // Update the town list and adapter
//                                    townList = towns
//                                    townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
//                                    townDropDown.setAdapter(townAdapter)
//                                    townDropDown.threshold = 1
//
//                                    // Update area dropdown with transferred areas
//                                    areaDropDown.text.clear()
//                                    areaList = areas
//                                    areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
//                                    areaDropDown.setAdapter(areaAdapter)
//                                    areaDropDown.threshold = 1
//
//                                    Log.d("TownList", "Updated town list for district $districtHashCode: $townList")
//                                    Log.d("Areas", "Transferred areas for town $newTownName: $areas")
//
//                                    Toast.makeText(this, "Town updated successfully", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        }
//                    }
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    .create() // Call create() here only once
//
//                // Show the dialog
//                editTownDialog.setOnShowListener {
//                    val positiveButtonEdit = editTownDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    val negativeButtonEdit = editTownDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                    // Load the custom font
//                    val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                    // Apply the font to the buttons
//                    positiveButtonEdit.typeface = customFont
//                    negativeButtonEdit.typeface = customFont
//
//                    // Optional: Set text color or style
//                    positiveButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_green)) // For "Save"
//                    positiveButtonEdit.textSize = 16f
//                    negativeButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
//                    negativeButtonEdit.textSize = 16f
//                    // Make the background dim darker
//                    val window = editTownDialog.window
//                    window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//
//                    // Use a Handler to ensure the keyboard opens after the dialog is fully visible
//                    inputEditText.postDelayed({
//                        inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
//                    }, 100) // Delay by 100ms
//                }
//                editTownDialog.show()
//            } else {
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//                Toast.makeText(this, "Please select a town to edit", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//
//
//        val areaEditIcon = binding.editAreaIcon
//
//        areaEditIcon.setOnClickListener {
//            val currentSelectedDistrict = districtDropDown.text.toString().trim()
//            val currentSelectedTown = townDropDown.text.toString().trim()
//            val currentTownHashKey = "$currentSelectedDistrict-$currentSelectedTown".hashCode()
//            val currentAreaName = areaDropDown.text.toString().trim()
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//            if (currentAreaName.isNotEmpty() && areaList.contains(currentAreaName)) {
//                // Inflate the custom edit alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
//                val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
//                val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)
//                // Set the Edit Text Hint
//                inputEditText.hint = "Edit Property Area"
//                val centeredTitle = dialogView.findViewById<TextView>(R.id.centeredTitle)
//
//                // Set the message text
//                editMessage.text = "Edit the name of the selected area"
//
//                // Set the EditText initial value
//                inputEditText.setText(currentAreaName)
//                inputEditText.setSelection(currentAreaName.length) // Move cursor to the end of the text
//
//                // Set the title dynamically
//                centeredTitle.text = "Property Area ''$currentAreaName''"
//
//// Create a confirmation dialog to edit the selected district
//                val editAreaDialog = MaterialAlertDialogBuilder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("Save") { _, _ ->
//                        val newAreaName = inputEditText.text.toString().trim()
//
//                        when {
//                            newAreaName.isEmpty() -> {
//                                Toast.makeText(this, "Area cannot be empty", Toast.LENGTH_SHORT).show()
//                            }
//                            newAreaName == currentAreaName -> {
//                                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
//                            }
//                            areaList.contains(newAreaName) -> {
//                                Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
//                            }
//                            else -> {
//
//                                // Update the area in the list
//                                val index = areaList.indexOf(currentAreaName)
//                                if (index != -1) {
//                                    areaList[index] = newAreaName
//                                }
//
//                                // Update the area mapping in townAreaMap
//                                val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()
//                                val areaIndex = areas.indexOf(currentAreaName)
//                                if (areaIndex != -1) {
//                                    areas[areaIndex] = newAreaName
//                                    townAreaMap[currentTownHashKey] = areas
//                                }
//
//                                // Update preferences with new keys
//                                val oldFinalKey = generateFinalKey(currentSelectedDistrict, currentSelectedTown, currentAreaName, selectedLandType)
//                                val newFinalKey = generateFinalKey(currentSelectedDistrict, currentSelectedTown, newAreaName, selectedLandType)
//
//                                val currentValues = PreferencesManager.getLandOptionRates()
//                                val existingLandValues = currentValues[oldFinalKey] ?: return@setPositiveButton
//                                currentValues.remove(oldFinalKey)
//                                currentValues[newFinalKey] = existingLandValues
//                                PreferencesManager.saveLandRates(currentValues)
//
//                                // Update UI elements
//                                areaDropDown.setText(newAreaName, false)
//                                areaDropDown.setSelection(newAreaName.length)
//
//                                // Refresh adapter
//                                areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
//                                areaDropDown.setAdapter(areaAdapter)
//                                areaDropDown.threshold = 1
//
//                                Log.d("AreaList", "Updated area list for town $currentTownHashKey: $areaList")
//                                Log.d("Preferences", "Updated keys from $oldFinalKey to $newFinalKey")
//
//                                Toast.makeText(this, "Area updated successfully", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                    .setNegativeButton("Cancel") { _, _ -> }
//                    .create()
//
//                // Show the dialog
//                editAreaDialog.setOnShowListener {
//
//                    val positiveButtonEdit = editAreaDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                    val negativeButtonEdit = editAreaDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                    // Load the custom font
//                    val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                    // Apply the font to the buttons
//                    positiveButtonEdit.typeface = customFont
//                    negativeButtonEdit.typeface = customFont
//
//                    // Optional: Set text color or style
//                    positiveButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_green)) // For "Save"
//                    positiveButtonEdit.textSize = 16f
//                    negativeButtonEdit.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
//                    negativeButtonEdit.textSize = 16f
//
//                    // Make the background dim darker
//                    val window = editAreaDialog.window
//                    window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//                    // Use a Handler to ensure the keyboard opens after the dialog is fully visible
//                    inputEditText.postDelayed({
//                        inputMethodManager.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
//                    }, 100) // Delay by 100ms
//                }
//                editAreaDialog.show()
//            } else {
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//                Toast.makeText(this, "Please select property area to edit", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//// ================ Delete  district Logic Below ================
//
//
//
//
//        binding.deleteDistrictIcon.setOnClickListener {
//
//            val currentSelectedDistrict = districtDropDown.text.toString().trim()
//            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//
//            if (currentSelectedDistrict.isNotEmpty()) {
//                // Inflate the custom alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
//                val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
//                alertMessage.text = "Warning deleting ''$currentSelectedDistrict'' will delete all towns and areas associated with it as well!"
//
//                // Create a new title view for the dialog
//                val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
//                val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
//                centeredTitle.text = "Do you still want to delete this district?"
//
//                // Ensure that the titleView is not attached to any parent
//                if (titleView.parent != null) {
//                    (titleView.parent as ViewGroup).removeView(titleView)
//                }
//
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//                // Create a confirmation dialog to delete the selected district
//                val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
//                    .setView(titleView)
//                    .setCustomTitle(dialogView)
//
//                deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->
//
//                    // Check if the district exists in the list
//                    if (districtList.contains(currentSelectedDistrict)) {
//
//                        val currentDistrictHashKey = currentSelectedDistrict.hashCode()
//                        // Get all towns for this district
//                        val townsToDelete = districtTownMap[currentDistrictHashKey] ?: emptyList()
//
//                        // Remove district and its towns
//                        districtList.remove(currentSelectedDistrict)
//                        districtTownMap.remove(currentDistrictHashKey)
//
//                        // Remove areas for each town in this district
//                        townsToDelete.forEach { town ->
//                            val townKey = "$currentDistrictHashKey-$town".hashCode()
//                            townAreaMap.remove(townKey)  // Just remove, don't put back
//                        }
//
//
//
//                        districtDropDown.text.clear() // Clear dropdown text.
//                        townDropDown.text.clear() // Clear dropdown text.
//                        areaDropDown.text.clear() // Clear dropdown text.
//
//                        townAdapter.clear()
//                        areaAdapter.clear()
//
//
//                        // Refresh adapter by recreating it
//                        districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
//                        districtDropDown.setAdapter(districtAdapter)
//
//
//                        Log.d("DistrictList", "Deleted district list: $districtList, $districtTownMap, $townList, $townAreaMap ,$areaList")
//                        Toast.makeText(this, "District deleted successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Selected district does not exist", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                deleteConfirmationDialog.setNegativeButton("Cancel") { _, _ -> }
//
//                // Show the dialog
//                val dialog = deleteConfirmationDialog.show()
//
//                // Access the positive and negative buttons after the dialog is shown
//                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                // Load the custom font
//                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                // Apply the font to the buttons
//                positiveButton.typeface = customFont
//                negativeButton.typeface = customFont
//
//                // Optional: Set text color or style
//                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_red)) // For "Delete"
//                positiveButton.textSize = 16f // For "Delete")
//                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
//                negativeButton.textSize = 16f
//                // Make the background dim darker
//                val window = dialog.window
//                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//            } else {
//                inputMethodManager.hideSoftInputFromWindow(districtDropDown.windowToken, 0)
//
//                // Show a Toast message if no district is selected
//                Toast.makeText(this, "Please select a district first", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//
//// ============= Delete Town Logic Below ================
//
//        binding.deleteTownIcon.setOnClickListener {
//
//            val currentSelectedTown = townDropDown.text.toString().trim()
//            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//            if (currentSelectedTown.isNotEmpty()) {
//                // Inflate the custom alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
//                val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
//                alertMessage.text = "Warning deleting ''$currentSelectedTown'' will delete all property areas associated with it as well!"
//
//                // Create a new title view for the dialog
//                val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
//                val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
//                centeredTitle.text = "Are you sure you want to delete this town?"
//
//                // Ensure that the titleView is not attached to any parent
//                if (titleView.parent != null) {
//                    (titleView.parent as ViewGroup).removeView(titleView)
//                }
//
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//
//                // Create a confirmation dialog to delete the selected district
//                val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
//                    .setView(titleView)
//                    .setCustomTitle(dialogView)
//
//                deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->
//
//                    val currentSelectedDistrict = districtDropDown.text.toString().trim()
//                    if (townList.contains(currentSelectedTown)) {
//                        val currentDistrictHashKey = currentSelectedDistrict.hashCode()
//                        val currentTownHashKey = "$currentDistrictHashKey-$currentSelectedTown".hashCode()
//
//                        // Remove the town from the townList and districtTownMap
//                        townList.remove(currentSelectedTown)
//                        val districtTowns = districtTownMap[currentDistrictHashKey] ?: mutableListOf()
//                        districtTowns.remove(currentSelectedTown)
//                        districtTownMap[currentDistrictHashKey] = districtTowns
//
//                        // Remove all areas associated with this town
//                        townAreaMap.remove(currentTownHashKey)  // This removes all areas for this town
//
//
//                        townDropDown.text.clear()
//                        areaDropDown.text.clear()
//                        areaAdapter.clear()
//
//                        // Refresh the town adapter
//                        townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
//                        townDropDown.setAdapter(townAdapter)
//                        // Clear the area dropdown and its adapter since the town was deleted
//
//                        Log.d("TownAreaMap", "Updated townAreaMap: $townAreaMap")
//
//                        Toast.makeText(this, "Town and its associated areas deleted successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Selected Town does not exist", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//
//                deleteConfirmationDialog.setNegativeButton("Cancel") { _, _ -> }
//
//                // Show the dialog
//                val dialog = deleteConfirmationDialog.show()
//
//                // Access the positive and negative buttons after the dialog is shown
//                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                // Load the custom font
//                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                // Apply the font to the buttons
//                positiveButton.typeface = customFont
//                negativeButton.typeface = customFont
//
//                // Optional: Set text color or style
//                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_red)) // For "Delete"
//                positiveButton.textSize = 16f // For "Delete")
//                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
//                negativeButton.textSize = 16f
//                // Make the background dim darker
//                val window = dialog.window
//                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//            } else {
//                inputMethodManager.hideSoftInputFromWindow(townDropDown.windowToken, 0)
//
//                // Show a Toast message if no district is selected
//                Toast.makeText(this, "Please select a town first", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // ============= Delete Property Area Logic Below ================
//
//        binding.deleteAreaIcon.setOnClickListener {
//
//            val currentSelectedArea = areaDropDown.text.toString().trim()
//            if (currentSelectedArea.isNotEmpty()) {
//                // Inflate the custom alert view for the dialog
//                val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
//                val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
//                alertMessage.text = "Are you sure you want to delete this Area?"
//
//                // Create a new title view for the dialog
//                val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
//                val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
//                centeredTitle.text = "Property Area   ''$currentSelectedArea''"
//
//                // Ensure that the titleView is not attached to any parent
//                if (titleView.parent != null) {
//                    (titleView.parent as ViewGroup).removeView(titleView)
//                }
//
//                // Create a confirmation dialog to delete the selected district
//                val deleteConfirmationDialog = MaterialAlertDialogBuilder(this)
//                    .setView(titleView)
//                    .setCustomTitle(dialogView)
//
//                deleteConfirmationDialog.setPositiveButton("Delete") { _, _ ->
//                    // Check if the district exists in the list
//                    if (areaList.contains(currentSelectedArea)) {
//                        areaList.remove(currentSelectedArea)
//                        areaDropDown.setText("")
//
//                        // Refresh adapter by recreating it
//                        areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
//                        areaDropDown.setAdapter(areaAdapter)
//
//
//                        Log.d("AreaList", "Deleted town list: $areaList")
//                        Toast.makeText(this, "Area deleted successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Selected Area does not exist", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                deleteConfirmationDialog.setNegativeButton("Cancel") { _, _ -> }
//
//                // Show the dialog
//                val dialog = deleteConfirmationDialog.show()
//
//                // Access the positive and negative buttons after the dialog is shown
//                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//                val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//
//                // Load the custom font
//                val customFont = ResourcesCompat.getFont(this, R.font.sf_pro_display_bold)
//
//                // Apply the font to the buttons
//                positiveButton.typeface = customFont
//                negativeButton.typeface = customFont
//
//                // Optional: Set text color or style
//                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_red)) // For "Delete"
//                positiveButton.textSize = 16f // For "Delete")
//                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.teal_700)) // For "Cancel"
//                negativeButton.textSize = 16f
//                // Make the background dim darker
//                val window = dialog.window
//                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)
//
//            } else {
//                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(areaDropDown.windowToken, 0)
//
//                // Show a Toast message if no district is selected
//                Toast.makeText(this, "Please select area first", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }

    private fun setupDropdowns() {

        // District Dropdown Setup
        districtList = PreferencesManager.getDropdownList().toMutableList()
//        val districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
//        binding.districtDropdown.setAdapter(districtAdapter)

        // Initial load if we have selections
        val currentDistrict = binding.districtDropdown.text.toString().trim()
        if (currentDistrict.isNotEmpty()) {
            val currentDistrictHashKey = currentDistrict.hashCode()

            // Load towns
            val districtTownMap = PreferencesManager.getDistrictTownMap()
            townList = districtTownMap[currentDistrictHashKey] ?: mutableListOf()
//            val townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
//            binding.townDropdown.setAdapter(townAdapter)
//            binding.townDropdown.threshold = 1

            // Load areas using the same hash key pattern as settings
            val currentTown = binding.townDropdown.text.toString().trim()
            if (currentTown.isNotEmpty()) {
                val currentTownHashKey = "$currentDistrictHashKey-$currentTown".hashCode()
                val townAreaMap = PreferencesManager.getTownAreaMap()

                // Use the exact same pattern as in settings
                val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()
                areaList = areas
                val areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                binding.propertyAreaDropdown.setAdapter(areaAdapter)
                binding.propertyAreaDropdown.threshold = 1

                Log.d("AreasLoaded", "Current areas for $currentTown: $areas")
            }
        }

        // District Selection Listener
        binding.districtDropdown.setOnItemClickListener { _, _, _, _ ->
            val selectedDistrict = binding.districtDropdown.text.toString().trim()
            val districtHashKey = selectedDistrict.hashCode()

            binding.townDropdown.text.clear()
            binding.propertyAreaDropdown.text.clear()

            val districtTownMap = PreferencesManager.getDistrictTownMap()
            townList = districtTownMap[districtHashKey] ?: mutableListOf()

            val townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
            binding.townDropdown.setAdapter(townAdapter)
            binding.townDropdown.threshold = 1
//            binding.townDropdown.requestFocus()
        }

        // Town Selection Listener
        binding.townDropdown.setOnItemClickListener { _, _, _, _ ->
            // Use the exact same hash key pattern as in settings
            val currentDistrictHashKey = binding.districtDropdown.text.toString().trim().hashCode()
            val currentTownName = binding.townDropdown.text.toString().trim()
            val currentTownHashKey = "$currentDistrictHashKey-$currentTownName".hashCode()

            binding.propertyAreaDropdown.text.clear()

            val townAreaMap = PreferencesManager.getTownAreaMap()
            val areas = townAreaMap[currentTownHashKey] ?: mutableListOf()

            // Update area list and adapter
            areaList = areas
            val areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
            binding.propertyAreaDropdown.setAdapter(areaAdapter)
            binding.propertyAreaDropdown.threshold = 1
//            binding.propertyAreaDropdown.requestFocus()

            Log.d("AreasLoaded", "Areas for $currentTownName: $areas")
        }


    }




    private fun generateFinalKey(districtName: String, town: String, area: String, landType: String): Int {
        val districtHashKey = districtName.hashCode()
        val townHashKey = "$districtHashKey-$town".hashCode()
        val areaHashKey = "$townHashKey-$area".hashCode()
        return "$areaHashKey-$landType".hashCode()
    }





}