package com.example.resist

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.Password
import kotlinx.android.synthetic.main.activity_login.email

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        createaccount_btn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        loginbtn.setOnClickListener {
            progressBar2.setVisibility(View.VISIBLE)
            doLogin()
        }

        passwordresetbtn.setOnClickListener{
           val builder  = AlertDialog.Builder(this)
            builder.setTitle(Html.fromHtml("<font color='#000000'>Forgot Password</font>"))
            val view = layoutInflater.inflate(R.layout.forgotpassword, null)
           val username = view.findViewById<EditText>(R.id.et_username)
            builder.setView(view)
            builder.setPositiveButton(Html.fromHtml("<font color='#000000'>Reset Password</font>"), DialogInterface.OnClickListener { _ , _ ->
            forgotPassword(username)
            } )
            builder.setNegativeButton(Html.fromHtml("<font color='#000000'>Close</font>"), DialogInterface.OnClickListener { _ , _ ->  } )
            builder.show()
        }

    }

    private fun forgotPassword(username : EditText) {
               if(username.text.toString().isEmpty()){
                   username.error = "Please enter email"
              return
           }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            username.error = "Please enter valid email"
            return
        }
       auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun doLogin() {

        if (email.text.toString().isEmpty()) {
            progressBar2.setVisibility(View.INVISIBLE)
            email.error = "Please enter email"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            progressBar2.setVisibility(View.INVISIBLE)
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (Password.text.toString().isEmpty()) {
            progressBar2.setVisibility(View.INVISIBLE)
            Password.error = "Please enter Password"
            Password.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(email.text.toString(), Password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                }

                else {
                    updateUI(null)
                    progressBar2.setVisibility(View.INVISIBLE)
                }
            }


    }


   private fun updateUI(currentUser: FirebaseUser?) {
       if (currentUser != null) {
    if (currentUser.isEmailVerified) {
                startActivity(Intent(this, BlocosActivity::class.java))
        progressBar2.setVisibility(View.INVISIBLE)
        finish()
            }
            else{
                Toast.makeText(baseContext, "Please verify your email adress.", Toast.LENGTH_SHORT).show()
        progressBar2.setVisibility(View.INVISIBLE)
            }
        }
            else {
                Toast.makeText(baseContext, "Login failed. Check your password or email.", Toast.LENGTH_SHORT).show()
                progressBar2.setVisibility(View.INVISIBLE)
            }

    }
}

