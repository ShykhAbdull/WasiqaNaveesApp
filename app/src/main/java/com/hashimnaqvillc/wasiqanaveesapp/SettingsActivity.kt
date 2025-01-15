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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
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

private lateinit var selectedDistrict: String


private lateinit var binding: ActivitySettingsBinding

@SuppressLint("ClickableViewAccessibility", "InflateParams")
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
binding = ActivitySettingsBinding.inflate(layoutInflater)
setContentView(binding.root)

//    ------------------------------------------------------ UI CHANGES -------------------------------------------------------------------

    addRippleEffect(binding.headingTextSettings)
    addRippleEffect(binding.taxRateBtn)





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

//    ------------------------------------------UI CHANGES ----------------------------------------------------------


//    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

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

// Sample data for the dropdown
districtList = PreferencesManager.getDropdownList()
districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
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


    // Fetch the districtTownMap once when the activity starts
    val districtTownMap: MutableMap<String, MutableList<String>> by lazy {
        PreferencesManager.getDistrictTownMap()
    }

// Dropdown item selection logic
    districtDropDown.setOnItemClickListener { _, _, _, _ ->

        // Also clear dependent dropdown fields for Town and Area
        townDropDown.text.clear()
        areaDropDown.text.clear()


        // Normalize the selected district to avoid formatting issues
        selectedDistrict = districtDropDown.text.toString().trim()

        // Retrieve the towns for the selected district
        val towns = districtTownMap[selectedDistrict] ?: mutableListOf()

        // Log the retrieved data
        Log.d("DistrictSelection", "Selected district: $selectedDistrict, towns: $towns")

        // Update the town dropdown
        townList = towns
        townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
        townDropDown.setAdapter(townAdapter)
        townDropDown.threshold = 1


        // Log the cleared selections
        Log.d("DistrictSelection", "Cleared previous town and area selections.")
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



    // Fetch the TownAreaMap once when the activity starts
    val townAreaMap: MutableMap<String, MutableList<String>> by lazy {
        PreferencesManager.getTownAreaMap()
    }

// On Town Selection
    townDropDown.setOnItemClickListener { _, _, _, _ ->

        areaDropDown.text.clear()


        val selectedTown = townDropDown.text.toString().trim()
        // Clear and reset areas
        val areas = townAreaMap[selectedTown] ?: mutableListOf()
        areaList = areas
        areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
        areaDropDown.setAdapter(areaAdapter)


        Log.d("TownSelection", "Selected town: $selectedTown, areas: $areas")
    }



    // Fetch the AreaLandMap once when the activity starts
    val areaLandMap: MutableMap<String, MutableList<String>> by lazy {
        PreferencesManager.getAreaLandMap()
    }


//    // On Area Selection
//    areaDropDown.setOnItemClickListener { _, _, _, _ ->
//
//        // Get the selected area from the dropdown
//        val selectedArea = areaDropDown.text.toString().trim()
//
//        // Reset landList with fixed 4 options
//
//        // Check if the area already has saved land types and remove them from the options
//        val savedLandTypes = areaLandMap[selectedArea] ?: mutableListOf()
//        landList.removeAll(savedLandTypes)
//
//        // Create and set the adapter for landTypeDropdown
//        landAdapter = ArrayAdapter(this, custom_dropdown_item, landList)
//
//
//    }








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




    //    When the town text clears all other field text clear as well
    binding.districtInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        districtDropDown.text.clear()
        townDropDown.text.clear()
        areaDropDown.text.clear()

    }

    //    When the town text clears all other field text clear as well
    binding.townInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        townDropDown.text.clear()
        areaDropDown.text.clear()


    }

    //    When the town text clears all other field text clear as well
    binding.propertyAreaInputLayout.setEndIconOnClickListener {
        // Also clear dependent dropdown fields for Town and Area
        areaDropDown.text.clear()
    }

