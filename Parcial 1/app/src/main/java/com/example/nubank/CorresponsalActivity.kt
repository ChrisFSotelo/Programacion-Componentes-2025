package com.example.nubank

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nubank.enums.TipoLlave
import com.example.nubank.models.Llave

class CorresponsalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_point)
    }

    override fun onResume() {
        super.onResume()
        mostrarLlavePlaca()
    }

    private fun mostrarLlavePlaca() {
        val prefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)
        val setLlaves = prefs.getStringSet("listaLlaves", emptySet()) ?: emptySet()

        val listaLlaves = setLlaves.mapNotNull {
            val partes = it.split(":")
            if (partes.size == 2) {
                val tipoEnum = runCatching { TipoLlave.valueOf(partes[0]) }.getOrNull()
                tipoEnum?.let { tipo -> Llave(tipo, partes[1]) }
            } else null
        }

        val llavePlaca = listaLlaves.find { it.tipo == TipoLlave.PLACA }

        val textViewPlaca = findViewById<TextView>(R.id.placa_code)
        textViewPlaca.text = llavePlaca?.valor ?: "Sin placa"
    }

}
