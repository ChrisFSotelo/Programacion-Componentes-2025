package com.example.nubank

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var btnEnviarDineroLlave: Button
    private lateinit var btnDetallesCuenta: Button
    private lateinit var btnCuentaAhorros: ConstraintLayout
    private lateinit var btnCompartir: LinearLayout
    private lateinit var btnFraude: LinearLayout
    private lateinit var btnInvitarUsuario: ImageView
    private lateinit var btnVerMonto: ImageView
    private lateinit var textMonto: TextView
    private lateinit var btnLlaves: ImageView

    private var ayudaVisible = true
    private var valorRealMonto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        btnEnviarDineroLlave = findViewById(R.id.enviarDineroLlaves)
        btnDetallesCuenta = findViewById(R.id.btnDetallesCuenta)
        btnCuentaAhorros = findViewById(R.id.cuentaAhorros)
        btnInvitarUsuario = findViewById(R.id.btnInvitarUsuario)
        btnCompartir = findViewById(R.id.btnCompartir)
        btnFraude = findViewById(R.id.btnFraude)
        btnLlaves = findViewById(R.id.tusLlaves)


        btnVerMonto = findViewById(R.id.btnVerMonto)
        textMonto = findViewById(R.id.textMonto)

        // Guarda el texto original del monto
        valorRealMonto = textMonto.text.toString()

        btnEnviarDineroLlave.setOnClickListener {
            val intent = Intent(this, EnviarDineroActivity::class.java)
            startActivity(intent)
        }

        btnDetallesCuenta.setOnClickListener {
            val intent = Intent(this, DetallesCuentaActivity::class.java)
            startActivity(intent)
        }

        btnCuentaAhorros.setOnClickListener {
            val intent = Intent(this, MovimientosActivity::class.java)
            startActivity(intent)
        }

        btnLlaves.setOnClickListener {
            val intent = Intent(this, LlavesActivity::class.java)
            startActivity(intent)
        }
        btnInvitarUsuario.setOnClickListener {
            val intent = Intent(this, InviteScreenActivity::class.java)
            startActivity(intent)
        }
        btnCompartir.setOnClickListener {
            val intent = Intent(this, InviteScreenActivity::class.java)
            startActivity(intent)
        }
        btnFraude.setOnClickListener {
            val url = "https://blog.nu.com.co/ayuda/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
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

