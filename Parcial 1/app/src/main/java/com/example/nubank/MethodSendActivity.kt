package com.example.nubank

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MethodSendActivity : AppCompatActivity(){

    private lateinit var btnLlave: LinearLayout
    private lateinit var btnNu: LinearLayout
    private lateinit var btnOtrosBancos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.method_send)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)


        btnOtrosBancos = findViewById(R.id.opcionOtrosBancos)

        btnOtrosBancos.setOnClickListener {
            val intent = Intent(this,OtherBanksActivity ::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this,HomeActivity ::class.java)
            startActivity(intent)
        }

    }

}
