package com.example.parcial2.view.categoria


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.model.Categoria

import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class CategoriasFragment : Fragment() {

    private lateinit var recyclerCategorias: RecyclerView
    private lateinit var adapter: CategoriaAdapter
    private val categorias = mutableListOf<Categoria>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categorias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerCategorias = view.findViewById(R.id.recyclerCategorias)
        recyclerCategorias.layoutManager = LinearLayoutManager(requireContext())

        adapter = CategoriaAdapter(
            categorias,
            onEditarClick = { categoria -> abrirModalEditarCategoria(categoria) },
            onEliminarClick = { categoria -> confirmarEliminacion(categoria.id) }
        )

        recyclerCategorias.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarCategoria).setOnClickListener {
            abrirModalAgregar()
        }

        obtenerCategorias()
    }

    private fun obtenerCategorias() {
        val url = "http://192.168.1.9/Urban-Pixel/src/features/categorias/controller/CategoriaControlador.php?accion=listar"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                categorias.clear()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    categorias.add(Categoria(item.getInt("id"), item.getString("nombre")))
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(requireContext(), "Error al cargar categorías", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun abrirModalAgregar() {
        val view = layoutInflater.inflate(R.layout.dialog_form_categoria, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreCategoria)

        AlertDialog.Builder(requireContext())
            .setTitle("Registrar Categoría")
            .setView(view)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val nombre = etNombre.text.toString()
                        if (nombre.isEmpty()) {
                            Toast.makeText(context, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val form = FormBody.Builder()
                            .add("nombre", nombre)
                            .build()

                        val request = okhttp3.Request.Builder()
                            .url("http://192.168.1.7/Urban-Pixel/src/features/categorias/controller/CategoriaControlador.php?accion=agregar")
                            .post(form)
                            .build()

                        OkHttpClient().newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                activity?.runOnUiThread {
                                    Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                activity?.runOnUiThread {
                                    if (response.isSuccessful) {
                                        val json = JSONObject(response.body?.string() ?: "")
                                        Toast.makeText(context, json.optString("mensaje", "Categoría registrada"), Toast.LENGTH_SHORT).show()
                                        dismiss()
                                        obtenerCategorias()
                                    } else {
                                        Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        })
                    }
                }
            }.show()
    }

    private fun abrirModalEditarCategoria(categoria: Categoria) {
        val view = layoutInflater.inflate(R.layout.dialog_form_categoria, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombreCategoria)
        etNombre.setText(categoria.nombre)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Categoría")
            .setView(view)
            .setPositiveButton("Actualizar", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val nuevoNombre = etNombre.text.toString()

                if (nuevoNombre.isEmpty()) {
                    Toast.makeText(context, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val form = FormBody.Builder()
                    .add("id", categoria.id.toString())
                    .add("nombre", nuevoNombre)
                    .build()

                val request = okhttp3.Request.Builder()
                    .url("http://192.168.1.9/Urban-Pixel/src/features/categorias/controller/CategoriaControlador.php?accion=actualizar")
                    .post(form)
                    .build()

                OkHttpClient().newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        activity?.runOnUiThread {
                            if (response.isSuccessful) {
                                val json = JSONObject(response.body?.string() ?: "")
                                Toast.makeText(context, json.optString("mensaje", "Categoría actualizada"), Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                obtenerCategorias()
                            } else {
                                Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }

        dialog.show()
    }

    private fun confirmarEliminacion(id: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("¿Eliminar categoría?")
            .setMessage("Esta acción no se puede deshacer.")
            .setPositiveButton("Sí") { _, _ ->
                eliminarCategoria(id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarCategoria(id: Int) {

        val url = "http://192.168.1.9/Urban-Pixel/src/features/categorias/controller/CategoriaControlador.php?accion=eliminar&id=$id"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)

                    if (jsonObject.has("mensaje")) {
                        Toast.makeText(requireContext(), jsonObject.getString("mensaje"), Toast.LENGTH_SHORT).show()
                        obtenerCategorias()
                    } else if (jsonObject.has("error")) {
                        Toast.makeText(requireContext(), jsonObject.getString("error"), Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error de formato en respuesta", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Error de conexión al eliminar categoría", Toast.LENGTH_SHORT).show()
            }
        )

        // Agrega a la cola de Volley
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

}
