package com.example.nubank

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.widget.ImageView

class InviteScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invite_screen)

        // Referencias a los LinearLayout por ID
        val whatsappBtn = findViewById<LinearLayout>(R.id.whatsappOption)
        val qrBtn = findViewById<LinearLayout>(R.id.qrOption)
        val emailBtn = findViewById<LinearLayout>(R.id.emailOption)
        val backBtn = findViewById<ImageView>(R.id.backIcon) // si agregas el ID en XML

        // Acción WhatsApp
        whatsappBtn.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "¡Únete a esta increíble app sin filas ni costos ocultos!")
                type = "text/plain"
                setPackage("com.whatsapp")
            }
            try {
                startActivity(sendIntent)
            } catch (e: Exception) {
                // WhatsApp no instalado
            }
        }

        // Acción QR
//        qrBtn.setOnClickListener {
//            val intent = Intent(this, QrCodeActivity::class.java)
//            startActivity(intent)
//        }

        // Acción Email
        emailBtn.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, "Invitación")
                putExtra(Intent.EXTRA_TEXT, "Te invito a probar esta app para evitar filas y comisiones.")
            }
            startActivity(Intent.createChooser(emailIntent, "Enviar invitación..."))
        }

        // Acción flecha atrás
        backBtn?.setOnClickListener {
            finish()
        }
    }
}