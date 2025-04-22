package com.example.nubank

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

        val phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""

        // Toggle para mostrar/ocultar contraseña
        ivTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        // Continuar
        btnContinue.setOnClickListener {
            val password = etPassword.text.toString()
            val phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""

            //DEBUG PARA EL NUMERO TELEFONICO RECIBIDO
//            Log.d("DEBUG", "Teléfono recibido: $phoneNumber")
//            Log.d("DEBUG", "Contraseña ingresada: $password")

            // Validación básica de contraseña
            if (password.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación contra los datos estáticos
            if (phoneNumber != STATIC_PHONE || password != STATIC_PASSWORD) {
                Toast.makeText(this, "Número o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si todo es correcto
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

            // Aquí puedes redirigir al usuario
            // startActivity(Intent(this, HomeActivity::class.java))
        }


        // Botón de retroceso
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish() // Cierra esta actividad y regresa
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
        etPassword.setSelection(etPassword.text.length) // Mantener el cursor al final
        isPasswordVisible = !isPasswordVisible
    }

    companion object {
        const val STATIC_PHONE = "3246194520"
        const val STATIC_PASSWORD = "udistrital"
    }

}


