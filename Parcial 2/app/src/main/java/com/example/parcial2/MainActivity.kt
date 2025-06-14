package com.example.parcial2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lanzar LoginActivity al iniciar
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        // Opcional: finalizar MainActivity para que no se pueda volver con "back"
        finish()
    }
}
