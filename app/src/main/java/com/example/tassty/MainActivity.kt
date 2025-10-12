package com.example.tassty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tassty.screen.detailmenu.DetailMenuScreen
import com.example.tassty.screen.detailrestaurant.DetailRestaurantScreen
import com.example.tassty.ui.theme.TasstyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasstyTheme {
                DetailRestaurantScreen()
            }
        }
    }
}
