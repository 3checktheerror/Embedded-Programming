package com.patpet.qiu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_list)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dropdown: Spinner = findViewById(R.id.locationSpinner)
        val items = arrayOf("Kualar Lumpur", "Ipoh", "Langkawi")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter

        setupIconClickListeners()

        val exploreButton: Button = findViewById(R.id.exploreButton)
        exploreButton.setOnClickListener {
            val url = "https://www.petfinder.my/listings.php"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun setupIconClickListeners() {
        val homeIcon: ImageView = findViewById(R.id.icon_home)
        val listIcon: ImageView = findViewById(R.id.icon_list)
        val cartIcon: ImageView = findViewById(R.id.icon_cart)
        val locationIcon: ImageView = findViewById(R.id.icon_location)
        val settingsIcon: ImageView = findViewById(R.id.icon_setting)
        val currentActivity = javaClass.simpleName

        homeIcon.setOnClickListener {
            if (currentActivity != "MainActivity") {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        listIcon.setOnClickListener {
            if (currentActivity != "ListActivity") {
                val intent = Intent(this, ListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        cartIcon.setOnClickListener {
            if (currentActivity != "CartActivity") {
                val intent = Intent(this, CartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        locationIcon.setOnClickListener {
            if (currentActivity != "LocationActivity") {
                val intent = Intent(this, LocationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        settingsIcon.setOnClickListener {
            if (currentActivity != "SettingActivity") {
                val intent = Intent(this, SettingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }
    }
}
