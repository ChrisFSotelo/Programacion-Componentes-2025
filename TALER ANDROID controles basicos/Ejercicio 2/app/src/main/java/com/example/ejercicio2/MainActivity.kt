package com.example.ejercicio2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular elementos del XML
        val montoActual = findViewById<EditText>(R.id.montoActual)
        val monedaActual = findViewById<Spinner>(R.id.monedaActual)
        val cambioMoneda = findViewById<Spinner>(R.id.cambioMoneda)
        val button = findViewById<Button>(R.id.buton_convert)
        val textResponse = findViewById<TextView>(R.id.textResponse)

        // Opciones de moneda
        val monedas = arrayOf("USD", "EUR", "COP", "MXN")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monedas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monedaActual.adapter = adapter
        cambioMoneda.adapter = adapter

        // Tasas de cambio (Ejemplo: 1 USD = X otra moneda)
        val tasasDeCambio = mapOf(
            "USD" to mapOf("EUR" to 0.92, "COP" to 4196.42, "MXN" to 20.48),
            "EUR" to mapOf("USD" to 1.08, "COP" to 4543.20, "MXN" to 22.17),
            "COP" to mapOf("USD" to 0.00024, "EUR" to 0.00022, "MXN" to 0.0049),
            "MXN" to mapOf("USD" to 0.049, "EUR" to 0.045, "COP" to 204.99)
        )

        // Acción del botón
        button.setOnClickListener {
            val montoStr = montoActual.text.toString()
            if (montoStr.isEmpty()) {
                textResponse.text = "Ingrese un monto válido."
                return@setOnClickListener
            }

            val monto = montoStr.toDouble()
            val monedaOrigen = monedaActual.selectedItem.toString()
            val monedaDestino = cambioMoneda.selectedItem.toString()

            if (monedaOrigen == monedaDestino) {
                textResponse.text = "Seleccione monedas diferentes."
                return@setOnClickListener
            }

            val tasa = tasasDeCambio[monedaOrigen]?.get(monedaDestino)

            if (tasa != null) {
                val montoConvertido = monto * tasa
                textResponse.text = "$monto $monedaOrigen equivale a %.2f $monedaDestino".format(montoConvertido)
            } else {
                textResponse.text = "No hay tasa de cambio disponible."
            }
        }
    }
}
