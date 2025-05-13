package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AgregarLlaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_key)

        val btnCelular = findViewById<LinearLayout>(R.id.opcion_celular)
        val btnCorreo = findViewById<LinearLayout>(R.id.opcion_correo)
        val btnDocumento = findViewById<LinearLayout>(R.id.opcion_documento)
        val btnPlaca = findViewById<LinearLayout>(R.id.opcion_placa)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnCelular.setOnClickListener {
            val intent = Intent(this, AgregarCelularActivity::class.java)
            startActivity(intent)
        }

        btnCorreo.setOnClickListener {
            val intent = Intent(this, AgregarCorreoActivity::class.java)
            startActivity(intent)
        }

        btnDocumento.setOnClickListener {
            val intent = Intent(this, AgregarDocumentoActivity::class.java)
            startActivity(intent)
        }
        btnPlaca.setOnClickListener {
            val intent = Intent(this, AgregarPlacaActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }
}

