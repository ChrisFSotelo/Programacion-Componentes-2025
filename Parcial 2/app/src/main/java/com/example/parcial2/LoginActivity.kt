package com.example.parcial2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.view.animation.AnimationUtils // ⬅️ Asegúrate de importar esto
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import org.json.JSONException


class LoginActivity : AppCompatActivity() {

    private lateinit var viewFlipper: ViewFlipper


    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewFlipper = findViewById(R.id.form_flipper)
        viewFlipper.displayedChild = 0

        viewFlipper.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        viewFlipper.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)


        val goToRegister: TextView = findViewById(R.id.go_to_register)
        val goToLogin: TextView = findViewById(R.id.go_to_login)

        goToRegister.setOnClickListener {
            Log.d("holaaa", "Click en goToRegister")
            Toast.makeText(this, "Ir a registro", Toast.LENGTH_SHORT).show()
            viewFlipper.displayedChild = 1
        }

        goToLogin.setOnClickListener {
            Toast.makeText(this, "Volver a login", Toast.LENGTH_SHORT).show()
            viewFlipper.displayedChild = 0
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
            .url("http://192.168.101.71/Urban-Pixel/src/features/users/controller/ClienteControlador.php?accion=registrar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error de red222", Toast.LENGTH_SHORT).show()
                }
            }

            @OptIn(UnstableApi::class)
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val respuesta = response.body?.string()
                        try {
                            val json = JSONObject(respuesta)

                            if (json.has("error")) {
                                Toast.makeText(this@LoginActivity, json.getString("error"), Toast.LENGTH_LONG).show()
                                return@runOnUiThread
                            }

                            if (json.has("cliente")) {
                                val cliente = json.getJSONObject("cliente")

                                if (cliente.has("idRol")) {
                                    val rol = cliente.getInt("idRol")

                                     if (rol == 2) {
                                        Toast.makeText(this@LoginActivity, "Cliente Registrado correctamente", Toast.LENGTH_SHORT).show()
                                        // TODO: redirigir a pantalla cliente
                                    } else {
                                        Toast.makeText(this@LoginActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity, "Campo idRol no encontrado en JSON", Toast.LENGTH_SHORT).show()
                                }

                            } else {
                                Toast.makeText(this@LoginActivity, "Respuesta sin datos de cliente", Toast.LENGTH_SHORT).show()
                            }


                        } catch (e: Exception) {
                            Log.e("LOGIN_ERROR", "Error al parsear JSON: ${e.message}")
                            Toast.makeText(this@LoginActivity, "Error al procesar la respuesta", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Error: ${response.message}", Toast.LENGTH_LONG).show()
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
            .url("http://192.168.101.71/Urban-Pixel/src/features/users/controller/UsuarioControlador.php?accion=autenticar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            }

            @OptIn(UnstableApi::class)
            override fun onResponse(call: Call, response: Response) {
                val cuerpo = response.body?.string()
                Log.e("LOGIN_RESPONSE", "Respuesta cruda del servidor:\n$cuerpo")

                try {
                    val json = JSONObject(cuerpo ?: "{}")

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
                } catch (e: JSONException) {
                    Log.e("ERROR_JSON", "Error al convertir respuesta: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

