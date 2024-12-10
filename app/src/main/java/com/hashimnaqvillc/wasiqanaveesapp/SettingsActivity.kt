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
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hashimnaqvillc.wasiqanaveesapp.R.layout.custom_dropdown_item
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivitySettingsBinding

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

        binding.townDropdown.setOnClickListener {
            binding.districtInputLayout.isEndIconVisible = false
        }

        // Optional: Disable default focus stealing by the clear_text icon
        districtDropDown.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                districtDropDown.showDropDown()
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
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Close the keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.propertyTypeDropdown.windowToken, 0)

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

            if (districtDropDown.text.toString().trim().isEmpty()) {

                val newDistrictDialog = AlertDialog.Builder(this)
                newDistrictDialog.setTitle("Add New District")


//          Create an EditText for the new district name

                val input = EditText(this).apply {
                    hint = "Enter district name"
                    inputType = InputType.TYPE_CLASS_TEXT
                    isFocusableInTouchMode = true
                }
                newDistrictDialog.setView(input)

                newDistrictDialog.setPositiveButton("Add") { _, _ ->
                    val newDistrict = input.text.toString().trim()

                    if (newDistrict.isNotEmpty()) {
                        val normalizedDistrict = newDistrict.lowercase()
                        if (!districtList.map { it.lowercase() }.contains(normalizedDistrict)) {
                            Log.d("DistrictList", "Before adding: $districtList")
                            districtList.add(newDistrict)
                            Log.d("DistrictList", "After adding: $districtList")

                            // Refresh adapter by recreating it
                            adapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                            districtDropDown.setAdapter(adapter)


                            districtDropDown.setText("") // Clear dropdown text.
                            districtDropDown.post {
                                districtDropDown.dismissDropDown()
                                districtDropDown.showDropDown()
                            }

                            Toast.makeText(this, "District added", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "District name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
//                    adapter.notifyDataSetChanged() // Notify adapter of changes.

                }


                newDistrictDialog.setNegativeButton("Cancel") { _, _ -> }

                val dialog = newDistrictDialog.create()

                // Show the dialog and then request focus for the EditText
                dialog.setOnShowListener {
                    input.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
                }

                dialog.show()
            } else {
                // If districtDropDown has text, show a Toast message
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                Toast.makeText(this, "Remove District first", Toast.LENGTH_SHORT).show()
            }
        }


//        --------------- Add district Logic Above-------------


//        ================ Edit  district Logic Below ===============

        val districtEditIcon = binding.editDistrictIcon

        districtEditIcon.setOnClickListener {

            if (districtDropDown.text.toString().trim().isEmpty()) {

                // Create a dialog to edit an existing district
                val editDistrictDialog = AlertDialog.Builder(this)
                editDistrictDialog.setTitle("Edit District")

                // Create a dropdown to let the user select the district to edit
                val districtDropdownforEdit = AutoCompleteTextView(this).apply {
                    setAdapter(
                        ArrayAdapter(
                            this@SettingsActivity,
                            custom_dropdown_item,
                            districtList
                        )
                    )
                    threshold = 1
                    hint = "Select district to edit"
                }

                // Create an EditText for editing the district name
                val input = EditText(this).apply {
                    hint = "Enter new district name"
                    inputType = InputType.TYPE_CLASS_TEXT
                    isFocusableInTouchMode = true
                }

                // Use a vertical layout to hold both dropdown and input
                val layout = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(50, 40, 50, 10) // Adjust padding for better UI
                    addView(districtDropdownforEdit)
                    addView(input)
                }

                editDistrictDialog.setView(layout)

                editDistrictDialog.setPositiveButton("Save") { _, _ ->
                    val selectedDistrict = districtDropdownforEdit.text.toString().trim()
                    val newDistrictName = input.text.toString().trim()

                    Log.d("selectedDistrict", "Selected edit: $selectedDistrict")
                    Log.d("selectedDistrict", "New edit: $newDistrictName")

                    if (selectedDistrict.isEmpty()) {
                        Toast.makeText(this, "Please select a district to edit", Toast.LENGTH_SHORT).show()
                    } else if (newDistrictName.isEmpty()) {
                        Toast.makeText(this, "New district name cannot be empty", Toast.LENGTH_SHORT).show()
                    } else if (!districtList.contains(selectedDistrict)) {
                        Toast.makeText(this, "Selected district does not exist", Toast.LENGTH_SHORT).show()
                    } else if (districtList.contains(newDistrictName)) {
                        Toast.makeText(this, "New district name already exists", Toast.LENGTH_SHORT).show()
                    } else {
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

                editDistrictDialog.setNegativeButton("Cancel") { _, _ -> }

                val dialog = editDistrictDialog.create()

                // Show the dialog and focus the dropdown initially
                dialog.setOnShowListener {
                    districtDropdownforEdit.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(districtDropdownforEdit, InputMethodManager.SHOW_IMPLICIT)
                }

                dialog.show()
            } else {
                // If districtDropDown has text, show a Toast message
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                Toast.makeText(this, "Remove District first", Toast.LENGTH_SHORT).show()
            }

//        ============= Delete  district Logic Below ================

            val districtDeleteIcon = binding.deleteDistrictIcon

            districtDeleteIcon.setOnClickListener {

                if (districtDropDown.text.toString().trim().isEmpty()) {

                    // Create a dialog to delete an existing district
                    val deleteDistrictDialog = AlertDialog.Builder(this)
                    deleteDistrictDialog.setTitle("Delete District")

                    // Create a dropdown to let the user select the district to delete
                    val districtDropdownforDelete = AutoCompleteTextView(this).apply {
                        setAdapter(
                            ArrayAdapter(
                                this@SettingsActivity,
                                custom_dropdown_item,
                                districtList
                            )
                        )
                        threshold = 1
                        hint = "Select district to delete"
                    }

                    // Use a layout to hold the dropdown
                    val layout = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(50, 40, 50, 10) // Adjust padding for better UI
                        addView(districtDropdownforDelete)
                    }

                    deleteDistrictDialog.setView(layout)

                    deleteDistrictDialog.setPositiveButton("Delete") { _, _ ->
                        val selectedDistrict = districtDropdownforDelete.text.toString().trim()

                        if (selectedDistrict.isEmpty()) {
                            Toast.makeText(this, "Please select a district to delete", Toast.LENGTH_SHORT).show()
                        } else if (!districtList.contains(selectedDistrict)) {
                            Toast.makeText(this, "Selected district does not exist", Toast.LENGTH_SHORT).show()
                        } else {
                            districtList.remove(selectedDistrict)

                            // Refresh adapter by recreating it
                            adapter = ArrayAdapter(this, custom_dropdown_item, districtList)
                            districtDropDown.setAdapter(adapter)

                            districtDropDown.setText("") // Clear dropdown text.


                            Log.d("DistrictList", "Deleted district list: $districtList")
                            Toast.makeText(this, "District deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                    }


                    deleteDistrictDialog.setNegativeButton("Cancel") { _, _ -> }

                    val dialog = deleteDistrictDialog.create()

                    // Show the dialog and focus the dropdown initially
                    dialog.setOnShowListener {
                        districtDropdownforDelete.requestFocus()
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(districtDropdownforDelete, InputMethodManager.SHOW_IMPLICIT)
                    }

                    dialog.show()
                } else {
                    // If districtDropDown has text, show a Toast message
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    Toast.makeText(this, "Remove District first", Toast.LENGTH_SHORT).show()
                }
//        ============= Done with the District Add,Edit, Delete Logic ===============


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