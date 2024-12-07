package com.hashimnaqvillc.wasiqanaveesapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
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

        val settingIcon = findViewById<ImageButton>(R.id.nav_settings_icon)
        settingIcon.visibility = View.GONE
        val dateMonth = findViewById<TextView>(com.hashimnaqvillc.wasiqanaveesapp.R.id.date_month_day)
        dateMonth.visibility = View.GONE
        val dateYear = findViewById<TextView>(com.hashimnaqvillc.wasiqanaveesapp.R.id.date_year)
        dateYear.visibility = View.GONE

        val profileEditBtn = findViewById<LinearLayout>(R.id.profile_edit_btn)
        profileEditBtn.visibility = View.GONE





        val backBtn = findViewById<ImageButton>(R.id.nav_back)
        backBtn.setOnClickListener {
            finish()
        }







    }




}