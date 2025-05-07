    package com.example.nubank

    import android.content.Context
    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.widget.addTextChangedListener
    import com.example.nubank.enums.TipoLlave
    import com.example.nubank.models.Llave

    class AgregarCelularActivity : AppCompatActivity() {

        private lateinit var phone_number: EditText
        private lateinit var btnContinuar: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.add_phone_key)

            phone_number = findViewById(R.id.phone_number)
            btnContinuar = findViewById(R.id.btn_agregar_llave_celular)

            // Deshabilitar el botón inicialmente
            btnContinuar.isEnabled = false

            phone_number.addTextChangedListener { text ->
                val celular = text.toString().trim()
                btnContinuar.isEnabled = celular.length == 10 && celular.all { it.isDigit() }
            }

            btnContinuar.setOnClickListener {
                val celular = phone_number.text.toString().trim()
                if (celular.length == 10 && celular.all { it.isDigit() }) {
                    guardarLlaveCelular(celular)
                } else {
                    Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun guardarLlaveCelular(numero: String) {
            val sharedPrefs = getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)

            // Convertir la nueva llave a formato de texto plano
            val nuevaLlave = "CELULAR:$numero"

            // Obtener el set actual, o uno nuevo si no existe
            val llavesGuardadas = sharedPrefs.getStringSet("listaLlaves", mutableSetOf())!!.toMutableSet()

            // Agregar la nueva llave
            llavesGuardadas.add(nuevaLlave)

            // Guardar de nuevo el set
            sharedPrefs.edit().putStringSet("listaLlaves", llavesGuardadas).apply()

            Toast.makeText(this, "Llave guardada exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LlavesActivity::class.java)
            startActivity(intent)

            finish()
        }


    }
