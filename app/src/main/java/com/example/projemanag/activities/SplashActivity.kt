package com.example.projemanag.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import com.example.projemanag.R
import com.example.projemanag.firebase.FirestoreClass
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAnalytics = Firebase.analytics
        val tv_app_name=findViewById<TextView>(R.id.tv_app_name)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAGS_CHANGED
        )

        val typeFace= Typeface.createFromAsset(assets,"carbon bl.ttf")
        tv_app_name.typeface=typeFace

        Handler().postDelayed({
            var currentUserID=FirestoreClass().getCurrentUserId()
            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        },2500)
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