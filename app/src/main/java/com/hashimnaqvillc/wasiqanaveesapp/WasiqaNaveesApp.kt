package com.hashimnaqvillc.wasiqanaveesapp

import android.app.Application

class WasiqaNaveesApp : Application(){

    override fun onCreate() {
        super.onCreate()

        // Initialize SharedPreferencesManager
        PreferencesManager.initialize(this)





    }
}