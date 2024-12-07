package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        val districtDropdown = binding.districtDropdown

        districtDropdown.isCursorVisible = false // Hide cursor by default


        val addIcon = findViewById<ImageView>(R.id.add_destrict_icon)
        val editIcon = findViewById<ImageView>(R.id.edit_district_icon)
        val deleteIcon = findViewById<ImageView>(R.id.delete_district_icon)





// Sample data for the dropdown
        val districtList = mutableListOf("Lahore", "Islamabad", "Karachi", "Peshawar")
        val adapter = ArrayAdapter(this, R.layout.custom_dropdown_item, districtList)

// Set adapter to AutoCompleteTextView
        districtDropdown.setAdapter(adapter)



// Make the dropdown searchable
        districtDropdown.threshold = 1 // Start searching after typing one character



        addIcon.setOnClickListener {
            // Enable input and show cursor
            districtDropdown.inputType = InputType.TYPE_CLASS_TEXT
            districtDropdown.isCursorVisible = true

            // Show the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(districtDropdown, InputMethodManager.SHOW_IMPLICIT)

            // Optionally clear any previous text
            districtDropdown.text.clear()
        }


// Clear text when the cross icon is clicked
        districtDropdown.setOnTouchListener { v, event ->
            val drawableEnd = 2 // Position of drawableEnd
            binding.districtDropdown.showDropDown()
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= ((districtDropdown.right - districtDropdown.compoundDrawables[drawableEnd]?.bounds?.width()!!)
                        ?: 0)
                ) {
                    districtDropdown.text.clear() // Clear text
                    return@setOnTouchListener true
                }
            }
            false
        }






        addIcon.setOnClickListener {
            val newDistrictDialog = AlertDialog.Builder(this)
            newDistrictDialog.setTitle("Add New District")
            val input = EditText(this)
            input.hint = "Enter district name"
            input.inputType = InputType.TYPE_CLASS_TEXT
            newDistrictDialog.setView(input)



            newDistrictDialog.setPositiveButton("Add") { _, _ ->
                val newDistrict = input.text.toString().trim()
                if (newDistrict.isNotEmpty() && !districtList.contains(newDistrict)) {
                    districtList.add(newDistrict)
                    Toast.makeText(this, "District added", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                    // Disable input and hide cursor after adding
                    districtDropdown.isCursorVisible = false

                }
                else if (newDistrict.isEmpty()) {
                    Toast.makeText(this, "District name cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else if(districtList.contains(newDistrict)){
                    Toast.makeText(this, "District already exists", Toast.LENGTH_SHORT).show()
                }

            }

            newDistrictDialog.setNegativeButton("Cancel") { _, _ ->
                // Disable input and hide cursor if the user cancels
                districtDropdown.isCursorVisible = false
            }

            newDistrictDialog.show()
        }





    }
}