package com.example.thesharpexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.thesharpexperience.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    //variables to hold check box values
    private lateinit var dayCheck : CheckBox
    private lateinit var nightCheck : CheckBox
    private lateinit var RNCheck : CheckBox
    private lateinit var NACheck : CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //variables of type checkBox to store live checked values
        dayCheck  = findViewById(R.id.CBDays)
        nightCheck  = findViewById(R.id.CBNights)
        RNCheck  = findViewById(R.id.CB_RN)
        NACheck = findViewById(R.id.CB_NA)

        //bools to be sent to checkVals for checking purposes
        var pair1: Boolean = false
        var pair2: Boolean = false

        //Redeclaring pair1 and pair 2 as false in box checks below incase
            //signup button is pressed early and need to reset


        //Set of two pairs of logic
        //first pair actively listens to Days and Night check box to
        //  make sure only one value is checked at once
        dayCheck.setOnCheckedChangeListener { _, isChecked ->
            pair1 = false
            if(isChecked){
                nightCheck.isChecked = false
                pair1 = true
            }
        }
        nightCheck.setOnCheckedChangeListener { _, isChecked ->
            pair1 = false
            if(isChecked){
                dayCheck.isChecked = false
                pair1 = true
            }
        }

        //second pair actively listens to Registered Nurse and Nursing Assistant
        // check box to make sure only one value is checked at once

        RNCheck.setOnCheckedChangeListener { _, isChecked ->
            pair2 = false
            if(isChecked){
                NACheck.isChecked = false
                pair2 = true
            }
        }
        NACheck.setOnCheckedChangeListener { _, isChecked ->
            pair2 = false
            if(isChecked){
                RNCheck.isChecked = false
                pair2 = true
            }
        }

        auth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.signup).setOnClickListener{
            val email = findViewById<TextView>(R.id.email).text.toString().trim()
            val password = findViewById<TextView>(R.id.password).text.toString().trim()
            val confirmpassword = findViewById<TextView>(R.id.repassword).text.toString().trim()
            val name = findViewById<TextView>(R.id.name).text.toString().trim()
            //FIREBASE AUTH REQUIRES NON EMPTY EMAIL/PASSWORD FIELDS ALONG WITH AT LEAST A 6 CHARACTER PASSWORD
            val passwordSize = password.length

            val nullcheck = checkVals(pair1, pair2, password, confirmpassword, email, name)

            if(nullcheck == false)
                Toast.makeText(this, "Cannot have empty fields", Toast.LENGTH_SHORT).show()
            else if(password != confirmpassword)
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            else if (passwordSize < 8)
                Toast.makeText(this, "Password must be 8 characters long", Toast.LENGTH_SHORT).show()
            else
                {
                    if(password == confirmpassword){ //passes requirements and password match
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {  //email doesnt exist and firebase authentication accepts it

                            //store sighnup values into firebase firestore and make an account associated
                            addToFirebase(email, name)

                            startActivity(Intent(applicationContext, FragDisplayActivity::class.java))
                            } else {
                                Toast.makeText(this, "Not created", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

        }
    }


    private fun checkVals(pair1:Boolean, pair2: Boolean, password : String, confirmPassword : String, email: String, name: String) : Boolean
    {
        if(pair1 == false || (pair2 == false) || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || name.isEmpty())
            return false
        else
            return true
    }
    private fun addToFirebase(email : String,name : String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["email"] = email
        user["name"] = name
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Firebase saved ", "$user")
            }
            .addOnFailureListener {
                Log.d("Firebase failed ", "$user")
            }
    }
}