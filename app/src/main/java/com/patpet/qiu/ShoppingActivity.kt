package com.patpet.qiu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class ShoppingActivity : AppCompatActivity() {

    private lateinit var itemImageView: ImageView
    private lateinit var itemCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        itemImageView = findViewById(R.id.item_image)
        itemCountTextView = findViewById(R.id.item_count)

        val itemCount = intent.getIntExtra("itemCount", 0)
        val itemImage = intent.getIntExtra("itemImage", R.mipmap.ct_sp01)

        itemImageView.setImageResource(itemImage)
        itemCountTextView.text = "Item Count: $itemCount"
    }
}

