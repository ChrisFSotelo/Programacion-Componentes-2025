import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parcial2.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var viewFlipper: ViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewFlipper = findViewById(R.id.form_flipper)

        val goToRegister: TextView = findViewById(R.id.go_to_register)
        val goToLogin: TextView = findViewById(R.id.go_to_login)

        goToRegister.setOnClickListener {
            viewFlipper.displayedChild = 0 // Registro
        }

        goToLogin.setOnClickListener {
            viewFlipper.displayedChild = 1 // Login
        }

        findViewById<Button>(R.id.btn_iniciar_sesion).setOnClickListener {
            iniciarSesion()
        }

        findViewById<Button>(R.id.btn_registrarse).setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nombre = findViewById<EditText>(R.id.et_nombre).text.toString()
        val apellido = findViewById<EditText>(R.id.et_apellido).text.toString()
        val correo = findViewById<EditText>(R.id.et_correo_registro).text.toString()
        val clave = findViewById<EditText>(R.id.et_clave_registro).text.toString()

        if (nombre.isBlank() || apellido.isBlank() || correo.isBlank() || clave.isBlank()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val formBody = FormBody.Builder()
            .add("nombre", nombre)
            .add("apellido", apellido)
            .add("correo", correo)
            .add("clave", clave)
            .build()

        val request = Request.Builder()
            .url("http://TU_DOMINIO/src/features/users/controller/ClienteControlador.php?accion=registrar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body?.string() ?: "{}")
                runOnUiThread {
                    if (json.has("mensaje")) {
                        Toast.makeText(this@LoginActivity, "Registro exitoso", Toast.LENGTH_LONG).show()
                        viewFlipper.displayedChild = 1
                    } else {
                        Toast.makeText(this@LoginActivity, json.optString("error", "Error desconocido"), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun iniciarSesion() {
        val correo = findViewById<EditText>(R.id.et_correo_login).text.toString()
        val clave = findViewById<EditText>(R.id.et_clave_login).text.toString()

        if (correo.isBlank() || clave.isBlank()) {
            Toast.makeText(this, "Correo y clave obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val formBody = FormBody.Builder()
            .add("correo", correo)
            .add("clave", clave)
            .build()

        val request = Request.Builder()
            .url("http://TU_DOMINIO/src/features/users/controller/UsuarioControlador.php?accion=autenticar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body?.string() ?: "{}")
                runOnUiThread {
                    if (json.has("error")) {
                        Toast.makeText(this@LoginActivity, json.getString("error"), Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }

                    val usuario = json.getJSONObject("usuario")
                    val rol = usuario.getString("rol")
                    val mensaje = json.optString("mensaje")

                    Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()

                    when (rol) {
                        "usuario" -> startActivity(Intent(this@LoginActivity, PanelUsuarioActivity::class.java))
                        "2" -> startActivity(Intent(this@LoginActivity, LandingClienteActivity::class.java))
                        else -> Toast.makeText(this@LoginActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
class PanelUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_usuario)
    }
}

class LandingClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_cliente)
    }
}
