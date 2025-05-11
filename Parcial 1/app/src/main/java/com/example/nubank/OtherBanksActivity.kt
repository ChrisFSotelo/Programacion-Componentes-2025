package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class OtherBanksActivity : AppCompatActivity() {

    private lateinit var editTextMonto: EditText
    private lateinit var btnContinuar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.how_much_money)

        editTextMonto = findViewById(R.id.editTextMonto)
        btnContinuar = findViewById(R.id.btnContinuar)

        btnContinuar.setOnClickListener {
            val montoTexto = editTextMonto.text.toString()
            val monto = if (montoTexto.isNotEmpty()) montoTexto.toDouble() else 0.0

            val intent = Intent(this, DataBankActivity::class.java)
            intent.putExtra("monto_envio", monto)
            startActivity(intent)
        }

    }
}
