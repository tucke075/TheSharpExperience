package com.example.thesharpexperience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.thesharpexperience.fragments.dayNaFragment
import com.example.thesharpexperience.fragments.dayRnFragment
import com.example.thesharpexperience.fragments.nightNaFragment
import com.example.thesharpexperience.fragments.nightRnFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating fragment variables referencing fragments in fragment folder
        val dayRN = dayRnFragment()
        val dayNA = dayNaFragment()
        val nightRN = nightRnFragment()
        val nightNA = nightNaFragment()

        changeFragment(dayRN)
        findViewById<BottomNavigationView>(R.id.bot_nav).setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.ic_day_rn -> {
                    changeFragment(dayRN)
                    true
                }
                R.id.ic_day_na -> {
                    changeFragment(dayNA)
                    true
                }
                R.id.ic_night_rn -> {
                    changeFragment(nightRN)
                    true
                }
                R.id.ic_night_na -> {
                    changeFragment(nightNA)
                    true
                }
             else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }
}