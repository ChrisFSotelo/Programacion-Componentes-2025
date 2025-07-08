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

        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE)
        val correoGuardado = prefs.getString("correo", "")
        val claveGuardada = prefs.getString("clave", "")
        val recordar = prefs.getBoolean("recordar", false)

        findViewById<EditText>(R.id.et_correo_login).setText(correoGuardado)
        findViewById<EditText>(R.id.et_clave_login).setText(claveGuardada)
        findViewById<CheckBox>(R.id.chk_recordar).isChecked = recordar

        // animaciones y navegación entre formularios
        viewFlipper.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        viewFlipper.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)

        findViewById<TextView>(R.id.go_to_register).setOnClickListener {
            viewFlipper.displayedChild = 1
        }

        findViewById<TextView>(R.id.go_to_login).setOnClickListener {
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
            .url("http://192.168.1.7/Urban-Pixel/src/features/users/controller/ClienteControlador.php?accion=registrar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            @OptIn(UnstableApi::class)
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR_OKHTTP", "Fallo de red: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
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
        val chkRecordar = findViewById<CheckBox>(R.id.chk_recordar).isChecked

        if (correo.isBlank() || clave.isBlank()) {
            Toast.makeText(this, "Correo y clave obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val formBody = FormBody.Builder()
            .add("correo", correo)
            .add("clave", clave)
            .build()

        val request = Request.Builder()
            .url("http://192.168.1.9/Urban-Pixel/src/features/users/controller/UsuarioControlador.php?accion=autenticar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            @OptIn(UnstableApi::class)
            override fun onFailure(call: Call, e: IOException) {
                Log.e("LOGIN_ERROR", "Fallo de red: ${e.message}", e) // <-- Esta línea es clave
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val cuerpo = response.body?.string()

                try {
                    val json = JSONObject(cuerpo ?: "{}")
                    runOnUiThread {
                        if (json.has("error")) {
                            Toast.makeText(this@LoginActivity, json.getString("error"), Toast.LENGTH_SHORT).show()
                            return@runOnUiThread
                        }

                        val usuario = json.getJSONObject("usuario")
                        val rol = usuario.getString("rol")
                        val idUsuario = usuario.getInt("id") // 👈 Extrae el ID
                        val mensaje = json.optString("mensaje")


                        val prefs = getSharedPreferences("datos_usuario", MODE_PRIVATE).edit()
                        if (chkRecordar) {
                            prefs.putString("correo", correo)
                            prefs.putString("clave", clave)
                            prefs.putBoolean("recordar", true)
                        } else {
                            prefs.clear() // No guardar nada si no está marcado
                        }
                        prefs.apply()

                        Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()

                        when (rol) {
                            "1" -> {
                                val intent = Intent(this@LoginActivity, PanelUsuarioActivity::class.java)
                                intent.putExtra("idUsuario", idUsuario)
                                startActivity(intent)                            }
                            "2" -> startActivity(Intent(this@LoginActivity, LandingClienteActivity::class.java))
                            else -> Toast.makeText(this@LoginActivity, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: JSONException) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

