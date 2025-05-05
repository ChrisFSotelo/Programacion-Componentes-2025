    package com.example.nubank

    import android.os.Bundle
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity

    class DetallesCuentaActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.account_details)

            // Referencias a los TextView del layout
            val tvNombreUsuario = findViewById<TextView>(R.id.tvNombreUsuario)
            val tvCedula = findViewById<TextView>(R.id.tvCedula)
            val tvPlaca = findViewById<TextView>(R.id.tvPlaca)
            val tvCuenta = findViewById<TextView>(R.id.tvCuenta)

            // Recuperar datos desde SharedPreferences
            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val firstName = sharedPref.getString("firstName", "")
            val lastName = sharedPref.getString("lastName", "")
            val idNumber = sharedPref.getString("idNumber", "")

            // Asignar valores a los TextView
            tvNombreUsuario.text = "$firstName $lastName"
            tvCedula.text = "Cédula: $idNumber"

            // Si tienes estos datos, puedes mostrarlos también (placeholder por ahora)
            tvPlaca.text = "Placa: [no disponible]"
            tvCuenta.text = "Cuenta: [no disponible]"

            // Abrir el bottom sheet al hacer clic en el ícono
            val ayudaBtn = findViewById<ImageView>(R.id.CuentaDeAhorrosNu)
            ayudaBtn.setOnClickListener {
                val bottomSheet = SavingAccountBottomSheet()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
        }
    }

