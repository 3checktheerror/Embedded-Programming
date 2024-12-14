package com.patpet.qiu

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "qiu.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE_NUMBER = "phoneNumber"
        const val COLUMN_ROLE = "role"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_AGE = "age"
        const val COLUMN_ADDRESS = "address"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_PHONE_NUMBER TEXT," +
                "$COLUMN_ROLE TEXT," +
                "$COLUMN_GENDER TEXT," +
                "$COLUMN_AGE INTEGER," +
                "$COLUMN_ADDRESS TEXT)"
        db.execSQL(createTable)
        insertInitialData(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
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
            db.insert(TABLE_NAME, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAdminCount(): Int {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
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
            TABLE_NAME,
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
            db.insert(TABLE_NAME, null, values) != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun checkUserCredentials(username: String, password: String): String {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_ROLE),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )
        var role = "";
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
        }
        cursor.close()
        return role
    }

}
