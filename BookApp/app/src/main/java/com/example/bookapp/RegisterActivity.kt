package com.example.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.bookapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Activity for user registration
class  RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // Firebase authentication instance
    private lateinit var firebaseAuth: FirebaseAuth

    private var name = ""
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        // Set click listener for register button
        binding.regButton.setOnClickListener {
            validateData() // Validate user input data
        }
    }

    // Validate user input data
    private fun validateData(){
        // Retrieve user input
        name = binding.nameText.text.toString().trim()
        email = binding.emailAddress.text.toString().trim()
        password = binding.editPassword.text.toString().trim()
        val confPassword = binding.editPassword2.text.toString().trim()

        // Check if user input is correct
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Your Email is Invalid....",Toast.LENGTH_SHORT).show()
        }
        else if (password != confPassword){
            Toast.makeText(this,"Password Doesn't match the confimed Password....",Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount() // Create user account if input is valid
        }
    }

    // Create user account using Firebase authentication
    private fun createUserAccount() {
        // Creating user in Firebase authentication
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            updateUserInfo() // Account created, add user to the database
            Toast.makeText(this,"The account is created",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity,DashBoard::class.java)) // Redirect to dashboard
        }
            .addOnFailureListener{e->
                // Display error message if account creation fails
                Toast.makeText(this,"Failed To Create an Account due to ${e.message}",Toast.LENGTH_LONG).show()
            }
    }

    // Update user info in Firebase database
    private fun updateUserInfo(){

        // Setting up the data to add to the database
        val hashMap:HashMap<String,Any?> = HashMap()
        hashMap["email"] = email
        hashMap["name"] = name

        // Set the data to the database
        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.setValue(hashMap).addOnSuccessListener {
            // User info saved successfully
            Toast.makeText(this,"Account is created...",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity,DashBoard::class.java)) // Redirect to dashboard
            finish()
        }
            .addOnFailureListener{e->
                // Failed to add the data to the database
                Toast.makeText(this,"Failed to save user info due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}
