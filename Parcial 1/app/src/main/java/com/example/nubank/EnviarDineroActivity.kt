package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EnviarDineroActivity :AppCompatActivity() {

    private lateinit var btnEnviarDinero: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.send_money)
        btnEnviarDinero = findViewById(R.id.btnEnviarDinero)

        btnEnviarDinero.setOnClickListener {
            val intent = Intent(this,MethodSendActivity ::class.java)
            startActivity(intent)
        }

    }
}
