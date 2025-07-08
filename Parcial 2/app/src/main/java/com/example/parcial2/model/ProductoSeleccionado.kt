package com.example.parcial2.model

data class ProductoSeleccionado(
    val id: Int,
    val nombre: String,
    val precio: Int,
    var cantidad: Int = 0,
    var seleccionado: Boolean = false
)
