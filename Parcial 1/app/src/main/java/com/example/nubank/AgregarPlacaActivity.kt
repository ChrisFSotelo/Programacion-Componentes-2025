package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class AgregarPlacaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_page) // Pantalla de carga

        // Simular tiempo de procesamiento
        Handler(Looper.getMainLooper()).postDelayed({
            val nuevaLlave = generarLlavePlaca()
            guardarLlave(nuevaLlave)
        }, 2000) // 2 segundos de espera (ajustable)
    }

    private fun generarLlavePlaca(): String {
        val letras = ('A'..'Z').shuffled().take(3).joinToString("")
        val numeros = (0..999).random().toString().padStart(3, '0')
        return "@$letras$numeros"
    }

    private fun guardarLlave(llave: String) {
        val sharedPrefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)
        val llaves = sharedPrefs.getStringSet("listaLlaves", mutableSetOf())!!.toMutableSet()

        llaves.add("PLACA:$llave")
        sharedPrefs.edit().putStringSet("listaLlaves", llaves).apply()

        // Ir a la pantalla principal de llaves
        val intent = Intent(this, LlavesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
