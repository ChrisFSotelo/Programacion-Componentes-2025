package com.example.nubank
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nubank.enums.TipoMovimiento
import com.example.nubank.models.Movimiento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ConfirmacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmation_send)

        val btnEnviar = findViewById<Button>(R.id.btnEnviar)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        // Referencias a los TextView del layout
        val tvBanco = findViewById<TextView>(R.id.tvBanco)
        val tvNumeroCuenta = findViewById<TextView>(R.id.tvNumeroCuenta)
        val tvTipoCuenta = findViewById<TextView>(R.id.tvTipoCuenta)
        val tvTipoDocumento = findViewById<TextView>(R.id.tvTipoDocumento)
        val tvNumeroDocumento = findViewById<TextView>(R.id.tvNumeroDocumento)
        val tvMonto = findViewById<TextView>(R.id.tvMontoEnvio)
        val tvImpuesto = findViewById<TextView>(R.id.tvImpuesto)
        val tvCobroEnvio = findViewById<TextView>(R.id.tvCobroEnvio)
        val tvNombreTitular = findViewById<TextView>(R.id.tvNombreTitular)

        // Obtener datos del Intent
        val intent = intent
        val banco = intent.getStringExtra("banco") ?: "No especificado"
        val tipoCuenta = intent.getStringExtra("tipoCuenta") ?: "No especificado"
        val numeroCuenta = intent.getStringExtra("numeroCuenta") ?: "No especificado"
        val tipoDocumento = intent.getStringExtra("tipoDocumento") ?: "No especificado"
        val numeroDocumento = intent.getStringExtra("numeroDocumento") ?: "No especificado"
        val monto = intent.getDoubleExtra("monto_envio", 0.0)
        val cobroEnvio = intent.getStringExtra("cobroEnvio") ?: "0"
        val nombreCompleto = intent.getStringExtra("nombre") ?: "Nombre Apellido"

        // Asignar datos a las vistas
        tvBanco.text = banco
        tvNumeroCuenta.text = numeroCuenta
        tvTipoCuenta.text = tipoCuenta
        tvTipoDocumento.text = tipoDocumento
        tvNumeroDocumento.text = numeroDocumento
        tvMonto.text = "$ $monto"
        tvImpuesto.text = "$ 0"
        tvCobroEnvio.text = "$ $cobroEnvio"
        tvNombreTitular.text = nombreCompleto

        btnEnviar.setOnClickListener {
            val saldoActual = obtenerSaldoActual(this)

            if (monto > saldoActual) {
                Toast.makeText(this, "Saldo insuficiente para realizar el envío", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("MovimientosPrefs", Context.MODE_PRIVATE)
            val gson = Gson()

            // Leer la lista existente de movimientos
            val movimientosJson = sharedPref.getString("listaMovimientos", null)
            val tipoLista = object : TypeToken<MutableList<Movimiento>>() {}.type
            val listaMovimientos: MutableList<Movimiento> =
                if (movimientosJson != null)
                    gson.fromJson(movimientosJson, tipoLista)
                else
                    mutableListOf()

            // Crear el nuevo movimiento (tipo ENVIO)
            val nuevoMovimiento = Movimiento(
                nombreCorresponsal = banco,
                direccionCorresponsal = tipoCuenta,
                nombreUsuario = nombreCompleto,
                monto = monto,
                placa = numeroCuenta,
                comprobante = numeroDocumento,
                tipo = TipoMovimiento.ENVIO,
                fecha = System.currentTimeMillis()
            )

            // Agregar y guardar en SharedPreferences
            listaMovimientos.add(nuevoMovimiento)
            val editor = sharedPref.edit()
            editor.putString("listaMovimientos", gson.toJson(listaMovimientos))
            editor.apply()

            Toast.makeText(this, "Envío registrado con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

    }
}


