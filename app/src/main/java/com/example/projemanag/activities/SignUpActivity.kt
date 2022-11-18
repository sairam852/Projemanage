package com.example.projemanag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.projemanag.R
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class SignUpActivity : BaseActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAnalytics = Firebase.analytics
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAGS_CHANGED
        )

        val btn_sign_up=findViewById<Button>(R.id.btn_sign_up)
        btn_sign_up.setOnClickListener{
            registerUser()
        }

        //setUpActionBar()
    }
    fun userRegisteredSuccess(){
        Toast.makeText(
            this@SignUpActivity,
            "you have successfully registered",
            Toast.LENGTH_SHORT
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()

    }
    override fun onResume() {
        super.onResume()
        setCurrentScreen(this.javaClass.simpleName)
    }
    private fun setUpActionBar(){
        val toolbar_sign_up_activity=findViewById<Toolbar>(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar=supportActionBar
        if(actionBar!=null){
            with(actionBar) {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            }
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun registerUser(){
        val et_name=findViewById<EditText>(R.id.et_name)
        val et_email=findViewById<EditText>(R.id.et_email)
        val et_password=findViewById<EditText>(R.id.et_password)
        val name:String=et_name.text.toString().trim{it<= ' '}
        val email:String=et_email.text.toString().trim{it<= ' '}
        val password:String=et_password.text.toString().trim{it<= ' '}
        if (validateForm(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            /*Toast.makeText(this@SignUpActivity,"Now you can register a new user",
                Toast.LENGTH_SHORT).show()*/
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // Hide the progress dialog
//                        hideProgressDialog()

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!
                            val user= User(firebaseUser.uid,name,registeredEmail)
                            FirestoreClass().registerUser(this,user)
                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Intro Screen for Sign-In
                             */

                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Intro Screen for Sign-In
                             */
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }
    private fun validateForm(name:String,email:String,password:String):Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }else->{
                true
            }
        }
    }

}