package com.example.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.bookapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()


        // if user doesn't have an account, go to register screen
        binding.signUp.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.LoginButton.setOnClickListener {
            vaildateData()
        }

    }
    private fun vaildateData(){
        // input data
        email = binding.EmailAddress.text.toString().trim()
        password = binding.Password.text.toString().trim()

        // validating the data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"invalid email format",Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            Toast.makeText(this,"Enter your password",Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                startActivity(Intent(this@LoginActivity,DashBoard::class.java))}

                .addOnFailureListener{e->
                    //failed to login
                    Toast.makeText(this,"Login Failed due to ${e.message}",Toast.LENGTH_LONG).show()
                }
        }
    }
}