package com.example.tassty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tassty.screen.setupaccount.SetLocationScreen
import com.example.tassty.ui.theme.TasstyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasstyTheme {
                SetLocationScreen(
                    onLocationSelected = {_,_,_->}
                )
            }
        }
    }
}
