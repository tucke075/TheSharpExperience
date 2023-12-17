package com.example.thesharpexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar //needed for toolbar, HAS TO BE ANDROID X
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.thesharpexperience.bot_nav_fragments.dayNaFragment
import com.example.thesharpexperience.bot_nav_fragments.dayRnFragment
import com.example.thesharpexperience.bot_nav_fragments.nightNaFragment
import com.example.thesharpexperience.bot_nav_fragments.nightRnFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class FragDisplayActivity : AppCompatActivity() {
    //private lateinit var drawerLayout : DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frag_display)
        //creating fragment variables referencing fragments in fragment folder
        val dayRN = dayRnFragment()
        val dayNA = dayNaFragment()
        val nightRN = nightRnFragment()
        val nightNA = nightNaFragment()

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener{
            startActivity(Intent(this, RequestAddActivity::class.java))
        }
        /*findViewById<FloatingActionButton>(R.id.bSignout).setOnClickListener{
            logout()
        }

         */

    }

    //always needed for navigation drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }
    private fun logout()
    {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }
}