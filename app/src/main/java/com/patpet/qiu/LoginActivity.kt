package com.patpet.qiu


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.patpet.qiu.MainActivity
import com.patpet.qiu.R

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backToSignupText: TextView = findViewById(R.id.back_to_sign_up_text)
        backToSignupText.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.log_in_button)

        val loginButton: Button = findViewById(R.id.log_in_button)
        loginButton.setOnClickListener {
            if (validateLogin()) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun validateLogin(): Boolean {
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()

        val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val registeredUsername = sharedPref.getString("username", "")
        val registeredPassword = sharedPref.getString("password", "")

        return if (username == registeredUsername && password == registeredPassword) {
            true
        } else {
            showAlert("Username and password do not match.")
            false
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alert = builder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.purple_blue))
    }
}
