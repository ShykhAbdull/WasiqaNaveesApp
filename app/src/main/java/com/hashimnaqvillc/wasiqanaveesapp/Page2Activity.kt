package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hashimnaqvillc.wasiqanaveesapp.databinding.ActivityPage2Binding

class Page2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPage2Binding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPage2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        // Retrieve the values from the Intent
        val selectedDistrict = intent.getStringExtra("selectedDistrict")
        val selectedTown = intent.getStringExtra("selectedTown")
        val selectedPropertyArea = intent.getStringExtra("selectedPropertyArea")
        val selectedLandType = intent.getStringExtra("selectedLandType")
        val selectedPropertyType = intent.getStringExtra("selectedPropertyType")
        val kanalValue = intent.getDoubleExtra("kanalValue", 0.0)
        val marlaValue = intent.getDoubleExtra("marlaValue", 0.0)
        val sqftValue = intent.getDoubleExtra("sqftValue", 0.0)
        val coveredArea = intent.getDoubleExtra("coveredArea", 0.0)

        val plotValueDC = intent.getDoubleExtra("plotValueDC", 0.0)



//        Displaying selected values on Pg2 Views
        binding.districtDisplayPg2.text = selectedDistrict
        binding.townDisplayPg2.text = selectedTown
        binding.propertyAreaDisplayPg2.text = selectedPropertyArea
        binding.landTypeDisplayPg2.text = selectedLandType
        binding.coveredAreaDisplayPg2.text = coveredArea.toString()

// Create a string to display land area
        val landAreaStringBuilder = StringBuilder()

        if (kanalValue != 0.0) {
            landAreaStringBuilder.append("${kanalValue.toInt()} Kanal, ")
        }
        if (marlaValue != 0.0) {
            landAreaStringBuilder.append("${marlaValue.toInt()} Marla, ")
        }
        if (sqftValue != 0.0) {
            landAreaStringBuilder.append("${sqftValue.toInt()} Sqft")
        }

// Remove the trailing comma and space, if any
        val landAreaDisplayText = landAreaStringBuilder.toString().trimEnd(',', ' ')

// Set the text if there's any value, otherwise show a placeholder
        binding.landAreaDisplayPg2.text = if (landAreaDisplayText.isNotEmpty()) {
            landAreaDisplayText
        } else {
            "No land area specified"
        }




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

        binding.plotValueDCDisplay.text = plotValueDC.toString()


        binding.nextButtonPg2.setOnClickListener {
            val intent = Intent(this, Page3Activity::class.java)
            startActivity(intent)
        }

        binding.propertyAreaDisplayPg2.text = selectedTown
        binding.landTypeDisplayPg2.text = selectedLandType


        if(selectedPropertyType == "Building"){
//            binding.coveredAreaRowPg2Separator.visibility = View.VISIBLE
            binding.coveredAreaRowPg2.visibility = View.VISIBLE
//            binding.buildingValuePg2Separator.visibility = View.VISIBLE
            binding.buildingValuePg2.visibility = View.VISIBLE
        } else{
//            binding.coveredAreaRowPg2Separator.visibility = View.GONE
            binding.coveredAreaRowPg2.visibility = View.GONE
//            binding.buildingValuePg2Separator.visibility = View.GONE
            binding.buildingValuePg2.visibility = View.GONE
        }



    }
}