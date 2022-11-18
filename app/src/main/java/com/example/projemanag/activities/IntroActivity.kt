package com.example.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.projemanag.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        firebaseAnalytics = Firebase.analytics
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAGS_CHANGED
        )
        val btn_sign_in_intro=findViewById<Button>(R.id.btn_sign_in_intro)
        btn_sign_in_intro.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                )
            )
        }
        val btn_sign_up_intro=findViewById<Button>(R.id.btn_sign_up_intro)
        btn_sign_up_intro.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    override fun onResume() {
        super.onResume()
        setCurrentScreen(this.javaClass.simpleName)
    }
    fun setCurrentScreen(screenName: String) = firebaseAnalytics?.run {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

}