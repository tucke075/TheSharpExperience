package com.example.thesharpexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar //needed for toolbar, HAS TO BE ANDROID X
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.thesharpexperience.Drawer_Activities.SettingsActivity
import com.example.thesharpexperience.bot_nav_fragments.dayNaFragment
import com.example.thesharpexperience.bot_nav_fragments.dayRnFragment
import com.example.thesharpexperience.bot_nav_fragments.nightNaFragment
import com.example.thesharpexperience.bot_nav_fragments.nightRnFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragDisplayActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frag_display)
        //creating fragment variables referencing fragments in fragment folder
        val dayRN = dayRnFragment()
        val dayNA = dayNaFragment()
        val nightRN = nightRnFragment()
        val nightNA = nightNaFragment()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpDrawer()//personalizes the navigation drawer for the current user

        changeFragment(dayRN)
        findViewById<BottomNavigationView>(R.id.bot_nav).setOnItemSelectedListener { item ->
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

        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, RequestAddActivity::class.java))
        }

    }

    //always needed for navigation drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUpDrawer() {
        val navigationView: NavigationView = findViewById(R.id.nav_view) // Make sure this is the correct ID for your NavigationView
        val hireDateMenuItem = navigationView.menu.findItem(R.id.hireDate)
        checkLead{isLead ->
            if(!isLead) { //case where we want to hide hireDate menu item for the general user
                hireDateMenuItem.isVisible = false //makes it invisible if youre not a lead
                navigationView.setNavigationItemSelectedListener { menuItem ->
                    when (menuItem.itemId) {
                        //handling all item calls
                        R.id.title -> {
                            true // Indicate that the click event has been handled
                        }
                        R.id.viewProfile -> {

                        }
                        R.id.signout -> {
                            logout()
                        }
                    }
                    true // Close drawer when an item is clicked
                }
            }else{ //case where a person with access has logged in and has access to hireDate menu item
                navigationView.setNavigationItemSelectedListener { menuItem ->
                    when (menuItem.itemId) {
                        //handling all item calls
                        R.id.hireDate -> {

                                }
                        R.id.title -> {

                        }
                        R.id.viewProfile ->{

                        }
                        R.id.signout -> {
                            logout()
                            true
                        }
                    }
                    true // Indicate that the click event has been handled
                 }
            }
            true // Close drawer when an item is clicked
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun checkLead(callback: (Boolean) -> Unit) {
        var lead: Int = -1

        //checking firebase authentication associated with login
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) //user exists
        {
            //open cloud firestore and check if person is lead
            val db = FirebaseFirestore.getInstance()
            db.collection("Days")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (document in it.result!!) {
                            if (user.email == document.data["email"].toString()) {
                                lead = document.data["nurseType"]?.toString()?.toIntOrNull() ?: -1
                            }
                        }
                        /*
                        needs callback function since fetch from database could take some time
                        and make sure lead value represents the correct value
                         */
                        callback(lead == 2)
                    } else {
                        callback(false)
                    }
                }
        } else {
            callback(false)
        }
    }
}
