package com.patpet.qiu

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "qiu.db"
        const val DATABASE_VERSION = 4

        // users table
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE_NUMBER = "phoneNumber"
        const val COLUMN_ROLE = "role"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_AGE = "age"
        const val COLUMN_ADDRESS = "address"

        // product table
        const val TABLE_PRODUCT = "product"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESP = "desp"
        const val COLUMN_PRICE = "price"
        const val COLUMN_PICTURE = "picture"

        // order table
        const val TABLE_ORDER = "orders"
        const val COLUMN_ORDER_ID = "order_id"
        const val COLUMN_ORDER_USERNAME = "username"
        const val COLUMN_STATE = "state"
        const val COLUMN_ORDER_PRODUCT_NAME = "name"
        const val COLUMN_ORDER_PRODUCT_PICTURE = "picture"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_PHONE_NUMBER TEXT," +
                "$COLUMN_ROLE TEXT," +
                "$COLUMN_GENDER TEXT," +
                "$COLUMN_AGE INTEGER," +
                "$COLUMN_ADDRESS TEXT)"
        db.execSQL(createUserTable)

        val createProductTable = "CREATE TABLE $TABLE_PRODUCT (" +
                "$COLUMN_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_DESP TEXT," +
                "$COLUMN_PRICE REAL," +
                "$COLUMN_PICTURE TEXT)"
        db.execSQL(createProductTable)

        // 新增picture列
        val createOrderTable = "CREATE TABLE $TABLE_ORDER (" +
                "$COLUMN_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_ORDER_USERNAME TEXT," +
                "$COLUMN_STATE INTEGER," +
                "$COLUMN_ORDER_PRODUCT_NAME TEXT," +
                "$COLUMN_ORDER_PRODUCT_PICTURE TEXT)"
        db.execSQL(createOrderTable)

        insertInitialData(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        // 初始化users表
        val users = listOf(
            User("alice", "pass123", "1234567890", "user", "Female", 25, "123 Street"),
            User("bob", "pass456", "0987654321", "admin", "Male", 30, "456 Avenue"),
            User("charlie", "pass789", "1112223333", "user", "Male", 28, "789 Road"),
            User("diana", "passabc", "2223334444", "admin", "Female", 32, "321 Lane")
        )
        for (user in users) {
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_PHONE_NUMBER, user.phoneNumber)
                put(COLUMN_ROLE, user.role)
                put(COLUMN_GENDER, user.gender)
                put(COLUMN_AGE, user.age)
                put(COLUMN_ADDRESS, user.address)
            }
            db.insert(TABLE_USERS, null, values)
        }

        // 初始化product表
        val products = listOf(
            Triple("Chicken Meal", "High protein", 25.00),
            Triple("Salmon", "Rich in Omega-3", 15.00),
            Triple("Brown Rice", "Provides fiber", 18.00),
            Triple("Blueberries", "Rich in antioxidants", 23.00)
        )
        val pictures = listOf("@mipmap/ct_sp01", "@mipmap/ct_sp02", "@mipmap/ct_sp03", "@mipmap/fdcat04")

        products.forEachIndexed { index, (name, desp, price) ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, name)
                put(COLUMN_DESP, desp)
                put(COLUMN_PRICE, price)
                put(COLUMN_PICTURE, pictures[index])
            }
            db.insert(TABLE_PRODUCT, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER")
        onCreate(db)
    }

    fun getUserRole(username: String): String {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_ROLE), "$COLUMN_USERNAME = ?", arrayOf(username), null, null, null)
        var role = "user"
        if (cursor.moveToFirst()) {
            role = cursor.getString(0)
        }
        cursor.close()
        return role
    }

    fun getOrderCount(username: String): Int {
        val db = readableDatabase
        val cursor = db.query(TABLE_ORDER, arrayOf("COUNT(*)"), "$COLUMN_ORDER_USERNAME = ?", arrayOf(username), null, null, null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    // 修改订单查询方法，包含picture
    fun getAllOrdersByUser(username: String): List<Map<String,String>> {
        val db = readableDatabase
        val cursor = db.query(TABLE_ORDER, arrayOf(COLUMN_ORDER_PRODUCT_NAME, COLUMN_ORDER_PRODUCT_PICTURE), "$COLUMN_ORDER_USERNAME = ?", arrayOf(username), null, null, null)
        val orders = mutableListOf<Map<String,String>>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME))
            val picture = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_PICTURE))
            orders.add(mapOf("name" to name, "picture" to picture))
        }
        cursor.close()
        return orders
    }

    // addOrder时将商品图片路径也存入orders表中
    fun addOrder(username: String, productName: String): Boolean {
        val product = getProductByName(productName)
        if (product == null) {
            return false
        }

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDER_USERNAME, username)
            put(COLUMN_STATE, 0)
            put(COLUMN_ORDER_PRODUCT_NAME, productName)
            put(COLUMN_ORDER_PRODUCT_PICTURE, product["picture"].toString()) // 保存图片路径
        }
        return db.insert(TABLE_ORDER, null, values) != -1L
    }

    // payOrders现在支付后清空state=0的订单记录
    fun payOrders(username: String): Double {
        val db = writableDatabase
        // 计算所有state=0订单的总价
        val cursor = db.query(TABLE_ORDER, arrayOf(COLUMN_ORDER_PRODUCT_NAME), "$COLUMN_ORDER_USERNAME = ? AND $COLUMN_STATE = 0", arrayOf(username), null, null, null)
        var total = 0.0
        while (cursor.moveToNext()) {
            val productName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME))
            val pCursor = db.query(TABLE_PRODUCT, arrayOf(COLUMN_PRICE), "$COLUMN_NAME = ?", arrayOf(productName), null, null, null)
            if (pCursor.moveToFirst()) {
                total += pCursor.getDouble(0)
            }
            pCursor.close()
        }
        cursor.close()

        // 支付后清空state=0的订单(直接删除)
        db.delete(TABLE_ORDER, "$COLUMN_ORDER_USERNAME = ? AND $COLUMN_STATE = 0", arrayOf(username))

        return total
    }

    fun updateProduct(oldName: String, newName: String, newDesp: String, newPrice: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
            put(COLUMN_DESP, newDesp)
            put(COLUMN_PRICE, newPrice)
            put(COLUMN_PICTURE, "@mipmap/ct_sp01")
        }
        val rows = db.update(TABLE_PRODUCT, values, "$COLUMN_NAME = ?", arrayOf(oldName))
        return rows > 0
    }

    fun getAllProducts(): List<Map<String,Any>> {
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null)
        val productList = mutableListOf<Map<String,Any>>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val desp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESP))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val picture = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE))
            productList.add(mapOf("name" to name, "desp" to desp, "price" to price, "picture" to picture))
        }
        cursor.close()
        return productList
    }

    fun getProductByName(name: String): Map<String,Any>? {
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCT, null, "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        var product: Map<String,Any>? = null
        if (cursor.moveToFirst()) {
            val pname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val desp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESP))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val picture = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PICTURE))
            product = mapOf("name" to pname, "desp" to desp, "price" to price, "picture" to picture)
        }
        cursor.close()
        return product
    }

    fun checkUserCredentials(username: String, password: String): String {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_ROLE),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )
        var role = ""
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
        }
        cursor.close()
        return role
    }

    fun getAdminCount(): Int {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf("COUNT(*)"),
            "$COLUMN_ROLE = ?",
            arrayOf("admin"),
            null, null, null
        )
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun updateUserRole(username: String, newRole: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ROLE, newRole)
        }
        val rows = db.update(
            TABLE_USERS,
            values,
            "$COLUMN_USERNAME = ?",
            arrayOf(username)
        )
        return rows > 0
    }

    fun addUser(username: String, password: String, phoneNumber: String, role: String, gender: String, age: Int, address: String): Boolean {
        return try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, username)
                put(COLUMN_PASSWORD, password)
                put(COLUMN_PHONE_NUMBER, phoneNumber)
                put(COLUMN_ROLE, role)
                put(COLUMN_GENDER, gender)
                put(COLUMN_AGE, age)
                put(COLUMN_ADDRESS, address)
            }
            db.insert(TABLE_USERS, null, values) != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteOrder(username: String, productName: String): Boolean {
        val db = writableDatabase
        val rows = db.delete(
            TABLE_ORDER,
            "$COLUMN_ORDER_USERNAME = ? AND $COLUMN_ORDER_PRODUCT_NAME = ?",
            arrayOf(username, productName)
        )
        return rows > 0
    }

}
