package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ShoppingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var orderContainer: LinearLayout
    private lateinit var itemCountText: TextView
    private lateinit var currentAmountText: TextView
    private lateinit var totalMoneyText: TextView

    private var username: String = ""
    private var totalMoney = 1000.00 // 初始化总金额为1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        dbHelper = DatabaseHelper(this)
        username = UserSession.username ?: "bob"

        itemCountText = findViewById(R.id.item_count)
        currentAmountText = findViewById(R.id.current_amount)
        orderContainer = findViewById(R.id.order_container)
        totalMoneyText = findViewById(R.id.total_money_tv)

        // 初始化总金额显示
        totalMoneyText.text = "Total Money: %.2f".format(totalMoney)

        loadOrders()

        // Pay按钮
        val payButton: Button = findViewById(R.id.pay_button)
        payButton.setOnClickListener {
            payForOrders()
        }

        // Return按钮返回CartActivity
        val returnButton: Button = findViewById(R.id.return_button)
        returnButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadOrders() {
        val orders = dbHelper.getAllOrdersByUser(username) // 现在已返回name和picture列表
        orderContainer.removeAllViews()

        // 展示订单数量
        itemCountText.text = "Item Count: ${orders.size}"

        var total = 0.0

        for (order in orders) {
            val productName = order["name"].toString()
            val pictureStr = order["picture"].toString()

            // 为每个订单项目动态加载布局
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_order, orderContainer, false)
            val productImage = itemView.findViewById<ImageView>(R.id.productImage)
            val productNameTv = itemView.findViewById<TextView>(R.id.productName)
            val productDespTv = itemView.findViewById<TextView>(R.id.productDesp)
            val productPriceTv = itemView.findViewById<TextView>(R.id.productPrice)
            val deleteButton = itemView.findViewById<Button>(R.id.deleteButton)

            // 因为订单表中只有name和picture,需要从product表再获取描述和价格
            val product = dbHelper.getProductByName(productName)
            val price = product?.get("price") as? Double ?: 0.0
            val desp = product?.get("desp") as? String ?: ""

            total += price

            productNameTv.text = productName
            productDespTv.text = desp
            productPriceTv.text = "RM %.2f".format(price)

            // 加载图片
            val picName = if (pictureStr.startsWith("@mipmap/")) pictureStr.substring("@mipmap/".length) else ""
            val imageResId = if (picName.isNotEmpty()) {
                resources.getIdentifier(picName, "mipmap", packageName)
            } else 0

            if (imageResId != 0) {
                productImage.setImageResource(imageResId)
            } else {
                productImage.setImageResource(R.mipmap.ic_launcher)
            }

            // 删除按钮点击事件
            deleteButton.setOnClickListener {
                dbHelper.deleteOrder(username, productName)
                loadOrders() // 重新加载订单列表
            }

            orderContainer.addView(itemView)
        }

        // 当前总额展示
        currentAmountText.text = "Current Amount: %.2f".format(total)
    }

    private fun payForOrders() {
        val totalPaid = dbHelper.payOrders(username)
        if (totalPaid > 0) {
            Toast.makeText(this, "Paid RM %.2f".format(totalPaid), Toast.LENGTH_SHORT).show()

            // 从Total Money中扣除相应金额
            totalMoney -= totalPaid
            totalMoneyText.text = "Total Money: %.2f".format(totalMoney)
        } else {
            Toast.makeText(this, "No pending orders to pay.", Toast.LENGTH_SHORT).show()
        }
        // 支付完成后重新加载列表显示更新结果
        loadOrders()
    }
}
