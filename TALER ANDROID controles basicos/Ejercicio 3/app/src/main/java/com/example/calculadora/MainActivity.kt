package com.example.calculadora

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    // Variables para los componentes
    private lateinit var etNumero1: EditText
    private lateinit var etNumero2: EditText
    private lateinit var rgOperaciones: RadioGroup
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Asignación de vistas
        etNumero1 = findViewById(R.id.etNumero1)
        etNumero2 = findViewById(R.id.etNumero2)
        rgOperaciones = findViewById(R.id.rgOperaciones)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)

        btnCalcular.setOnClickListener {
            realizarCalculo()
        }
    }

    private fun realizarCalculo() {
        val num1Str = etNumero1.text.toString()
        val num2Str = etNumero2.text.toString()

        // Validar que al menos el primer número esté presente
        if (num1Str.isEmpty()) {
            Toast.makeText(this, "Ingrese el primer número", Toast.LENGTH_SHORT).show()
            return
        }

        val num1 = num1Str.toDoubleOrNull()
        val num2 = num2Str.toDoubleOrNull()

        if (num1 == null) {
            Toast.makeText(this, "Número 1 inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRadioId = rgOperaciones.checkedRadioButtonId

        if (selectedRadioId == -1) {
            Toast.makeText(this, "Seleccione una operación", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = when (selectedRadioId) {
            R.id.rbSuma -> {
                if (num2 == null) showNumero2Invalido() else num1 + num2
            }
            R.id.rbResta -> {
                if (num2 == null) showNumero2Invalido() else num1 - num2
            }
            R.id.rbMultiplicacion -> {
                if (num2 == null) showNumero2Invalido() else num1 * num2
            }
            R.id.rbDivision -> {
                if (num2 == null || num2 == 0.0) {
                    Toast.makeText(this, "División por cero no permitida", Toast.LENGTH_SHORT).show()
                    return
                } else num1 / num2
            }
            R.id.rbFactorial -> {
                if (num1 < 0 || num1 % 1 != 0.0) {
                    Toast.makeText(this, "El factorial solo se puede calcular con números enteros no negativos", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    factorial(num1.toInt()).toDouble()
                }
            }
            else -> {
                Toast.makeText(this, "Operación desconocida", Toast.LENGTH_SHORT).show()
                return
            }
        }

        tvResultado.text = "Resultado: $resultado"
    }

    private fun factorial(n: Int): Long {
        var result = 1L
        for (i in 1..n) {
            result *= i
        }
        return result
    }

    private fun showNumero2Invalido(): Nothing {
        Toast.makeText(this, "Ingrese el segundo número", Toast.LENGTH_SHORT).show()
        throw IllegalArgumentException("Número 2 inválido")
    }
}
