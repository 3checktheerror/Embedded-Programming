package com.patpet.qiu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var adminLoginButton: Button
    private lateinit var backToSignupText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        usernameEditText = findViewById(R.id.usernameInput)
        passwordEditText = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.log_in_button)
        adminLoginButton = findViewById(R.id.admin_log_in_button)
        backToSignupText = findViewById(R.id.back_to_sign_up_text)

        // 普通用户登录
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = dbHelper.checkUserCredentials(username, password)
            if (role != "") {
                // 登录成功（此处不区分角色，只要密码匹配即成功，可根据需求变更逻辑）
                UserSession.username = username
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UserDisplayActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

        // 管理员登录
        adminLoginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = dbHelper.checkUserCredentials(username, password)
            if (role != "") {
                val currentAdminCount = dbHelper.getAdminCount()
                if (role == "admin") {
                    // 用户已经是管理员，直接登录成功
                    UserSession.username = username
                    Toast.makeText(this, "Login successful as admin", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, UserDisplayActivity::class.java)
                    startActivity(intent)
                } else if (currentAdminCount < 2) {
                    // 用户不是管理员，且管理员数量小于 2，将其升级为管理员
                    val updated = dbHelper.updateUserRole(username, "admin")
                    if (updated) {
                        UserSession.username = username
                        Toast.makeText(this, "Login successful as admin", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, UserDisplayActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failed to update role to admin", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 已达到管理员上限
                    Toast.makeText(this, "Cannot add more admins", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 用户名或密码不正确
                Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show()
            }
        }

        backToSignupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
