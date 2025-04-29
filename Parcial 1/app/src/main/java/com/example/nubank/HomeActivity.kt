package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var btnDetallesCuenta: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_page)
        btnDetallesCuenta = findViewById(R.id.btnDetallesCuenta)

        btnDetallesCuenta.setOnClickListener {
            val intent = Intent(this, DetallesCuentaActivity::class.java)
            startActivity(intent)
        }
    }
}
