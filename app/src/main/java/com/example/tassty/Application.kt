package com.example.tassty

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // This is the CRUCIAL initialization call
        AndroidThreeTen.init(this)
    }
}