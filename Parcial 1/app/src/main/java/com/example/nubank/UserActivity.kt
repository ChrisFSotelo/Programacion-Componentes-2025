package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var nextButton: ImageButton
    private lateinit var passwordEditText: EditText // nuevo campo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_user) // Asegúrate que este nombre coincida con tu XML

        // Referenciar los componentes
        firstNameEditText = findViewById(R.id.first_name)
        lastNameEditText = findViewById(R.id.last_name)
        idNumberEditText = findViewById(R.id.id_number)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phone)
        nextButton = findViewById(R.id.next_button)
        passwordEditText = findViewById(R.id.password)

        // Botón de acción
        nextButton.setOnClickListener {
            crearUsuario()
        }

    }
    private fun crearUsuario() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val idNumber = idNumberEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validar campos
        if (firstName.isEmpty() || lastName.isEmpty() || idNumber.isEmpty() ||
            email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar en SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("firstName", firstName)
        editor.putString("lastName", lastName)
        editor.putString("idNumber", idNumber)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("password", password) // ahora guarda lo que el usuario escribe
        editor.apply()

        Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, MainActivity::class.java))
    }

}
