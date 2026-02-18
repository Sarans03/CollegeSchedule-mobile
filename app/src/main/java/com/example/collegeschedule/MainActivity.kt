package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.ui.schedule.ScheduleScreen
import com.example.collegeschedule.ui.theme.CollegeScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitInstance.initialize(this)

        enableEdgeToEdge()
        setContent {
            CollegeScheduleTheme {
                Surface {
                    ScheduleScreen()
                }
            }
        }
    }
}