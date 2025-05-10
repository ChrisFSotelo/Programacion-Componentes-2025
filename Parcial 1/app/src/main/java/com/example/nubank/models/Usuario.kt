package com.example.nubank.models

data class Usuario(
    val firstName: String,
    val lastName: String,
    val idNumber: String,
    val email: String,
    val phone: String,
    val password: String
)