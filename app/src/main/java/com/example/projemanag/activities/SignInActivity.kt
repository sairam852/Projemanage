package com.example.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.example.projemanag.R
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.User
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SignInActivity : BaseActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        setContentView(R.layout.activity_sign_in)

        auth=FirebaseAuth.getInstance()
        val btn_signIn=findViewById<Button>(R.id.btn_sign_in)
        btn_signIn.setOnClickListener{
            Log.i(TAG,"button pressed")
            signInRegisteredUser()
        }

        //setUpActionBar()
    }
    fun signInSuccess(user: User){
            hideProgressDialog()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
    }
    override fun onResume() {
        super.onResume()
        setCurrentScreen(this.javaClass.simpleName)
    }
    private fun signInRegisteredUser(){
        val email=findViewById<EditText>(R.id.et_email_signIn).text.toString().trim()
        Log.i(TAG,email)
        val password=findViewById<EditText>(R.id.et_password_signIn).text.toString().trim()
        Log.i(TAG,password)
        if (validateForm(email,password)){
            Log.d(TAG, "validataion done")
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                       FirestoreClass().signInUser(this)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI()
                    }
                }
        }
    }
    private fun updateUI(){

    }
    private fun validateForm(email:String,password:String):Boolean{
        return when {
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a name")
                false
            }else->{
                true
            }
        }
    }
    private fun setUpActionBar(){
        val toolbar_sign_in_activity=findViewById<Toolbar>(R.id.toolbar_sign_in_activity)
        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }

    }
    companion object{
        val TAG="SignInActivity"
    }
}