package com.example.chatdiary

import EncryptionUtils
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chatdiary.data.LoginDataSource
import com.example.chatdiary.data.LoginRepository
import com.example.chatdiary.databinding.ActivityMainBinding
import com.example.chatdiary.ui.login.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var encryptionUtils: EncryptionUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        encryptionUtils = EncryptionUtils(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // 隐藏底部导航栏
            navView.visibility = View.GONE
            binding.root.visibility=View.GONE
            val loginFragment = LoginFragment()
            loginFragment.setOnLoginSuccessListener(object : LoginFragment.OnLoginSuccessListener {
                override fun onLoginSuccess() {
                    navView.visibility = View.VISIBLE
                    binding.root.visibility=View.VISIBLE
                }
            })
            showFragment(loginFragment)
        } else {
            val savedUsername = encryptionUtils.decrypt("username")
            val savedPassword = encryptionUtils.decrypt("password")
            val loginRepository = LoginRepository(
                dataSource = LoginDataSource()
            )
            loginRepository.login(savedUsername, savedPassword)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }
}
