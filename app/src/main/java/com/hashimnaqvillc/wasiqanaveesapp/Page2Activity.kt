package com.hashimnaqvillc.wasiqanaveesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage2Binding

class Page2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPage2Binding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPage2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingButton = findViewById<ImageButton>(R.id.nav_settings_icon)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val backBtn = findViewById<ImageButton>(R.id.nav_back)
        backBtn.setOnClickListener {
            finish()
        }

        val dateMonth = findViewById<TextView>(R.id.date_month_day)
        dateMonth.visibility = View.GONE
        val dateYear = findViewById<TextView>(R.id.date_year)
        dateYear.visibility = View.GONE


        binding.nextButtonPg2.setOnClickListener {
            val intent = Intent(this, Page3Activity::class.java)
            startActivity(intent)
        }

        // Retrieve the property type from SharedPreferences
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val propertyType = sharedPref.getString("PROPERTY_TYPE", "Plot") // Default is "Plot"
        val landType = sharedPref.getString("LAND_TYPE", "Residential") // Default is "Residential"
        val town = sharedPref.getString("TOWN", "Bahria Town Lhr") // Default is "Bahria Town Lhr"

        binding.propertyAreaDisplayPg2.text = town
        binding.landTypeDisplayPg2.text = landType


        if(propertyType == "Building"){
            binding.coveredAreaRowPg2Separator.visibility = View.VISIBLE
            binding.coveredAreaRowPg2.visibility = View.VISIBLE
            binding.buildingValuePg2Separator.visibility = View.VISIBLE
            binding.buildingValuePg2.visibility = View.VISIBLE
        } else{
            binding.coveredAreaRowPg2Separator.visibility = View.GONE
            binding.coveredAreaRowPg2.visibility = View.GONE
            binding.buildingValuePg2Separator.visibility = View.GONE
            binding.buildingValuePg2.visibility = View.GONE
        }



    }
}