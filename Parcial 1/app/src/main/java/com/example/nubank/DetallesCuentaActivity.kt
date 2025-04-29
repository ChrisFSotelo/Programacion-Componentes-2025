    package com.example.nubank

    import android.os.Bundle
    import android.widget.ImageView
    import androidx.appcompat.app.AppCompatActivity

    class DetallesCuentaActivity : AppCompatActivity(){
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.account_details)

            val ayudaBtn = findViewById<ImageView>(R.id.CuentaDeAhorrosNu)
            ayudaBtn.setOnClickListener {
                val bottomSheet = SavingAccountBottomSheet()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }

        }

    }
