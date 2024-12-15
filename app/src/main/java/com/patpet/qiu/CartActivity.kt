package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var userRole: String = "user"
    private var username: String = ""

    // 用于存放当前从数据库获取的产品列表
    private var productList: List<Map<String, Any>> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContentView(R.layout.activity_cart)

        // 从会话中获取登录用户，若无则默认bob（请根据实际情况修改）
        username = UserSession.username ?: "bob"
        userRole = dbHelper.getUserRole(username)

        // 初始化加载产品数据
        loadProductsFromDB()

        // 更新订单数量显示
        updateProdNum()

        // 根据角色显示update按钮（admin可见）
        if (userRole == "admin") {
            findViewById<ImageView>(R.id.update_btn_1).visibility = android.view.View.VISIBLE
            findViewById<ImageView>(R.id.update_btn_2).visibility = android.view.View.VISIBLE
            findViewById<ImageView>(R.id.update_btn_3).visibility = android.view.View.VISIBLE
            findViewById<ImageView>(R.id.update_btn_4).visibility = android.view.View.VISIBLE
        }

        // 添加商品事件
        findViewById<ImageView>(R.id.add_btn_1).setOnClickListener {
            addProductToOrder(productList.getOrNull(0)?.get("name") as? String ?: "")
        }
        findViewById<ImageView>(R.id.add_btn_2).setOnClickListener {
            addProductToOrder(productList.getOrNull(1)?.get("name") as? String ?: "")
        }
        findViewById<ImageView>(R.id.add_btn_3).setOnClickListener {
            addProductToOrder(productList.getOrNull(2)?.get("name") as? String ?: "")
        }
        findViewById<ImageView>(R.id.add_btn_4).setOnClickListener {
            addProductToOrder(productList.getOrNull(3)?.get("name") as? String ?: "")
        }

        // 更新商品事件(仅admin)
        if (userRole == "admin") {
            findViewById<ImageView>(R.id.update_btn_1).setOnClickListener {
                val oldName = productList.getOrNull(0)?.get("name") as? String ?: ""
                showUpdateDialog(oldName)
            }
            findViewById<ImageView>(R.id.update_btn_2).setOnClickListener {
                val oldName = productList.getOrNull(1)?.get("name") as? String ?: ""
                showUpdateDialog(oldName)
            }
            findViewById<ImageView>(R.id.update_btn_3).setOnClickListener {
                val oldName = productList.getOrNull(2)?.get("name") as? String ?: ""
                showUpdateDialog(oldName)
            }
            findViewById<ImageView>(R.id.update_btn_4).setOnClickListener {
                val oldName = productList.getOrNull(3)?.get("name") as? String ?: ""
                showUpdateDialog(oldName)
            }
        }

        // 点击购物车图标跳转
        val cartIcon: ImageView = findViewById(R.id.cartIcon)
        cartIcon.setOnClickListener {
            val intent = Intent(this, ShoppingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateProdNum() {
        val count = dbHelper.getOrderCount(username)
        findViewById<TextView>(R.id.prod_num).text = count.toString()
    }

    private fun loadProductsFromDB() {
        productList = dbHelper.getAllProducts()
        updateProductUI()
    }

    private fun updateProductUI() {
        if (productList.size > 0) {
            findViewById<TextView>(R.id.cart_row2_01_01).text = productList[0]["name"].toString()
            findViewById<TextView>(R.id.cart_row2_01_02).text = productList[0]["desp"].toString()
            findViewById<TextView>(R.id.productPrice_1).text = "RM %.2f".format(productList[0]["price"])
        }
        if (productList.size > 1) {
            findViewById<TextView>(R.id.cart_row2_02_01).text = productList[1]["name"].toString()
            findViewById<TextView>(R.id.cart_row2_02_02).text = productList[1]["desp"].toString()
            findViewById<TextView>(R.id.productPrice_2).text = "RM %.2f".format(productList[1]["price"])
        }
        if (productList.size > 2) {
            findViewById<TextView>(R.id.cart_row2_03_01).text = productList[2]["name"].toString()
            findViewById<TextView>(R.id.cart_row2_03_02).text = productList[2]["desp"].toString()
            findViewById<TextView>(R.id.productPrice_3).text = "RM %.2f".format(productList[2]["price"])
        }
        if (productList.size > 3) {
            findViewById<TextView>(R.id.cart_row2_04_01).text = productList[3]["name"].toString()
            findViewById<TextView>(R.id.cart_row2_04_02).text = productList[3]["desp"].toString()
            findViewById<TextView>(R.id.productPrice_4).text = "RM %.2f".format(productList[3]["price"])
        }
    }

    private fun addProductToOrder(productName: String) {
        if (productName.isNotEmpty()) {
            val success = dbHelper.addOrder(username, productName)
            if (success) {
                Toast.makeText(this, "Added $productName to order", Toast.LENGTH_SHORT).show()
                updateProdNum()
            } else {
                Toast.makeText(this, "Failed to add $productName", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpdateDialog(oldName: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_product, null)
        val nameEdit = dialogView.findViewById<EditText>(R.id.editName)
        val despEdit = dialogView.findViewById<EditText>(R.id.editDesp)
        val priceEdit = dialogView.findViewById<EditText>(R.id.editPrice)

        val oldProduct = productList.find { it["name"] == oldName }
        if (oldProduct != null) {
            nameEdit.setText(oldProduct["name"].toString())
            despEdit.setText(oldProduct["desp"].toString())
            priceEdit.setText(oldProduct["price"].toString())
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Product")
        builder.setView(dialogView)
        builder.setPositiveButton("Update") { _, _ ->
            val newName = nameEdit.text.toString().trim()
            val newDesp = despEdit.text.toString().trim()
            val newPrice = priceEdit.text.toString().toDoubleOrNull() ?: 0.0

            if (newName.isEmpty() || newDesp.isEmpty() || newPrice <= 0) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val success = dbHelper.updateProduct(oldName, newName, newDesp, newPrice)
            if (success) {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show()
                refreshProducts()
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun refreshProducts() {
        loadProductsFromDB()
    }
}
