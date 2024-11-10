package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocationActivity : AppCompatActivity(){

    private lateinit var mapView: MapView
    private lateinit var infoEditText: EditText
    private lateinit var clearButton: Button
    private lateinit var postButton: Button
    private lateinit var postContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_location)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Configuration.getInstance().load(this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(R.layout.activity_location)

        mapView = findViewById(R.id.map_view)
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(34.0522, -118.2437)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(startPoint)

        val marker = Marker(mapView)
        marker.position = startPoint
        marker.title = "Current Location"
        mapView.overlays.add(marker)

        setupIconClickListeners();

        infoEditText = findViewById(R.id.infoEditText)
        clearButton = findViewById(R.id.clearButton)
        postButton = findViewById(R.id.postButton)
        postContainer = findViewById(R.id.postContainer)

        // Clear Button Logic
        clearButton.setOnClickListener {
            infoEditText.text.clear()
        }

        // Post Button Logic
        postButton.setOnClickListener {
            val infoText = infoEditText.text.toString()
            if (infoText.isNotBlank()) {
                addPost(infoText)
                infoEditText.text.clear() // Clear the EditText after posting
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDetach()
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

    private fun addPost(infoText: String) {
        val postView = LayoutInflater.from(this).inflate(R.layout.post_layout, postContainer, false)

        val postTextView: TextView = postView.findViewById(R.id.postTextView)
        postTextView.text = infoText

        postContainer.addView(postView)
    }
}