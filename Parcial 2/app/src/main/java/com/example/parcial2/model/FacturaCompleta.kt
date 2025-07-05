package com.example.parcial2.model

data class FacturaCompleta(
    val id: Int,
    val fecha: String,
    val hora: String,
    val subtotal: Int,
    val iva: Float,
    val total: Int,
    val idCliente: Int,
    val ciudad: String,
    val direccion: String,
    val estado: Int,
    val cliente: String,
    val productos: List<String>,
    val cantidades: List<Int>,
    val preciosUnitarios: List<Int>,
    val precioVentas: List<Int>
)

