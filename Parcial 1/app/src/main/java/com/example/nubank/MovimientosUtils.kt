package com.example.nubank

import android.content.Context
import com.example.nubank.enums.TipoMovimiento
import com.example.nubank.models.Movimiento
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun obtenerSaldoActual(context: Context): Double {
    val sharedPref = context.getSharedPreferences("MovimientosPrefs", Context.MODE_PRIVATE)
    val gson = Gson()

    val movimientosJson = sharedPref.getString("listaMovimientos", "[]")
    val tipoLista = object : TypeToken<MutableList<Movimiento>>() {}.type
    val movimientos = gson.fromJson<MutableList<Movimiento>>(movimientosJson, tipoLista)

    return movimientos.fold(0.0) { acc, movimiento ->
        when (movimiento.tipo) {
            TipoMovimiento.DEPOSITO -> acc + movimiento.monto
            TipoMovimiento.ENVIO -> acc - movimiento.monto
        }
    }
}