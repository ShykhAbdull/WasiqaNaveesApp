package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivitySettingsBinding
import kotlin.properties.Delegates

class SettingsActivity : AppCompatActivity() {

    private lateinit var districtList: MutableList<String>
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var adapter: ArrayAdapter<String>



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        Logic for showing and Hiding the Views on Settings Activity

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

//        binding.resetButton.setOnClickListener {
//            resetDistrictList()
//            Toast.makeText(this, "District List Reset", Toast.LENGTH_SHORT).show()
//        }
//        binding.saveButton.setOnClickListener {
//            saveDistrictList()
//            Toast.makeText(this, "District List Saved", Toast.LENGTH_SHORT).show()
//        }


//        Logic for District DropDown

        // Get references to the TextInputLayout and AutoCompleteTextView
        val districtDropDown = binding.districtDropdown


        districtDropDown.setOnClickListener {
            districtDropDown.dismissDropDown()
            districtDropDown.showDropDown()
            districtDropDown.requestFocus()

        }
        // Handle dropdown option selection
        districtDropDown.setOnItemClickListener { _, _, _, _ ->
            // Close the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(districtDropDown.windowToken, 0)

            // Hide the cursor
            districtDropDown.isCursorVisible = false

            // Optionally, clear focus
            districtDropDown.clearFocus()
        }




        binding.townDropdown.setOnClickListener {
            binding.districtInputLayout.isEndIconVisible = false
        }

        // Optional: Disable default focus stealing by the clear_text icon
        districtDropDown.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                districtDropDown.showDropDown()
                binding.districtIconContainer.visibility = View.VISIBLE
                binding.townIconContainer.visibility = View.GONE
                binding.propertyAreaIconContainer.visibility = View.GONE
            }
        }

        // Optional: Disable default focus stealing by the clear_text icon
        binding.townDropdown.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                districtDropDown.showDropDown()
                binding.districtIconContainer.visibility = View.GONE
                binding.townIconContainer.visibility = View.VISIBLE
                binding.propertyAreaIconContainer.visibility = View.GONE
            }
        }

        // Optional: Disable default focus stealing by the clear_text icon
        binding.propertyAreaDropdown.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                districtDropDown.showDropDown()
                binding.townIconContainer.visibility = View.GONE
                binding.districtIconContainer.visibility = View.GONE
                binding.propertyAreaIconContainer.visibility = View.VISIBLE
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

        //        Focus from Land Type ---> Property Type

        binding.landTypeDropdown.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                // Move focus to the next view (e.g., another dropdown)
                binding.propertyTypeDropdown.requestFocus()
                true // Return true to consume the action
            } else {
                false
            }
        }

        binding.propertyTypeDropdown.setOnEditorActionListener { _, actionId, _ ->
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

        binding.propertyTypeDropdown.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.propertyTypeDropdown.setRawInputType(InputType.TYPE_CLASS_TEXT)


        val districtAddIcon = binding.addDestrictIcon

// Sample data for the dropdown
        districtList = mutableListOf("Lahore", "Islamabad", "Karachi", "Peshawar", "Quetta")
        adapter = ArrayAdapter(this, custom_dropdown_item, districtList)

// Set adapter to AutoCompleteTextView
        districtDropDown.setAdapter(adapter)


        districtDropDown.threshold = 1 // Start searching after typing one character


        // Apply filter and count the visible items
        val itemCount = adapter.count // Gets the current filtered item count
        if (itemCount <= 3) {
            districtDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            // Set a fixed height for more than 3 items
            districtDropDown.dropDownHeight =
                (districtDropDown.context.resources.displayMetrics.density * 150).toInt()
        }


// Set up a listener to track the filtered results and dynamically set the dropdown height
        districtDropDown.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Apply filter and count the visible items
                adapter.filter.filter(s) {
                    val visibleItemCount = adapter.count // Gets the current filtered item count
                    if (visibleItemCount <= 3) {
                        districtDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
                    } else {
                        // Set a fixed height for more than 3 items
                        districtDropDown.dropDownHeight =
                            (districtDropDown.context.resources.displayMetrics.density * 150).toInt()
                    }
                }
            }
        })


