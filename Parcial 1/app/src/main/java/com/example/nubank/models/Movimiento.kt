package com.example.nubank.models

import com.example.nubank.enums.TipoMovimiento

data class Movimiento(
    val nombreCorresponsal: String,
    val direccionCorresponsal: String,
    val nombreUsuario: String,
    val monto: Double,
    val placa: String,
    val comprobante: String,
    val tipo: TipoMovimiento, // DEPOSITO o ENVIO
    val fecha: Long // timestamp
)