package com.example.thesharpexperience

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.signup).setOnClickListener{
            startActivity(Intent(applicationContext, SignupActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.Login).setOnClickListener {
            val email = findViewById<TextView>(R.id.email).text.toString().trim()
            val password = findViewById<TextView>(R.id.password).text.toString().trim()


            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    startActivity(Intent(applicationContext, FragDisplayActivity::class.java))
                }else{
                    Toast.makeText(this, "Email and Password do not match", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}