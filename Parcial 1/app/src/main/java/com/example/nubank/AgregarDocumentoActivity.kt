package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class AgregarDocumentoActivity : AppCompatActivity() {

    private lateinit var documento: EditText
    private lateinit var btnContinuar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_document_key) // tu nuevo layout

        documento = findViewById(R.id.edit_documento)
        btnContinuar = findViewById(R.id.btn_agregar_llave_documento)

        // Deshabilitar el botón inicialmente
        btnContinuar.isEnabled = false

        documento.addTextChangedListener { text ->
            val doc = text.toString().trim()
            btnContinuar.isEnabled = doc.length in 6..15 && doc.all { it.isDigit() }
        }

        btnContinuar.setOnClickListener {
            val doc = documento.text.toString().trim()
            if (doc.length in 6..15 && doc.all { it.isDigit() }) {
                guardarLlaveDocumento(doc)
            } else {
                Toast.makeText(this, "Documento inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarLlaveDocumento(numero: String) {
        val sharedPrefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)

        val nuevaLlave = "DOCUMENTO:$numero"

        val llavesGuardadas = sharedPrefs.getStringSet("listaLlaves", mutableSetOf())!!.toMutableSet()
        llavesGuardadas.add(nuevaLlave)

        sharedPrefs.edit().putStringSet("listaLlaves", llavesGuardadas).apply()

        Toast.makeText(this, "Llave guardada exitosamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LlavesActivity::class.java))
        finish()
    }
}
