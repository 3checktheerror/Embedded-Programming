package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupIconClickListeners();

        setupExpandedText();

        setupFragmentNavigation();
    }

    private fun setupFragmentNavigation() {
        val button1Desc: TextView = findViewById(R.id.main_more_01)
        val button2Desc: TextView = findViewById(R.id.main_more_02)
        val button3Desc: TextView = findViewById(R.id.main_more_03)

        button1Desc.setOnClickListener {
            openFragment1(MainFrag1())
        }

        button2Desc.setOnClickListener {
            openFragment2(MainFrag2())
        }

        button3Desc.setOnClickListener {
            openFragment3(MainFrag3())
        }

    }

    private fun openFragment1(fragment: Fragment) {
        val fragmentContainer: View = findViewById(R.id.fragment_main_01)
        fragmentContainer.visibility = View.VISIBLE

        findViewById<View>(R.id.app_bar_main).visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_main_01, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun openFragment2(fragment: Fragment) {
        val fragmentContainer: View = findViewById(R.id.fragment_main_02)
        fragmentContainer.visibility = View.VISIBLE

        findViewById<View>(R.id.app_bar_main).visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_main_02, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun openFragment3(fragment: Fragment) {
        val fragmentContainer: View = findViewById(R.id.fragment_main_03)
        fragmentContainer.visibility = View.VISIBLE

        findViewById<View>(R.id.app_bar_main).visibility = View.GONE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_main_03, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun setupExpandedText() {
        val description1: TextView = findViewById(R.id.textView1_main)
        val description2: TextView = findViewById(R.id.textView2_main)
        val description3: TextView = findViewById(R.id.textView3_main)

        setExpandableTextViewClickListener(description1)
        setExpandableTextViewClickListener(description2)
        setExpandableTextViewClickListener(description3)
    }

    private fun setExpandableTextViewClickListener(textView: TextView) {
        textView.setOnClickListener {
            if (textView.maxLines == 4) {
                textView.maxLines = Int.MAX_VALUE
            } else {
                textView.maxLines = 4
            }
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

    override fun onBackPressed() {
        val fragmentContainer01: View = findViewById(R.id.fragment_main_01)
        val fragmentContainer02: View = findViewById(R.id.fragment_main_02)
        val fragmentContainer03: View = findViewById(R.id.fragment_main_03)
        val scrollView: View = findViewById(R.id.main_scroll)
        val appBar: View = findViewById(R.id.app_bar_main)

        if (fragmentContainer01.visibility == View.VISIBLE) {
            fragmentContainer01.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            appBar.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        } else if (fragmentContainer02.visibility == View.VISIBLE) {
            fragmentContainer02.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            appBar.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        } else if (fragmentContainer03.visibility == View.VISIBLE) {
            fragmentContainer03.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            appBar.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }


}