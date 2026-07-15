package com.futurecode.scarymonstercallchat.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navController = navHostFragment?.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
