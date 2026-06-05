package com.futurecode.scarymonstercallchat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding

    // ✅ FIX: Inject the language configuration into the Activity's base context
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyApplication.setLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment!!.navController
    }

    fun goToMain() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}