package com.example.thesharpexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


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

        //Redeclaring pair1 and pair 2 as false in box checks below in all check cases
        // incase signup button is pressed early and need to reset

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

            val emptyCheck = checkVals(pair1, pair2, password, confirmpassword, email, name)

            if(emptyCheck == false)
                Toast.makeText(this, "Cannot have empty fields", Toast.LENGTH_SHORT).show()
            else if(password != confirmpassword)
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            else if (passwordSize < 8)
                Toast.makeText(this, "Password must be 8 characters long", Toast.LENGTH_SHORT).show()
            else
                {
                    if(password == confirmpassword){ //passes requirements and password match
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {  //email does exist and firebase authentication accepts it
                                //check for which boxes are checked to be stored in firebase
                                // store signup values into firebase firestore and make an account associated
                                if(dayCheck.isChecked && RNCheck.isChecked)
                                    addToFirebase(email,name, 1,1)
                                else if(dayCheck.isChecked && NACheck.isChecked)
                                    addToFirebase(email,name, 1,0)
                                else if(nightCheck.isChecked && RNCheck.isChecked)
                                    addToFirebase(email,name, 0,1)
                                else if(nightCheck.isChecked && NACheck.isChecked)
                                    addToFirebase(email,name, 0,0)
                                startActivity(Intent(applicationContext, FragDisplayActivity::class.java))
                            } else { //case where email already exist
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
    private fun addToFirebase(email : String,name : String, shiftType : Int, nurseType: Int) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["email"] = email
        user["name"] = name
        user["shiftType"] = shiftType
        user["nurseType"] = nurseType
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