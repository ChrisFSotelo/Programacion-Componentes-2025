package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReciboDepositoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transac_deposit)

        // Obtener referencias de los TextViews
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvDireccionValor = findViewById<TextView>(R.id.tvDireccionValor)
        val tvLimiteValor = findViewById<TextView>(R.id.etMontoDeposito)
        val depositarBtn = findViewById<LinearLayout>(R.id.depositarBtn)
        val noDepositarBtn = findViewById<LinearLayout>(R.id.noDepositarBtn)

        // Obtener datos del Intent
        val nombre = intent.getStringExtra("nombre")
        val direccion = intent.getStringExtra("direccion")
        val monto = intent.getStringExtra("monto") ?: "0.0"
        val placa = intent.getStringExtra("placa")

        // Asignar valores a los TextViews
        tvNombre.text = nombre ?: "Nombre no disponible"
        tvDireccionValor.text = direccion ?: "Dirección no disponible"
        tvLimiteValor.text = "Monto: $monto"

        depositarBtn.setOnClickListener {
            val intent = Intent(this, DepositoExitosoActivity::class.java).apply {
                putExtra("monto", monto)
                putExtra("nombre", nombre)
                putExtra("direccion", direccion)
                putExtra("placa", placa)
            }
            startActivity(intent)
            finish()
        }
        noDepositarBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}

