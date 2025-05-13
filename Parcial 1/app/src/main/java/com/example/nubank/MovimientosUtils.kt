package com.example.nubank

import android.content.Context
import android.util.Log
import com.example.nubank.enums.TipoMovimiento
import com.example.nubank.models.Movimiento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun obtenerSaldoActual(context: Context): Double {
    val sharedPref = context.getSharedPreferences("MovimientosPrefs", Context.MODE_PRIVATE)
    val gson = Gson()

    val movimientosJson = sharedPref.getString("listaMovimientos", "[]")
    Log.d("DEBUG", "JSON movimientos: $movimientosJson") // Diagnóstico

    // Paso 1: deserializar a una clase intermedia
    data class MovimientoRaw(
        val nombreCorresponsal: String,
        val direccionCorresponsal: String,
        val nombreUsuario: String,
        val monto: Double,
        val placa: String,
        val comprobante: String,
        val tipo: String,
        val fecha: Long
    )

    val tipoListaRaw = object : TypeToken<MutableList<MovimientoRaw>>() {}.type
    val movimientosRaw = gson.fromJson<MutableList<MovimientoRaw>>(movimientosJson, tipoListaRaw)

    // Paso 2: convertir a la clase Movimiento real con enum
    val movimientos = movimientosRaw.mapNotNull {
        try {
            Movimiento(
                nombreCorresponsal = it.nombreCorresponsal,
                direccionCorresponsal = it.direccionCorresponsal,
                nombreUsuario = it.nombreUsuario,
                monto = it.monto,
                placa = it.placa,
                comprobante = it.comprobante,
                tipo = TipoMovimiento.valueOf(it.tipo), // conversión segura a enum
                fecha = it.fecha
            )
        } catch (e: Exception) {
            Log.e("ERROR", "TipoMovimiento inválido: ${it.tipo}", e)
            null
        }
    }

    return movimientos.fold(0.0) { acc, movimiento ->
        when (movimiento.tipo) {
            TipoMovimiento.DEPOSITO -> acc + movimiento.monto
            TipoMovimiento.ENVIO -> acc - movimiento.monto
        }
    }
}
