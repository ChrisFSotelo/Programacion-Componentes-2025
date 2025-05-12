package com.example.nubank

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nubank.enums.TipoLlave
import com.example.nubank.models.Llave


class DepositarActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deposit_money)

        val btnDepositarEfectivo = findViewById<LinearLayout>(R.id.btnEnEfectivo)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnDepositarEfectivo.setOnClickListener {
            val intent = Intent(this, CorresponsalActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        mostrarLlaveDocumento()
    }

    private fun mostrarLlaveDocumento() {
        val prefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)
        val setLlaves = prefs.getStringSet("listaLlaves", emptySet()) ?: emptySet()

        val listaLlaves = setLlaves.mapNotNull {
            val partes = it.split(":")
            if (partes.size == 2) {
                val tipoEnum = runCatching { TipoLlave.valueOf(partes[0]) }.getOrNull()
                tipoEnum?.let { tipo -> Llave(tipo, partes[1]) }
            } else null
        }

        val llaveDocumento = listaLlaves.find { it.tipo == TipoLlave.DOCUMENTO }
        val valorDocumento = llaveDocumento?.valor ?: "Sin documento"

        val txtValorDocumento = findViewById<TextView>(R.id.valor_llave_documento)
        val btnCopiar = findViewById<ImageView>(R.id.btn_copiar_documento)

        txtValorDocumento.text = valorDocumento

        btnCopiar.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Llave Documento", valorDocumento)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Documento copiado al portapapeles", Toast.LENGTH_SHORT).show()
        }
    }


}
