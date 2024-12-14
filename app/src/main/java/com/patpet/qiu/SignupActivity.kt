package com.patpet.qiu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var signupButton: Button
    private lateinit var backToLoginText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        dbHelper = DatabaseHelper(this)

        // 假设布局文件中存在以下ID的控件
        usernameEditText = findViewById(R.id.usernameInput)
        passwordEditText = findViewById(R.id.passwordInput)
        phoneNumberEditText = findViewById(R.id.phoneNumberInput)
        ageEditText = findViewById(R.id.ageInput)
        addressEditText = findViewById(R.id.addressInput)
        genderSpinner = findViewById(R.id.genderInput)
        signupButton = findViewById(R.id.sign_up_button)
        backToLoginText = findViewById(R.id.back_to_login_text)

        setupGenderSpinner()


        backToLoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val ageStr = ageEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val gender = if (genderSpinner.selectedItemPosition > 0) genderSpinner.selectedItem.toString() else ""

            if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || ageStr.isEmpty() || address.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageStr.toIntOrNull() ?: 0
            if (age <= 0) {
                Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 角色默认是user
            val role = "user"

            val success = addUserToDatabase(username, password, phoneNumber, role, gender, age, address)
            if (success) {
                Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Signup failed, please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupGenderSpinner() {
        val genderArray = arrayOf("Gender", "Male", "Female")

        val genderAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            genderArray
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                } else {
                    (view as TextView).setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0 // 禁用第一个选项
            }
        }

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
        genderSpinner.setSelection(0) // 默认选择 "Gender"
    }

    private fun addUserToDatabase(username: String, password: String, phoneNumber: String, role: String, gender: String, age: Int, address: String): Boolean {
        return dbHelper.addUser(username, password, phoneNumber, role, gender, age, address)
    }
}
