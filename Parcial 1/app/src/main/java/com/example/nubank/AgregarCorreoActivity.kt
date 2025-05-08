package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class AgregarCorreoActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var btnContinuar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_email_key)

        email = findViewById(R.id.email)
        btnContinuar = findViewById(R.id.btn_agregar_llave_email)

        // Deshabilitar el botón inicialmente
        btnContinuar.isEnabled = false

        email.addTextChangedListener { text ->
            val correo = text.toString().trim()
            // Habilitar el botón si el email tiene un formato válido
            btnContinuar.isEnabled = android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
        }

        btnContinuar.setOnClickListener {
            val correo = email.text.toString().trim()
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                guardarLlaveEmail(correo)
            } else {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarLlaveEmail(correo: String) {
        val sharedPrefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)

        // Convertir la nueva llave a formato de texto plano
        val nuevaLlave = "EMAIL:$correo"

        // Obtener el set actual, o uno nuevo si no existe
        val llavesGuardadas = sharedPrefs.getStringSet("listaLlaves", mutableSetOf())!!.toMutableSet()

        // Agregar la nueva llave
        llavesGuardadas.add(nuevaLlave)

        // Guardar de nuevo el set
        sharedPrefs.edit().putStringSet("listaLlaves", llavesGuardadas).apply()

        Toast.makeText(this, "Llave guardada exitosamente", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LlavesActivity::class.java)
        startActivity(intent)

        finish()
    }
}
