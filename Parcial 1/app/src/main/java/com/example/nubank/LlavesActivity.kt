package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nubank.enums.TipoLlave
import com.example.nubank.models.Llave

class LlavesActivity : AppCompatActivity() {

    private lateinit var btnAgregarLlave: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LlaveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.keys_screen)

        btnAgregarLlave = findViewById(R.id.btn_agregar_llave)
        recyclerView = findViewById(R.id.recyclerLlaves)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAgregarLlave.setOnClickListener {
            val intent = Intent(this, AgregarLlaveActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLlaves()
    }

    private fun cargarLlaves() {
        val prefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)
        val setLlaves = prefs.getStringSet("listaLlaves", emptySet())?.toMutableSet() ?: mutableSetOf()

        val listaLlaves = setLlaves.mapNotNull {
            val partes = it.split(":")
            if (partes.size == 2) {
                val tipoEnum = runCatching { TipoLlave.valueOf(partes[0]) }.getOrNull()
                tipoEnum?.let { tipo -> Llave(tipo, partes[1]) }
            } else null
        }

        adapter = LlaveAdapter(listaLlaves) { llave ->
            Toast.makeText(this, "Click en ${llave.tipo}: ${llave.valor}", Toast.LENGTH_SHORT).show()
            val bottomSheet = LlaveBottomSheet.newInstance(llave.tipo.name, llave.valor)
            bottomSheet.show(supportFragmentManager, "BottomSheetLlave")
        }

        recyclerView.adapter = adapter
    }

}

