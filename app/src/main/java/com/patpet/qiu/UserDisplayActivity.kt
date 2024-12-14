package com.patpet.qiu

import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.appbar.MaterialToolbar

class UserDisplayActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var userList: RecyclerView
    private lateinit var adminList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_display)

        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.title = "User Display Page"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        topAppBar.setNavigationOnClickListener { onBackPressed() }

        dbHelper = DatabaseHelper(this)

        userList = findViewById(R.id.userList)
        adminList = findViewById(R.id.adminList)

        refreshUsers()
    }

    private fun refreshUsers() {
        val users = getUsersFromDatabase()
        val admins = users.filter { it.role == "admin" }
        val normalUsers = users.filter { it.role == "user" }

        val userCount: TextView = findViewById(R.id.userCount)
        val adminCount: TextView = findViewById(R.id.adminCount)
        userCount.text = "Users: ${normalUsers.size}"
        adminCount.text = "Admins: ${admins.size}"

        userList.layoutManager = LinearLayoutManager(this)
        userList.adapter = UserAdapter(
            normalUsers,
            onUpdateClicked = { user -> handleUpdateUser(user) },
            onDeleteClicked = { user -> handleDeleteUser(user) }
        )

        adminList.layoutManager = LinearLayoutManager(this)
        adminList.adapter = UserAdapter(
            admins,
            onUpdateClicked = { user -> handleUpdateUser(user) },
            onDeleteClicked = { user -> handleDeleteUser(user) }
        )
    }

    private fun getUsersFromDatabase(): List<User> {
        val db = dbHelper.readableDatabase
        val users = mutableListOf<User>()
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null, null, null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                val username = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME))
                val password = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
                val phoneNumber = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE_NUMBER))
                val role = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE))
                val gender = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENDER))
                val age = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE))
                val address = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))
                users.add(User(username, password, phoneNumber, role, gender, age, address))
            }
        }
        return users
    }

    private fun handleUpdateUser(user: User) {
        val dialog = UpdateUserDialogFragment.newInstance(user)
        dialog.show(supportFragmentManager, "UpdateUserDialog")
        dialog.setOnUpdateListener { updatedUser ->
            val db = dbHelper.writableDatabase
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_USERNAME, updatedUser.username)
                put(DatabaseHelper.COLUMN_PASSWORD, updatedUser.password)
                put(DatabaseHelper.COLUMN_PHONE_NUMBER, updatedUser.phoneNumber)
                put(DatabaseHelper.COLUMN_ROLE, updatedUser.role)
                put(DatabaseHelper.COLUMN_GENDER, updatedUser.gender)
                put(DatabaseHelper.COLUMN_AGE, updatedUser.age)
                put(DatabaseHelper.COLUMN_ADDRESS, updatedUser.address)
            }
            db.update(
                DatabaseHelper.TABLE_NAME,
                contentValues,
                "${DatabaseHelper.COLUMN_USERNAME} = ?",
                arrayOf(user.username)
            )
            refreshUsers()
        }
    }

    private fun handleDeleteUser(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete ${user.username}?")
            .setPositiveButton("Delete") { _, _ ->
                val db = dbHelper.writableDatabase
                db.delete(
                    DatabaseHelper.TABLE_NAME,
                    "${DatabaseHelper.COLUMN_USERNAME} = ?",
                    arrayOf(user.username)
                )
                refreshUsers()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

