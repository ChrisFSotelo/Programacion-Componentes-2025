package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AgregarLlaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_key)

        val btnCelular = findViewById<LinearLayout>(R.id.opcion_celular)
        val btnCorreo = findViewById<LinearLayout>(R.id.opcion_correo)

        btnCelular.setOnClickListener {
            val intent = Intent(this, AgregarCelularActivity::class.java)
            startActivity(intent)
        }

        btnCorreo.setOnClickListener {
            val intent = Intent(this, AgregarCorreoActivity::class.java)
            startActivity(intent)
        }

        // Puedes agregar más botones y listeners según el tipo de llave
    }
}

