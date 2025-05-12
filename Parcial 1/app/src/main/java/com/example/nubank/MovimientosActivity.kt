package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import android.widget.ImageButton

class MovimientosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accout_movements)

        // Referencias a los elementos interactivos
        val depositarBtn = findViewById<LinearLayout>(R.id.depositarBtn)
        val enviarBtn = findViewById<LinearLayout>(R.id.enviarBtn)
        val recargarBtn = findViewById<LinearLayout>(R.id.recargarBtn)
        val detallesCuentaBtn = findViewById<LinearLayout>(R.id.detallesCuentaBtn)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Listener para el botón Depositar
        depositarBtn.setOnClickListener {
            Toast.makeText(this, "Depositar clicado", Toast.LENGTH_SHORT).show()
            // Puedes iniciar una actividad aquí si lo deseas:
            // startActivity(Intent(this, DepositarActivity::class.java))
        }

        // Listener para el botón Enviar
        enviarBtn.setOnClickListener {
            Toast.makeText(this, "Enviar clicado", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, EnviarActivity::class.java))
        }

        // Listener para el botón Recargar
        recargarBtn.setOnClickListener {
            Toast.makeText(this, "Recargar clicado", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, RecargarActivity::class.java))
        }

        // Listener para Detalles de mi cuenta
        detallesCuentaBtn.setOnClickListener {
            val intent = Intent(this, DetallesCuentaActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView: RecyclerView = findViewById(R.id.MovimientosLista)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val datosEjemplo = listOf(
            MovimientosAdapter.Movimiento("Enviaste a Valentina", "12 feb · 19:19", "-$9.000,00", Color.RED, R.drawable.ic_send),
            MovimientosAdapter.Movimiento("Recibiste de Juan", "11 feb · 10:00", "+$15.000,00", Color.parseColor("#008000"), R.drawable.ic_receive),
            MovimientosAdapter.Movimiento("Recarga desde cuenta", "10 feb · 18:40", "+$20.000,00", Color.parseColor("#008000"), R.drawable.ic_topup)
        )


        val adapter = MovimientosAdapter(datosEjemplo)
        recyclerView.adapter = adapter

    }
}