package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import android.widget.ImageButton
import android.widget.TextView
import com.example.nubank.enums.TipoMovimiento
import com.example.nubank.models.Movimiento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovimientosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accout_movements)

        val depositarBtn = findViewById<LinearLayout>(R.id.depositarBtn)
        val enviarBtn = findViewById<LinearLayout>(R.id.enviarBtn)
        val recargarBtn = findViewById<LinearLayout>(R.id.recargarBtn)
        val detallesCuentaBtn = findViewById<LinearLayout>(R.id.detallesCuentaBtn)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        val saldo = intent.getDoubleExtra("saldo_actual", 0.0)
        val textViewSaldo = findViewById<TextView>(R.id.tvSaldoDisponible)
        textViewSaldo.text = "$ %.2f".format(saldo)

        depositarBtn.setOnClickListener {
            startActivity(Intent(this, DepositarActivity::class.java))
        }

        enviarBtn.setOnClickListener {
            val intent = Intent(this, MethodSendActivity::class.java)
            startActivity(intent)
        }

        detallesCuentaBtn.setOnClickListener {
            val intent = Intent(this, DetallesCuentaActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 🔄 Cargar movimientos reales
        val sharedPref = getSharedPreferences("MovimientosPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val movimientosJson = sharedPref.getString("listaMovimientos", "[]")
        val tipoLista = object : TypeToken<MutableList<Movimiento>>() {}.type
        val movimientos: MutableList<Movimiento> = gson.fromJson(movimientosJson, tipoLista)

        // 🔁 Adaptar a la UI
        val movimientosUI = movimientos.map {
            val descripcion = if (it.tipo == TipoMovimiento.DEPOSITO) "Recibiste de ${it.nombreCorresponsal}" else "Enviaste a ${it.nombreCorresponsal}"
            val montoTexto = if (it.tipo == TipoMovimiento.DEPOSITO) "+$${it.monto}" else "-$${it.monto}"
            val color = if (it.tipo == TipoMovimiento.DEPOSITO) Color.parseColor("#008000") else Color.RED
            val icono = if (it.tipo == TipoMovimiento.DEPOSITO) R.drawable.ic_receive else R.drawable.ic_send
            MovimientosAdapter.Movimiento(descripcion, convertirFecha(it.fecha), montoTexto, color, icono)
        }

        val recyclerView: RecyclerView = findViewById(R.id.MovimientosLista)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MovimientosAdapter(movimientosUI)
    }

    private fun convertirFecha(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM · HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
