package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserBankActivity : AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.titular_bank)
        val spinnerTipoDocumento = findViewById<Spinner>(R.id.spinnerTipoDocumento)
        val montoRecibido = intent.getDoubleExtra("monto", 0.0)
        val tvMonto = findViewById<TextView>(R.id.tvMontoEnvio)
        tvMonto.text = "$$montoRecibido"

        val tiposDocumento = listOf("Cédula ciudadana", "Extranjero")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDocumento)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = adapter
        val btnSiguiente = findViewById<ImageButton>(R.id.btnSiguiente)

        val banco = intent.getStringExtra("banco")
        val tipoCuenta = intent.getStringExtra("tipoCuenta")
        val numeroCuenta = intent.getStringExtra("numeroCuenta")
        val monto = intent.getStringExtra("monto")

        btnSiguiente.setOnClickListener {
            val nombreTitular = findViewById<EditText>(R.id.etNombreTitular).text.toString()
            val tipoDocumento = spinnerTipoDocumento.selectedItem.toString()
            val numeroDocumento = findViewById<EditText>(R.id.etNumeroDocumento).text.toString()
            val motivo = findViewById<EditText>(R.id.etMotivo).text.toString()

            // Aquí puedes hacer validaciones o pasar los datos a otra Activity
            Log.d("FormularioTitular", "Nombre: $nombreTitular")
            Log.d("FormularioTitular", "Tipo Documento: $tipoDocumento")
            Log.d("FormularioTitular", "Número Documento: $numeroDocumento")
            Log.d("FormularioTitular", "Motivo: $motivo")

             val intent = Intent(this, ConfirmacionActivity::class.java)
            intent.putExtra("nombre", nombreTitular)
            intent.putExtra("tipoDocumento", tipoDocumento)
            intent.putExtra("numeroDocumento", numeroDocumento)

            // Datos del banco (reenviados)
            intent.putExtra("banco", banco)
            intent.putExtra("tipoCuenta", tipoCuenta)
            intent.putExtra("numeroCuenta", numeroCuenta)
            intent.putExtra("monto_envio", montoRecibido)
             startActivity(intent)
        }

    }

}
