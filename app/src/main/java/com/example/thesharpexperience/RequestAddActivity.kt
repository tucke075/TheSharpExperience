package com.example.thesharpexperience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView

class RequestAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_add)

        //day onclick listener for the calendar
        val calendarView : CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, day ->


            val date = "$day/${month + 1}/$year"
            //Toast.makeText(this, "Selected date: $date", Toast.LENGTH_SHORT).show()

        }
    }
}