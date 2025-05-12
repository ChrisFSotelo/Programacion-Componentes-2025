package com.example.nubank
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmation_send)

        // Referencias a los TextView del layout
        val tvBanco = findViewById<TextView>(R.id.tvBanco)
        val tvNumeroCuenta = findViewById<TextView>(R.id.tvNumeroCuenta)
        val tvTipoCuenta = findViewById<TextView>(R.id.tvTipoCuenta)
        val tvTipoDocumento = findViewById<TextView>(R.id.tvTipoDocumento)
        val tvNumeroDocumento = findViewById<TextView>(R.id.tvNumeroDocumento)
        val tvMonto = findViewById<TextView>(R.id.tvMonto)
        val tvImpuesto = findViewById<TextView>(R.id.tvImpuesto)
        val tvCobroEnvio = findViewById<TextView>(R.id.tvCobroEnvio)

        // Obtener datos del Intent
        val intent = intent
        val banco = intent.getStringExtra("banco") ?: "No especificado"
        val tipoCuenta = intent.getStringExtra("tipoCuenta") ?: "No especificado"
        val numeroCuenta = intent.getStringExtra("numeroCuenta") ?: "No especificado"
        val tipoDocumento = intent.getStringExtra("tipoDocumento") ?: "No especificado"
        val numeroDocumento = intent.getStringExtra("numeroDocumento") ?: "No especificado"
        val monto = intent.getDoubleExtra("monto ", 0.0)
        tvMonto.text = "$ $monto"
        val cobroEnvio = intent.getStringExtra("cobroEnvio") ?: "0"



        // Asignar datos a las vistas
        tvBanco.text = banco
        tvNumeroCuenta.text = numeroCuenta
        tvTipoCuenta.text = tipoCuenta
        tvTipoDocumento.text = tipoDocumento
        tvNumeroDocumento.text = numeroDocumento
        tvMonto.text = "$ $monto"
        tvImpuesto.text = "$ 0"
        tvCobroEnvio.text = "$ $cobroEnvio"
    }
}

