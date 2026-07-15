package com.futurecode.scarymonstercallchat.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding

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
