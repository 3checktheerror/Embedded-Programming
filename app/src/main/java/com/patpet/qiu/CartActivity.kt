package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CartActivity : AppCompatActivity(){

    private var itemCount = 0
    private lateinit var prodNumTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_cart)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupIconClickListeners();

        prodNumTextView = findViewById(R.id.prod_num)
        val addButton1: ImageView = findViewById(R.id.add_btn_1)
        val addButton2: ImageView = findViewById(R.id.add_btn_2)
        val addButton3: ImageView = findViewById(R.id.add_btn_3)
        val addButton4: ImageView = findViewById(R.id.add_btn_4)


        val cartIcon: ImageView = findViewById(R.id.cartIcon)
        var clickedImage = 0;

        addButton1.setOnClickListener {
            itemCount++
            prodNumTextView.text = itemCount.toString()
            clickedImage = 1;
        }

        addButton2.setOnClickListener {
            itemCount++
            prodNumTextView.text = itemCount.toString()
            clickedImage = 2;
        }

        addButton3.setOnClickListener {
            itemCount++
            prodNumTextView.text = itemCount.toString()
            clickedImage = 3;
        }

        addButton4.setOnClickListener {
            itemCount++
            prodNumTextView.text = itemCount.toString()
            clickedImage = 4;
        }

        cartIcon.setOnClickListener {
            // Explicit Intent
            val intent = Intent(this, ShoppingActivity::class.java)
            intent.putExtra("itemCount", itemCount)
            if(clickedImage == 1) {
                intent.putExtra("itemImage", R.mipmap.ct_sp01)
            } else if (clickedImage == 2) {
                intent.putExtra("itemImage", R.mipmap.ct_sp02)
            } else if (clickedImage == 3) {
                intent.putExtra("itemImage", R.mipmap.ct_sp03)
            } else {
                intent.putExtra("itemImage", R.mipmap.ct_sp04)
            }

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