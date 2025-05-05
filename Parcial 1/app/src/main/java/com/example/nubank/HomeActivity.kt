package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var btnDetallesCuenta: Button
    private lateinit var btnCuentaAhorros: ConstraintLayout
    private lateinit var btnInvitarUsuario: ImageView
    private lateinit var btnVerMonto: ImageView
    private lateinit var textMonto: TextView

    private var ayudaVisible = true
    private var valorRealMonto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        btnDetallesCuenta = findViewById(R.id.btnDetallesCuenta)
        btnCuentaAhorros = findViewById(R.id.cuentaAhorros)
        btnInvitarUsuario = findViewById(R.id.btnInvitarUsuario)

        btnVerMonto = findViewById(R.id.btnVerMonto)
        textMonto = findViewById(R.id.textMonto)

        // Guarda el texto original del monto
        valorRealMonto = textMonto.text.toString()

        btnDetallesCuenta.setOnClickListener {
            val intent = Intent(this, DetallesCuentaActivity::class.java)
            startActivity(intent)
        }

        btnCuentaAhorros.setOnClickListener {
            val intent = Intent(this, MovimientosActivity::class.java)
            startActivity(intent)
        }

        btnInvitarUsuario.setOnClickListener {
            val intent = Intent(this, InviteScreenActivity::class.java)
            startActivity(intent)
        }

        btnVerMonto.setOnClickListener {
            ayudaVisible = !ayudaVisible

            if (ayudaVisible) {
                btnVerMonto.setImageResource(R.drawable.ic_eye_open)
                textMonto.text = valorRealMonto
            } else {
                btnVerMonto.setImageResource(R.drawable.ic_eye_closed)
                textMonto.text = "$ ••••••"
            }
        }
    }
}

