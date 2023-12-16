package com.example.thesharpexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.thesharpexperience.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.signup).setOnClickListener{
            val email = findViewById<TextView>(R.id.email).text.toString().trim()
            val password = findViewById<TextView>(R.id.password).text.toString().trim()
            val confirmpassword = findViewById<TextView>(R.id.repassword).text.toString().trim()
            val name = findViewById<TextView>(R.id.name).text.toString().trim()
            //FIREBASE AUTH REQUIRES NON EMPTY EMAIL/PASSWORD FIELDS ALONG WITH AT LEAST A 6 CHARACTER PASSWORD
            val passwordSize = password.length
            if(password != confirmpassword)
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            else if (passwordSize < 8)
                Toast.makeText(this, "Password must be 8 characters long", Toast.LENGTH_SHORT).show()
            else if (password.isEmpty() || confirmpassword.isEmpty() || email.isEmpty())
                Toast.makeText(this, "Cannot have empty fields", Toast.LENGTH_SHORT).show()
            else if(password == confirmpassword){ //passes requirements and password match
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
    private fun addToFirebase(email : String,name : String){
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