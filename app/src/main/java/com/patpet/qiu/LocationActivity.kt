package com.patpet.qiu

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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

        // 设置地图初始位置
        val startPoint = GeoPoint(34.0522, -118.2437) // 示例经纬度
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(startPoint)

        // 添加标记
        val marker = Marker(mapView)
        marker.position = startPoint
        marker.title = "Current Location"
        mapView.overlays.add(marker)
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
}