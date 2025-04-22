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
        val phoneNumber = intent.getStringExtra("PHONE")
//DEBUG PARA EL NUMERO TELEFONICO RECIBIDO
//        if (phoneNumber != null) {
//            Log.d("PasswordActivity", "Número recibido: $phoneNumber")
//        } else {
//            Log.e("PasswordActivity", "No se recibió el número")
//        }

        // Toggle para mostrar/ocultar contraseña
        ivTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        // Continuar
        btnContinue.setOnClickListener {
            val password = etPassword.text.toString()
            if (password.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
            } else {
                // Aquí puedes continuar a la siguiente pantalla o guardar la contraseña
                Toast.makeText(this, "Contraseña válida", Toast.LENGTH_SHORT).show()
            }
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
}
