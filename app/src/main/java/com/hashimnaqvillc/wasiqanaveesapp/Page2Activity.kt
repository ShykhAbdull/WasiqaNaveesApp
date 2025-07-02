package com.hashimnaqvillc.wasiqanaveesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val selectedPropertyArea = intent.getStringExtra("selectedPropertyArea") ?: ""
        val selectedLandType = intent.getStringExtra("selectedLandType")
        val selectedPropertyType = intent.getStringExtra("selectedPropertyType")
        val khasraNumber = intent.getStringExtra("khasraNumber")

        val kanalValue = intent.getIntExtra("kanalValue", 0)
        val marlaValue = intent.getFloatExtra("marlaValue", 0f)
        val sqftValue = intent.getIntExtra("sqftValue", 0)
        val coveredArea = intent.getIntExtra("coveredAreaValue", 0)

        val plotValueDC = intent.getIntExtra("plotValueDC", 0)
        val marlaValueDC = intent.getIntExtra("marlaDc", 0)

        val plotValueFbr = intent.getIntExtra("plotValueFbr", 0)
        val marlaValueFbr = intent.getIntExtra("marlaFbr", 0)

        val coveredAreaDc = intent.getIntExtra("coveredAreaDc", 0)
        val buildingValueDC = intent.getIntExtra("buildingValueDC", 0)

        val coveredAreaFbr = intent.getIntExtra("coveredAreaFbr", 0)
        val buildingValueFbr = intent.getIntExtra("buildingValueFbr", 0)

        Log.d("IntentData", "Intent Data: $selectedDistrict, $selectedTown, $selectedPropertyArea,  $selectedPropertyType")


        //        Displaying selected values on Pg2 Views
        binding.districtDisplayPg2.text = selectedDistrict
        binding.propertyAreaDisplayPg2.text = selectedPropertyArea
        binding.townDisplayPg2.text = selectedTown
        binding.landTypeDisplayPg2.text = selectedLandType
        binding.coveredAreaDisplayPg2.text = coveredArea.toString()
        binding.khasraDisplayPg2.text = khasraNumber.toString()


        if (selectedPropertyType == "Plot") {
            binding.coveredAreaTextPg2.visibility = View.GONE
            binding.coveredAreaDisplayPg2.visibility = View.GONE
        }else{
            binding.coveredAreaTextPg2.visibility = View.VISIBLE
            binding.coveredAreaDisplayPg2.visibility = View.VISIBLE
        }






// Create a string to display land area
        val landAreaStringBuilder = StringBuilder()

        if (kanalValue != 0) {
            landAreaStringBuilder.append("$kanalValue Kanal, ")
        }
        if (marlaValue != 0.0f) {
            landAreaStringBuilder.append("$marlaValue Marla, ")
        }
        if (sqftValue != 0) {
            landAreaStringBuilder.append("$sqftValue Sqft")
        }

// Remove the trailing comma and space, if any
        val landAreaDisplayText = landAreaStringBuilder.toString().trimEnd(',', ' ')

// Set the text if there's any value, otherwise show a placeholder
        binding.landAreaDisplayPg2.text = landAreaDisplayText.ifEmpty {
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
//        val dateYear = findViewById<TextView>(R.id.date_year)
//        dateYear.visibility = View.GONE

        binding.perMarlaDCDisplay.text = marlaValueDC.toString()
        binding.plotValueDCDisplay.text = plotValueDC.toString()

        binding.coveredAreaDCDisplay.text = coveredAreaDc.toString()
        binding.buildingValueDCDisplay.text = buildingValueDC.toString()

        binding.perMarlaFBRDisplay.text = marlaValueFbr.toString()
        binding.plotValueFBRDisplay.text = plotValueFbr.toString()

        binding.coveredAreaFBRDisplay.text = coveredAreaFbr.toString()
        binding.buildingValueFBRDisplay.text = buildingValueFbr.toString()







        val totalDC: Int
        val totalFBR: Int


        if (selectedPropertyType == "Building") {
            // Show relevant rows
            binding.coveredAreaRowPg2.visibility = View.VISIBLE
            binding.buildingValuePg2.visibility = View.VISIBLE

            // Calculate total values
            totalDC =  buildingValueDC  + plotValueDC
            totalFBR =  buildingValueFbr + plotValueFbr

            // Set the calculated values to the TextViews
            binding.totalDCDisplay.text = "$totalDC"
            binding.totalFBRDisplay.text = "$totalFBR"


        }
        else{
//            binding.coveredAreaRowPg2Separator.visibility = View.GONE
            binding.coveredAreaRowPg2.visibility = View.GONE
//            binding.buildingValuePg2Separator.visibility = View.GONE
            binding.buildingValuePg2.visibility = View.GONE

            // Calculate total values
           totalDC = marlaValueDC + plotValueDC
            totalFBR = marlaValueFbr + plotValueFbr

            binding.totalDCDisplay.text = "$totalDC"
            binding.totalFBRDisplay.text = "$totalFBR"
    }


        binding.nextButtonPg2.setOnClickListener {
            val intent = Intent(this, Page3Activity::class.java).apply {

                putExtra("selectedDistrict", selectedDistrict)
                putExtra("selectedPropertyArea", selectedPropertyArea)
                putExtra("selectedPropertyType", selectedPropertyType)

                putExtra("kanalValue", kanalValue)
                putExtra("marlaValue", marlaValue)
                putExtra("sqftValue", sqftValue)
                putExtra("coveredArea", coveredArea)
                putExtra("totalDC", totalDC)
                putExtra("totalFBR", totalFBR)
            }
            startActivity(intent)
        }



    }
}