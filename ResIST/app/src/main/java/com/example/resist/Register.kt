package com.example.resist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        btnregister.setOnClickListener {
            progressBar.setVisibility(View.VISIBLE)
            RegisterUser()
        }
        signinbtn.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }
    }
  private fun RegisterUser() {

      if (name.text.toString().isEmpty()){
          progressBar.setVisibility(View.INVISIBLE)
          name.error="Please enter name"
          name.requestFocus()
          return
      }

      if (istid.text.toString().isEmpty()){
          progressBar.setVisibility(View.INVISIBLE)
          istid.error="Please enter IST id"
          istid.requestFocus()
          return
      }


    if(email.text.toString().isEmpty()){
        progressBar.setVisibility(View.INVISIBLE)
        email.error="Please enter email"
        email.requestFocus()
        return
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
        progressBar.setVisibility(View.INVISIBLE)
        email.error="Please enter valid email"
        email.requestFocus()
        return
    }

    if(Password.text.toString().isEmpty()){
        progressBar.setVisibility(View.INVISIBLE)
        Password.error="Please enter Password"
        Password.requestFocus()
        return
    }

      auth.createUserWithEmailAndPassword(email.text.toString(), Password.text.toString())
          .addOnCompleteListener(this) { task ->
              if (task.isSuccessful) {
                  val user :FirebaseUser? = auth.currentUser
                  user!!.sendEmailVerification()
                      .addOnCompleteListener { task ->
                          if (task.isSuccessful) {
                              Toast.makeText(baseContext, "Account Created with Success. Please verify your email account.", Toast.LENGTH_SHORT).show()
                              progressBar.setVisibility(View.INVISIBLE)
                          }
                      }

              } else {
                  Toast.makeText(baseContext, "Register failed. Password needs to have 6 or more caracters.",
                      Toast.LENGTH_SHORT).show()
                  progressBar.setVisibility(View.INVISIBLE)
              }

          }

}



}













