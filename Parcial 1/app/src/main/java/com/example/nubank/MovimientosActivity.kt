package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MovimientosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accout_movements)

        // Referencias a los elementos interactivos
        val depositarBtn = findViewById<LinearLayout>(R.id.depositarBtn)
        val enviarBtn = findViewById<LinearLayout>(R.id.enviarBtn)
        val recargarBtn = findViewById<LinearLayout>(R.id.recargarBtn)
        val detallesCuentaBtn = findViewById<LinearLayout>(R.id.detallesCuentaBtn)

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
    }
}