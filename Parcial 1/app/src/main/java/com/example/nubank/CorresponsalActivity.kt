package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nubank.enums.TipoLlave
import com.example.nubank.models.Llave

class CorresponsalActivity : AppCompatActivity() {

    private var placa: String = "Sin placa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_point)
    }

    fun onPuntoClick(view: View) {
        val etMonto = findViewById<EditText>(R.id.etMontoDeposito)
        val monto = etMonto.text.toString()

        var nombre = ""
        var direccion = ""
        var distancia = ""

        when (view.id) {
            R.id.punto1 -> {
                nombre = findViewById<TextView>(R.id.corresponsal1).text.toString()
                direccion = findViewById<TextView>(R.id.direccion1).text.toString()
            }
            R.id.punto2 -> {
                nombre = findViewById<TextView>(R.id.corresponsal2).text.toString()
                direccion = findViewById<TextView>(R.id.direccion2).text.toString()
            }
            R.id.punto3 -> {
                nombre = findViewById<TextView>(R.id.corresponsal3).text.toString()
                direccion = findViewById<TextView>(R.id.direccion3).text.toString()
            }
        }

        val intent = Intent(this, ReciboDepositoActivity::class.java).apply {
            putExtra("monto", monto)
            putExtra("nombre", nombre)
            putExtra("direccion", direccion)
            putExtra("placa", placa)
        }
        startActivity(intent)
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
        placa = llavePlaca?.valor ?: "Sin placa"
        val textViewPlaca = findViewById<TextView>(R.id.placa_code)
        textViewPlaca.text = placa
    }
}

