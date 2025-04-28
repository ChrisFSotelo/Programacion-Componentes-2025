package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PasswordActivity : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var ivTogglePassword: ImageView
    private lateinit var btnContinue: ImageButton
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_page)

        etPassword = findViewById(R.id.etPassword)
        ivTogglePassword = findViewById(R.id.ivTogglePassword)
        btnContinue = findViewById(R.id.btnContinue)

        val inputPhoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""

        ivTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        btnContinue.setOnClickListener {
            val inputPassword = etPassword.text.toString()

            // Validación básica de contraseña
            if (inputPassword.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Leer los datos almacenados en SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val savedPhone = sharedPreferences.getString("phone", "")
            val savedPassword = sharedPreferences.getString("password", "")

            if (inputPhoneNumber == savedPhone && inputPassword == savedPassword) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Toast.makeText(this, "Número o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            ivTogglePassword.setImageResource(R.drawable.ic_eye_closed)
        } else {
            etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            ivTogglePassword.setImageResource(R.drawable.ic_eye_open)
        }
        etPassword.setSelection(etPassword.text.length)
        isPasswordVisible = !isPasswordVisible
    }
}