//        Logic for Adding new district from the user and saving that in the adapter

        districtAddIcon.setOnClickListener {

//            if (districtDropDown.text.toString().trim().isNotEmpty()) {
                // Inflate the custom add alert view for the dialog
                val dialogView = layoutInflater.inflate(R.layout.custom_add_alert, null)
                val addMessage = dialogView.findViewById<TextView>(R.id.addMessage)
                val addinputEditText = dialogView.findViewById<EditText>(R.id.AddinputEditText)

                // Set the message text
                addMessage.text = "Add a New District"

                // Create a confirmation dialog to add a new district
            val addDistrictDialog = MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val newDistrictName = addinputEditText.text.toString().trim()

                    when {
                        newDistrictName.isEmpty() -> {
                            Toast.makeText(this, "District name cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        districtList.contains(newDistrictName) -> {
                            Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // Add the new district to the list
                            districtList.add(newDistrictName)

                            // Refresh adapter by recreating it
                            adapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                            districtDropDown.setAdapter(adapter)

                            districtDropDown.setText("") // Clear dropdown text.

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

            addDistrictDialog.show()



//            }
//            else {
//                // If districtDropDown has text, show a Toast message
//                val inputMethodManager =
//                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//                Toast.makeText(this, "Remove District first", Toast.LENGTH_SHORT).show()
//            }
        }



//        --------------- Add district Logic Above-------------


// ============= Edit District Logic Below ================


        val districtEditIcon = binding.editDistrictIcon

        districtEditIcon.setOnClickListener {
            val selectedDistrict = districtDropDown.text.toString().trim()

            if (selectedDistrict.isNotEmpty() && districtList.contains(selectedDistrict)) {
                // Inflate the custom edit alert view for the dialog
                val dialogView = layoutInflater.inflate(R.layout.custom_edit_alert, null)
                val editMessage = dialogView.findViewById<TextView>(R.id.editMessage)
                val inputEditText = dialogView.findViewById<EditText>(R.id.inputEditText)
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
                                districtList[index] = newDistrictName

                                // Refresh adapter by recreating it
                                adapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                                districtDropDown.setAdapter(adapter)

                                districtDropDown.setText("") // Clear dropdown text.

                                Log.d("DistrictList", "Updated district list: $districtList")
                                Toast.makeText(this, "District updated successfully", Toast.LENGTH_SHORT).show()
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

                    inputEditText.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(inputEditText, InputMethodManager.SHOW_IMPLICIT)
                }
                editDistrictDialog.show()
            } else {
                Toast.makeText(this, "Please select a district to edit", Toast.LENGTH_SHORT).show()
            }
        }




//        ============= Delete  district Logic Below ================


        binding.deleteDistrictIcon.setOnClickListener {

            val selectedDistrict = districtDropDown.text.toString().trim()
            if (selectedDistrict.isNotEmpty()) {
                // Inflate the custom alert view for the dialog
                val dialogView = layoutInflater.inflate(R.layout.custom_delete_alert, null)
                val alertMessage = dialogView.findViewById<TextView>(R.id.alertMessage)
                alertMessage.text = "Are you sure you want to delete this district?"

                // Create a new title view for the dialog
                val titleView = layoutInflater.inflate(R.layout.dialog_title_centered, null)
                val centeredTitle = titleView.findViewById<TextView>(R.id.centeredTitle)
                centeredTitle.text = "District   ''$selectedDistrict''"

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
                    if (districtList.contains(selectedDistrict)) {
                        districtList.remove(selectedDistrict)

                        // Refresh adapter by recreating it
                        adapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                        districtDropDown.setAdapter(adapter)

                        // Clear the text in districtDropDown
                        districtDropDown.setText("")

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




        }


    private fun saveDistrictList() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(districtList)
        editor.putString("districtList", json)
        editor.apply()
    }
    private fun loadDistrictList() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val json = sharedPreferences.getString("districtList", null)
        if (json != null) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            districtList = Gson().fromJson(json, type)
        } else {
            districtList = ArrayList() // Default empty list
        }
    }

    private fun resetDistrictList() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val emptyListJson = Gson().toJson(ArrayList<String>())  // Empty list
        editor.putString("districtList", emptyListJson)  // Save empty list
        editor.apply()

        districtList.clear()  // Clears the current districtList in memory
    }


}