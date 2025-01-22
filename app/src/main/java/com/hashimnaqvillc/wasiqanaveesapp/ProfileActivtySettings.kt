package com.hashimnaqvillc.wasiqanaveesapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityProfileSettingsBinding

class ProfileActivtySettings : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingsBinding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val editTexts = mutableListOf(
            binding.stampDutyEditTextInner,
            binding.tmaCorpEditTextInner,
            binding.regsFeeEditTextInner,

            binding.fbr236KFilerEditTextInner,
            binding.fbr236KNonfilerEditTextInner,
            binding.fbr236KLatefilerEditTextInner,

            binding.fbr236CFilerEditTextInner,
            binding.fbr236CNonfilerEditTextInner,
            binding.fbr236ClatefilerEditTextInner,

            binding.officeNameEditTextInner,
            binding.officePhoneEditTextInner
        )

        // Ensure all EditTexts are disabled initially
        editTexts.forEach { editText ->
            editText.apply {
                isEnabled = false
                isFocusable = false
                isFocusableInTouchMode = false
                isClickable = false
            }
        }






        val settingIcon = findViewById<ImageButton>(R.id.nav_settings_icon)
        settingIcon.visibility = View.GONE
        val dateMonth = findViewById<TextView>(R.id.date_month_day)
        dateMonth.visibility = View.VISIBLE
        val dateYear = findViewById<TextView>(R.id.date_year)
        dateYear.visibility = View.VISIBLE

        val profileEditBtn = findViewById<Button>(R.id.profile_edit_btn)
        profileEditBtn.visibility = View.GONE

        val wasiqaLogo = findViewById<ImageView>(R.id.nav_wasiqa_logo)
        wasiqaLogo.visibility = View.GONE
        val profileText = findViewById<TextView>(R.id.profile_text)
        profileText.visibility = View.VISIBLE





        val backBtn = findViewById<ImageButton>(R.id.nav_back)
        backBtn.setOnClickListener {
            finish()
        }

        setUpEditButton()

    }

    override fun onResume() {

        super.onResume()
        setUpEditButton()
    }

    private fun setUpEditButton() {
        val editTexts = mutableListOf(
            binding.stampDutyEditTextInner,
            binding.tmaCorpEditTextInner,
            binding.regsFeeEditTextInner,

            binding.fbr236KFilerEditTextInner,
            binding.fbr236KNonfilerEditTextInner,
            binding.fbr236KLatefilerEditTextInner,

            binding.fbr236CFilerEditTextInner,
            binding.fbr236CNonfilerEditTextInner,
            binding.fbr236ClatefilerEditTextInner,

            binding.officeNameEditTextInner,
            binding.officePhoneEditTextInner
        )

        var isEditing = false

        binding.editBtn.setOnClickListener {
            editTexts.forEach { editText ->
                // Set all properties at once for the new state
                editText.apply {
                    isEnabled = !isEditing
                    isFocusable = !isEditing
                    isFocusableInTouchMode = !isEditing
                    isClickable = !isEditing
                }
            }

            binding.editBtn.text = if (!isEditing) "Done" else "Edit"
            isEditing = !isEditing
        }


        binding.saveBtn.setOnClickListener {
            editTexts.forEach { editText ->
                val key = editText.id.toString()
                val value = editText.text.toString()
                PreferencesManager.saveData(key, value)
            }
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
        }

// Load data back into the EditTexts
        editTexts.forEach { editText ->
            val key = editText.id.toString()
            val savedValue = PreferencesManager.getData(key)
            editText.setText(savedValue)
        }


    }




}