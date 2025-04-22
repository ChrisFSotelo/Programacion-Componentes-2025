package com.example.nubank

import LoginAction
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrarse = findViewById<Button>(R.id.btnGoogle)

        btnIngresar.setOnClickListener {
            handleLoginAction(LoginAction.INGRESAR, phoneInput.text.toString())
        }

        btnRegistrarse.setOnClickListener {
            handleLoginAction(LoginAction.REGISTRARSE, phoneInput.text.toString())
        }
    }

    private fun handleLoginAction(action: LoginAction, phoneNumber: String) {
        when (action) {
            LoginAction.INGRESAR -> {
                if (phoneNumber.isEmpty()) {
                    Toast.makeText(this, "Número vacío, redirigiendo a registro...", Toast.LENGTH_SHORT).show()
                    // Simular que el usuario eligió registrarse
                    handleLoginAction(LoginAction.REGISTRARSE, phoneNumber)
                    return
                }
                val intent = Intent(this, PasswordActivity::class.java)
                intent.putExtra("PHONE_NUMBER", phoneNumber)
                startActivity(intent)
            }

            LoginAction.REGISTRARSE -> {
                Toast.makeText(this, "Redirigiendo a registro...", Toast.LENGTH_SHORT).show()
                // Aquí podrías cargar el layout de registro o ir a otra activity
                // setContentView(R.layout.register_page)
                // o
                // startActivity(Intent(this, RegistroActivity::class.java))
            }
        }
    }

}
