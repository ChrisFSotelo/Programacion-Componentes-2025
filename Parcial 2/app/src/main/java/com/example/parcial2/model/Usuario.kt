package com.example.parcial2.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val clave: String,
    val idRol: Int,
    val idEstado: String
)


