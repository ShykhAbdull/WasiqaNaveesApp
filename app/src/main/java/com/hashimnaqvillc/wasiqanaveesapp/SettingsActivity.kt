package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

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



//        Logic for District DropDown

        // Get references to the TextInputLayout and AutoCompleteTextView
        val districtDropDown = binding.districtDropdown


        districtDropDown.setOnClickListener {
            districtDropDown.showDropDown()
            districtDropDown.requestFocus()
            districtDropDown.requestFocusFromTouch()
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
        val districtList = mutableListOf("Lahore", "Islamabad", "Karachi", "Peshawar", "Quetta")
        val adapter = ArrayAdapter(this, R.layout.custom_dropdown_item, districtList)

// Set adapter to AutoCompleteTextView
        districtDropDown.setAdapter(adapter)

        districtDropDown.threshold = 1 // Start searching after typing one character


        // Apply filter and count the visible items
        val itemCount = adapter.count // Gets the current filtered item count
        if (itemCount <= 3) {
            districtDropDown.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            // Set a fixed height for more than 3 items
            districtDropDown.dropDownHeight = (districtDropDown.context.resources.displayMetrics.density * 150).toInt()
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
                        districtDropDown.dropDownHeight = (districtDropDown.context.resources.displayMetrics.density * 150).toInt()
                    }
                }
            }
        })








//        Logic for Adding new district from the user and saving that in the adapter

        districtAddIcon.setOnClickListener {

            if(districtDropDown.text.toString().trim().isEmpty()){

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
                        districtList.add(newDistrict)

                        districtDropDown.setAdapter(adapter)
                        adapter.notifyDataSetChanged()

                        // Ensure dropdown shows updated data
                        districtDropDown.requestFocus()
                        districtDropDown.showDropDown()

                        Toast.makeText(this, "District added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "District name cannot be empty", Toast.LENGTH_SHORT).show()
                }
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
                }else {
                // If districtDropDown has text, show a Toast message
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                Toast.makeText(this, "Remove District first", Toast.LENGTH_SHORT).show()
            }
        }


//        --------------- Add district Logic Above-------------


//        ================ Edit  district Logic Below ===============

        val districtEditIcon = binding.editDistrictIcon

        districtEditIcon.setOnClickListener {
            // Create a dialog to edit an existing district
            val editDistrictDialog = AlertDialog.Builder(this)
            editDistrictDialog.setTitle("Edit District")

            // Create a dropdown to let the user select the district to edit
            val districtDropdown = AutoCompleteTextView(this).apply {
                setAdapter(ArrayAdapter(this@SettingsActivity, R.layout.custom_dropdown_item, districtList))
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
                addView(districtDropdown)
                addView(input)
            }

            editDistrictDialog.setView(layout)

            editDistrictDialog.setPositiveButton("Save") { _, _ ->
                val selectedDistrict = districtDropdown.text.toString().trim()
                val newDistrictName = input.text.toString().trim()

                if (selectedDistrict.isEmpty()) {
                    Toast.makeText(this, "Please select a district to edit", Toast.LENGTH_SHORT).show()
                } else if (newDistrictName.isEmpty()) {
                    Toast.makeText(this, "New district name cannot be empty", Toast.LENGTH_SHORT).show()
                } else if (!districtList.contains(selectedDistrict)) {
                    Toast.makeText(this, "Selected district does not exist", Toast.LENGTH_SHORT).show()
                } else if (districtList.contains(newDistrictName)) {
                    Toast.makeText(this, "New district name already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Update the selected district with the new name
                    val index = districtList.indexOf(selectedDistrict)
                    districtList[index] = newDistrictName
                    districtDropdown.setText(newDistrictName)
                    adapter.clear()
                    adapter.addAll(districtList)
                    adapter.notifyDataSetChanged()
                    districtDropDown.requestFocus() // Ensure dropdown gets focus
                    districtDropDown.showDropDown() // Show updated dropdown
                    Toast.makeText(this, "District updated successfully", Toast.LENGTH_SHORT).show()
                }
            }

            editDistrictDialog.setNegativeButton("Cancel") { _, _ -> }

            val dialog = editDistrictDialog.create()

            // Show the dialog and focus the dropdown initially
            dialog.setOnShowListener {
                districtDropdown.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(districtDropdown, InputMethodManager.SHOW_IMPLICIT)
            }

            dialog.show()
        }

//        ============= Delete  district Logic Below ================

        val districtDeleteIcon = binding.deleteDistrictIcon

        districtDeleteIcon.setOnClickListener {
            // Create a dialog to delete an existing district
            val deleteDistrictDialog = AlertDialog.Builder(this)
            deleteDistrictDialog.setTitle("Delete District")

            // Create a dropdown to let the user select the district to delete
            val districtDropdown = AutoCompleteTextView(this).apply {
                setAdapter(ArrayAdapter(this@SettingsActivity, R.layout.custom_dropdown_item, districtList))
                threshold = 1
                hint = "Select district to delete"
            }

            // Use a layout to hold the dropdown
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 40, 50, 10) // Adjust padding for better UI
                addView(districtDropdown)
            }

            deleteDistrictDialog.setView(layout)

            deleteDistrictDialog.setPositiveButton("Delete") { _, _ ->
                val selectedDistrict = districtDropdown.text.toString().trim()

                if (selectedDistrict.isEmpty()) {
                    Toast.makeText(this, "Please select a district to delete", Toast.LENGTH_SHORT).show()
                } else if (!districtList.contains(selectedDistrict)) {
                    Toast.makeText(this, "Selected district does not exist", Toast.LENGTH_SHORT).show()
                } else {
                    // Remove the selected district from the list
                    districtList.remove(selectedDistrict)
                    adapter.clear()
                    adapter.addAll(districtList)
                    districtDropdown.setText("") // Clear the dropdown
                    adapter.notifyDataSetChanged()
                    districtDropDown.requestFocus() // Ensure dropdown gets focus

                    Toast.makeText(this, "District deleted successfully", Toast.LENGTH_SHORT).show()
                }
            }

            deleteDistrictDialog.setNegativeButton("Cancel") { _, _ -> }

            val dialog = deleteDistrictDialog.create()

            // Show the dialog and focus the dropdown initially
            dialog.setOnShowListener {
                districtDropdown.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(districtDropdown, InputMethodManager.SHOW_IMPLICIT)
            }

            dialog.show()
        }

//        ============= Done with the District Add,Edit, Delete Logic ===============










    }
}