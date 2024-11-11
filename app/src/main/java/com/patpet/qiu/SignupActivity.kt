package com.patpet.qiu


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

class SignupActivity : AppCompatActivity() {


    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var phoneNumberInput: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_singup)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_signup)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        phoneNumberInput = findViewById(R.id.phoneNumberInput)
        signUpButton = findViewById(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            if (validateInput()) {

                val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("username", usernameInput.text.toString())
                    putString("password", passwordInput.text.toString())
                    apply()
                }

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val backToLoginText: TextView = findViewById(R.id.back_to_login_text)
        backToLoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun validateInput(): Boolean {
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()
        val phoneNumber = phoneNumberInput.text.toString()

        when {
            username.length < 3 || !username.matches(Regex(".*[a-zA-Z].*")) -> {
                showAlert("Username must contain at least 3 letters.")
                return false
            }
            !password.matches(Regex("^(?=.*[a-zA-Z])(?=.*[0-9]).{4,}$")) -> {
                showAlert("Password must contain both letters and numbers.")
                return false
            }
            password != confirmPassword -> {
                showAlert("Password and Confirm Password must match.")
                return false
            }
            phoneNumber.length < 2 -> {
                showAlert("Phone number must contain at least 2 digits.")
                return false
            }
            else -> return true
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
