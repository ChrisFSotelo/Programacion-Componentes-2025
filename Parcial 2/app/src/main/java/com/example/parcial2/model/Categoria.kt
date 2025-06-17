package com.example.parcial2.model

data class Categoria(val id: Int, val nombre: String) {
    override fun toString(): String {
        return nombre // Para que el Spinner muestre el nombre
    }
}

