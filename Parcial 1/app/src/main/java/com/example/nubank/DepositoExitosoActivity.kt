package com.example.nubank

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.example.nubank.enums.TipoMovimiento
import com.example.nubank.models.Movimiento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class DepositoExitosoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deposit_sucessfull)


        val tvNombre = findViewById<TextView>(R.id.tvEnvia)
        val tvDireccion = findViewById<TextView>(R.id.tvDireccion)
        val tvMonto = findViewById<TextView>(R.id.tvMonto)
        val tvPlaca = findViewById<TextView>(R.id.tvPlaca)
        val tvComprobante = findViewById<TextView>(R.id.tvComprobante)
        val btnSalir = findViewById<Button>(R.id.btnSalir)
        val tvRecibe = findViewById<TextView>(R.id.tvRecibe)
        val btnCompartir = findViewById<Button>(R.id.btnCompartir)



        // Obtener datos del intent
        val nombre = intent.getStringExtra("nombre") ?: "Desconocido"
        val direccion = intent.getStringExtra("direccion") ?: "No disponible"
        val monto = intent.getStringExtra("monto") ?: "0.0"
        val placa = intent.getStringExtra("placa") ?: "Sin placa"


        val codigoTransaccion = generarCodigoTransaccion()
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val firstName = sharedPref.getString("firstName", "Nombre") ?: "Nombre"
        val lastName = sharedPref.getString("lastName", "Apellido") ?: "Apellido"
        val nombreCompleto = "$firstName $lastName"

        tvNombre.text = "$nombre"
        tvDireccion.text = "$direccion"
        tvRecibe.text = "$nombreCompleto"
        tvMonto.text = "$monto"
        tvPlaca.text = "$placa"
        tvComprobante.text = "$codigoTransaccion"


        btnSalir.setOnClickListener {

            val sharedPref = getSharedPreferences("MovimientosPrefs", Context.MODE_PRIVATE)
            val gson = Gson()

            // Leer la lista existente (o inicializar si no existe)
            val movimientosJson = sharedPref.getString("listaMovimientos", null)
            val tipoLista = object : TypeToken<MutableList<Movimiento>>() {}.type
            val listaMovimientos: MutableList<Movimiento> =
                if (movimientosJson != null)
                    gson.fromJson(movimientosJson, tipoLista)
                else
                    mutableListOf()

            // Crear el nuevo movimiento
            val nuevoMovimiento = Movimiento(
                nombreCorresponsal = nombre,
                direccionCorresponsal = direccion,
                nombreUsuario = nombreCompleto,
                monto = monto.toDouble(),
                placa = placa,
                comprobante = codigoTransaccion,
                tipo = TipoMovimiento.DEPOSITO,
                fecha = System.currentTimeMillis()
            )

            // Agregar y guardar
            listaMovimientos.add(nuevoMovimiento)
            val editor = sharedPref.edit()
            editor.putString("listaMovimientos", gson.toJson(listaMovimientos))
            editor.apply()

            // Volver al inicio
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        btnCompartir.setOnClickListener {
            val layoutComprobante = findViewById<ConstraintLayout>(R.id.layoutComprobante)

            // 1. Crear el bitmap desde el layout
            val bitmap = Bitmap.createBitmap(
                layoutComprobante.width,
                layoutComprobante.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            layoutComprobante.draw(canvas)

            // 2. Guardar la imagen en un archivo temporal
            val file = File(cacheDir, "comprobante.png")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            // 3. Obtener el URI con FileProvider
            val uri: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",  // asegúrate que coincide con tu manifest
                file
            )

            // 4. Compartir el archivo
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Compartir comprobante"))
        }


    }
    private fun generarCodigoTransaccion(): String {
            val uuid = UUID.randomUUID().toString().substring(0, 8).uppercase()
            return "DEP-$uuid"
    }

}

