package org.hango.com

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import org.hango.com.Data.TravelViewModel
import org.hango.com.Data.UserInfoData
import org.hango.com.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var userModel: TravelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        userModel = ViewModelProvider(this).get(TravelViewModel::class.java)
        userModel.setLiveItems(
            UserInfoData(
                intent.getStringExtra("userID")!!,
                intent.getStringExtra("userName")!!,
                intent.getStringExtra("userProfile")!!,
                intent.getStringExtra("userMbti")!!
            )
        )

        Log.d("Life", "Main")

        initNavigation()

        setContentView(binding.root)
    }


    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val mNavController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navBar, mNavController)
    }



}