package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ShoppingActivity : AppCompatActivity() {

    private lateinit var itemImageView: ImageView
    private lateinit var itemCountTextView: TextView
    private lateinit var returnButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        itemImageView = findViewById(R.id.item_image)
        itemCountTextView = findViewById(R.id.item_count)
        returnButton = findViewById(R.id.return_button)

        val itemCount = intent.getIntExtra("itemCount", 0)
        val itemImage = intent.getIntExtra("itemImage", R.mipmap.ct_sp01)

        itemImageView.setImageResource(itemImage)
        itemCountTextView.text = "Item Count: $itemCount"

        returnButton.setOnClickListener {
            returnToCartActivity()
        }
    }

    private fun returnToCartActivity() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }
}
