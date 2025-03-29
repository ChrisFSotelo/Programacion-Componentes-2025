package com.example.a20marzo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val numberInteger = findViewById<EditText>(R.id.numberInteger)
        val optionConvert = findViewById<RadioGroup>(R.id.radioOptions)
        val buttonConvert = findViewById<Button>(R.id.button_convert)
        val textResponse = findViewById<TextView>(R.id.textResponse) // Cambio de nombre

        buttonConvert.setOnClickListener {
            val number = numberInteger.text.toString().toIntOrNull()

            if (number == null) {
                Toast.makeText(this, "No hay números para operar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val result = when (optionConvert.checkedRadioButtonId) {
                R.id.optionBinary -> number.toString(2)
                R.id.optionOctal -> number.toString(8)
                R.id.optionHexa -> number.toString(16)
                else -> "Seleccione una opción"
            }

            textResponse.text = "Respuesta: $result" // Se usa el TextView correctamente
        }
    }
}
