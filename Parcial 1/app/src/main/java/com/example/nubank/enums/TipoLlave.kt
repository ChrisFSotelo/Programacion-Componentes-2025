package com.example.nubank.enums

enum class TipoLlave(val displayName: String)  {
    CELULAR("Celular"),
    EMAIL("Correo electronico"),
    DOCUMENTO("Documento de identidad"),
    PLACA("Llave especial Nu");

    override fun toString(): String {
        return displayName
    }
}