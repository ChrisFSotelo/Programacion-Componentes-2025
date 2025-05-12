package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DataBankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.data_bank)
        val spinnerBancos = findViewById<Spinner>(R.id.spinnerBancos)
        val spinnerTipoCuenta = findViewById<Spinner>(R.id.spinnerTipoCuenta)
        val btnContinuar = findViewById<ImageButton>(R.id.btnContinuar)
        val montoRecibido = intent.getDoubleExtra("monto_envio", 0.0)

        val tvMonto = findViewById<TextView>(R.id.tvMontoEnvio)
        tvMonto.text = "$$montoRecibido"

        val bancos = listOf("Bancolombia", "Nequi", "Davivienda", "BBVA", "Banco de Bogotá")
        val tiposCuenta = listOf("Cuenta de ahorros", "Cuenta corriente")

        val bancoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bancos)
        bancoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBancos.adapter = bancoAdapter

        val tipoCuentaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposCuenta)
        tipoCuentaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoCuenta.adapter = tipoCuentaAdapter



        btnContinuar.setOnClickListener {
            val bancoSeleccionado = spinnerBancos.selectedItem.toString()
            val tipoCuentaSeleccionada = spinnerTipoCuenta.selectedItem.toString()
            val numeroCuenta = findViewById<EditText>(R.id.etNumeroCuenta).text.toString()

            // Validaciones básicas
            if (numeroCuenta.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa el número de cuenta", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Mostrar los datos por consola (luego puedes enviarlos a otra Activity o a tu backend)
//            Log.d("DataBank", "Banco: $bancoSeleccionado")
//            Log.d("DataBank", "Tipo de cuenta: $tipoCuentaSeleccionada")
//            Log.d("DataBank", "Número: $numeroCuenta")
//            Log.d("DataBank", "Monto recibido: $montoRecibido")

            val intent = Intent(this, UserBankActivity::class.java)
            intent.putExtra("banco", bancoSeleccionado)
            intent.putExtra("tipoCuenta", tipoCuentaSeleccionada)
            intent.putExtra("numeroCuenta", numeroCuenta)
            intent.putExtra("monto", montoRecibido)
            startActivity(intent)
        }


    }

}
