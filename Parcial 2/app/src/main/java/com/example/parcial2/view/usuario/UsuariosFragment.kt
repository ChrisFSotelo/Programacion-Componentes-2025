package com.example.parcial2.view.usuario
import android.app.AlertDialog
import com.android.volley.Request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.privacysandbox.tools.core.model.Method
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.model.Usuario
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response


class UsuariosFragment : Fragment() {

    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var adapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerUsuarios = view.findViewById(R.id.recyclerUsuarios)
        recyclerUsuarios.layoutManager = LinearLayoutManager(requireContext())

        adapter = UsuarioAdapter(
            usuarios,
            onEditarClick = { usuario -> abrirModalEditar(usuario) },
            onEliminarClick = { usuario -> confirmarEliminacion(usuario.id) }
        )
        recyclerUsuarios.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarUsuario).setOnClickListener {
            abrirModalAgregar()
        }

        obtenerUsuarios()
    }

    private fun obtenerUsuarios() {
        val url = "http://192.168.1.7//Urban-Pixel/src/features/users/controller/ClienteControlador.php?accion=listar"

        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                cargarUsuariosDesdeJson(response)
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(request)
    }

    private fun cargarUsuariosDesdeJson(jsonArray: JSONArray) {
        usuarios.clear()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val usuario = Usuario(
                id = obj.getInt("id"),
                nombre = obj.getString("nombre"),
                apellido = obj.getString("apellido"),
                correo = obj.getString("correo"),
                clave = obj.getString("clave"),
                idRol = obj.getInt("idRol"),
                idEstado = obj.getString("idEstado")
            )
            usuarios.add(usuario)
        }
        adapter.notifyDataSetChanged()
    }

    private fun abrirModalAgregar() {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_form_cliente, null)

        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etCorreo = dialogView.findViewById<EditText>(R.id.etCorreo)
        val etApellido = dialogView.findViewById<EditText>(R.id.etApellido)
        val etClave = dialogView.findViewById<EditText>(R.id.etClave)

        AlertDialog.Builder(requireContext()) // <- contexto corregido
            .setTitle("Registrar Cliente")
            .setView(dialogView)
            .setPositiveButton("Guardar", null) // Se maneja manualmente
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .apply {
                setOnShowListener {
                    val button = getButton(AlertDialog.BUTTON_POSITIVE)
                    button.setOnClickListener {
                        val nombre = etNombre.text.toString().trim()
                        val apellido = etApellido.text.toString().trim()
                        val correo = etCorreo.text.toString().trim()
                        val clave = etClave.text.toString().trim()

                        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val formBody = FormBody.Builder()
                            .add("nombre", nombre)
                            .add("apellido", apellido)
                            .add("correo", correo)
                            .add("clave", clave)
                            .build()

                        val request = okhttp3.Request.Builder()
                            .url("http://192.168.1.7/Urban-Pixel/src/features/users/controller/ClienteControlador.php?accion=registrar")
                            .post(formBody)
                            .build()

                        OkHttpClient().newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                activity?.runOnUiThread {
                                    Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                activity?.runOnUiThread {
                                    if (response.isSuccessful) {
                                        val respuesta = response.body?.string()
                                        try {
                                            val json = JSONObject(respuesta)
                                            if (json.has("mensaje")) {
                                                Toast.makeText(requireContext(), json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                                                dismiss() // cerrar el diálogo
                                            } else {
                                                Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(requireContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        })
                    }
                }
            }
            .show()
    }




    private fun abrirModalEditar(usuario: Usuario) {
        // TODO: Implementar
    }

    private fun confirmarEliminacion(idUsuario: Int) {
        // TODO: Implementar
    }
}