//--------------------------------------------------------------

    districtAddIcon.setOnClickListener {
        // Close the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        // Check if the dropdown has text written
        val currentDistrictName = districtDropDown.text.toString().trim()

        if (currentDistrictName.isEmpty()) {
            // Show a message if the dropdown is empty
            Toast.makeText(this, "Empty District!", Toast.LENGTH_SHORT).show()
        } else {
            // Proceed with the dialog to confirm adding the district
            val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
            val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)

            // Set the message text
            addMessage.text = "Add '$currentDistrictName' to the District List?"

            // Create a confirmation dialog
            val addDistrictDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    when {
                        districtList.contains(currentDistrictName) -> {
                            Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {

                            // Add the new district to the list
                            districtList.add(currentDistrictName)

                            // Refresh adapter by recreating it
                            districtAdapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                            districtDropDown.setAdapter(districtAdapter)

                            townDropDown.text.clear() // Clear towns dropdown
                            areaDropDown.text.clear() // Clear areas dropdown

                            // Clear and reset towns and areas
                            val towns = districtTownMap[currentDistrictName] ?: mutableListOf()
                            townList = towns
                            townAdapter = ArrayAdapter(this, custom_dropdown_item, townList)
                            townDropDown.setAdapter(townAdapter)


                            Log.d("DistrictList", "Updated district list: $districtList")
                            Toast.makeText(this, "District added successfully", Toast.LENGTH_SHORT).show()
                        }
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
                positiveButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_light_green)) // For "Add"
                positiveButtonAdd.textSize = 16f
                negativeButtonAdd.setTextColor(ContextCompat.getColor(this, R.color.wasiqa_dark_grey)) // For "Cancel"
                negativeButtonAdd.textSize = 16f

                // Make the background dim darker
                val window = addDistrictDialog.window
                window?.setDimAmount(0.8f) // Adjust the dim level (default is 0.5f)

            }

            addDistrictDialog.show()
        }
    }






    val townAddIcon = binding.addTownIcon // Assuming town add icon ID

    townAddIcon.setOnClickListener {
        // Close the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        selectedDistrict = districtDropDown.text.toString().trim()
        val selectedTown = townDropDown.text.toString().trim()

        if (selectedTown.isEmpty()) {
            // Show a Toast message prompting the user to enter a town name
            Toast.makeText(this, "Empty Town!", Toast.LENGTH_SHORT).show()
        } else {
            // Inflate the custom add alert view for the dialog
            val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
            val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)

            // Set the message text and pre-fill the input field with the dropdown text
            addMessage.text = "Add $selectedTown for $selectedDistrict District?"

            // Create a confirmation dialog to add a new town
            val addTownDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val newTownName = selectedTown // Get the text from the dropdown

                    when {
                        newTownName.isEmpty() -> {
                            Toast.makeText(
                                this,
                                "Town cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // Add the new town to the district's list in the map
                            val towns = districtTownMap.getOrPut(selectedDistrict) { mutableListOf() }
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
    }








    // Logic for Adding New Property Area

    val areaAddIcon = binding.addAreaIcon // Assuming area add icon ID
    areaAddIcon.setOnClickListener {
        // Close the keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
        val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)

        val selectedTown = townDropDown.text.toString().trim()

        val selectedArea = areaDropDown.text.toString().trim()

        if (selectedArea.isEmpty()) {
            // Close the keyboard
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            // Show Toast message
            Toast.makeText(this, "Select a Town first", Toast.LENGTH_SHORT).show()
        } else {
            // Set the message text
            addMessage.text = "Add $selectedArea for $selectedTown Town?"

            // Create a confirmation dialog to add a new property area
            val addAreaDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    when {
                        selectedArea.isEmpty() -> {
                            Toast.makeText(
                                this,
                                "Property Area cannot be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // Add the new area to the town's list in the map
                            val area = townAreaMap.getOrPut(selectedTown) { mutableListOf() }
                            if (area.contains(selectedArea)) {
                                Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                area.add(selectedArea) // Add the new area to the list

                                // Update the area list and adapter for the current town
                                areaList = area
                                areaAdapter = ArrayAdapter(this, custom_dropdown_item, areaList)
                                areaDropDown.setAdapter(areaAdapter)

                                // Set the new area as the selected text
                                areaDropDown.setText(selectedArea, false)
                                areaDropDown.setSelection(selectedArea.length)

                                // Provide feedback
                                Toast.makeText(
                                    this,
                                    "Property Area added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Log.d("AreaList", "Updated area list for $selectedTown: $areaList")
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
    }



//        --------------- Add district/Town/Area Logic Above-------------


// ============= Edit District/Town Logic Below ================


val districtEditIcon = binding.editDistrictIcon

districtEditIcon.setOnClickListener {
    selectedDistrict = districtDropDown.text.toString().trim()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


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
                        Toast.makeText(this, "District cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    newDistrictName == selectedDistrict -> {
                        Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                    }
                    districtList.contains(newDistrictName) -> {
                        Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
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
        val selectedTown = townDropDown.text.toString().trim()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
                            Toast.makeText(this, "Town cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        newTownName == selectedTown -> {
                            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                        }
                        townList.contains(newTownName) -> {
                            Toast.makeText(this, "Town already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {

                            // Close the keyboard
                            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

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
        val selectedArea = areaDropDown.text.toString().trim()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
                            Toast.makeText(this, "Area cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        newAreaName == selectedArea -> {
                            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
                        }
                        areaList.contains(newAreaName) -> {
                            Toast.makeText(this, "Area already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Close the keyboard
                            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

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


// ============= Delete  district Logic Below ================




binding.deleteDistrictIcon.setOnClickListener {

    selectedDistrict = districtDropDown.text.toString().trim()
    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

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

        val selectedTown = townDropDown.text.toString().trim()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
        val landTypeDropdown = dialogView.findViewById<AutoCompleteTextView>(R.id.landTypeDropdownDialogDisplay)
        val selectedArea = areaDropDown.text.toString().trim()
        landTypeDropdown.setOnClickListener {
            landTypeDropdown.showDropDown()
            landTypeDropdown.requestFocus()
        }

        // Validate selectedArea
        if (selectedArea.isEmpty()) {
            Toast.makeText(this, "Please select an area first.", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        // Fetch and filter dropdown options dynamically
        val savedLandTypes = PreferencesManager.getLandRates()[selectedArea]?.keys ?: emptySet()
        val landOptions = mutableListOf("Agriculture", "Residential", "Commercial", "Industrial")
        val availableLandOptions = landOptions.filter { it !in savedLandTypes }
        val landAdapter = ArrayAdapter(this, custom_dropdown_item, availableLandOptions)
        landTypeDropdown.setAdapter(landAdapter)

        // Prepopulate fields if land type already exists
        landTypeDropdown.setOnItemClickListener { _, _, position, _ ->
            val landType = availableLandOptions[position]
            val existingLandRates = PreferencesManager.getLandRates()[selectedArea]?.get(landType)
            existingLandRates?.let {
                dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.setText(it["MarlaDC"])
                dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.setText(it["MarlaFBR"])
                dialogView.findViewById<EditText>(R.id.covereAreaDC)?.setText(it["CoveredAreaDC"])
                dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.setText(it["CoveredAreaFBR"])
                dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.setText(it["KhasraNumber"])
            }
        }

        // Create the dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val landType = landTypeDropdown.text.toString().trim()

                if (landType.isEmpty()) {
                    Toast.makeText(this, "Please select a land type.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Fetch values from EditText fields
                val marlaDc = dialogView.findViewById<EditText>(R.id.marlaUnitPriceDC)?.text.toString().trim()
                val marlaFbr = dialogView.findViewById<EditText>(R.id.marlaUnitPriceFBR)?.text.toString().trim()
                val coveredAreaDc = dialogView.findViewById<EditText>(R.id.covereAreaDC)?.text.toString().trim()
                val coveredAreaFbr = dialogView.findViewById<EditText>(R.id.covereAreaFBR)?.text.toString().trim()
                val khasraNumber = dialogView.findViewById<EditText>(R.id.khasraNumberEditText)?.text.toString().trim()

                if (marlaDc.isEmpty() || marlaFbr.isEmpty() || coveredAreaDc.isEmpty() || coveredAreaFbr.isEmpty()) {
                    Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Prepare and save data
                val areaLandRates = PreferencesManager.getLandRates()
                val landRatesData = mapOf(
                    "MarlaDC" to marlaDc,
                    "MarlaFBR" to marlaFbr,
                    "CoveredAreaDC" to coveredAreaDc,
                    "CoveredAreaFBR" to coveredAreaFbr,
                    "KhasraNumber" to khasraNumber
                )
                val updatedLandRates = (areaLandRates[selectedArea] ?: mutableMapOf()).apply {
                    put(landType, landRatesData)
                }
                areaLandRates[selectedArea] = updatedLandRates

                try {
                    PreferencesManager.saveLandRates(areaLandRates)
                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                    Log.d("SaveSuccess", "Land rates saved successfully! Data: $areaLandRates")
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("SaveError", "Error saving land rates", e)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
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

                // Save the updated list in SharedPreferences
                PreferencesManager.saveDropdownList(districtList)

                // Save updated district-town map in SharedPreference
                PreferencesManager.saveDistrictTownMap(districtTownMap)

                // Save updated Town-Area map in SharedPreference
                PreferencesManager.saveTownAreaMap(townAreaMap)

                // Save updated Area-Land map in SharedPreference
                PreferencesManager.saveAreaLandMap(areaLandMap)

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


}