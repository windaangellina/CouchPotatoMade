package com.winda.couchpotato.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.winda.couchpotato.R
import com.winda.couchpotato.ui.catalogue.CatalogueActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //menghilangkan ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler(mainLooper)
        handler.postDelayed({
            startActivity(Intent(applicationContext, CatalogueActivity::class.java))
            finish()
        }, 3000L) //3000 L = 3 detik
    }
}