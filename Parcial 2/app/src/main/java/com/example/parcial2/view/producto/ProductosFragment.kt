package com.example.parcial2.view.producto

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.model.Categoria
import com.example.parcial2.model.Producto
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class ProductosFragment : Fragment() {

    private lateinit var recyclerProductos: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val productos = mutableListOf<Producto>()
    private var listaCategorias = mutableListOf<Categoria>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_productos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerProductos = view.findViewById(R.id.recyclerProductos)
        recyclerProductos.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductoAdapter(
            productos,
            onEditarClick = { producto -> abrirModalEditarProducto(producto) },
            onEliminarClick = { producto -> confirmarEliminacion(producto.id) }
//            , onEstadoClick = { producto -> actualizarEstadoProducto(producto.id) }
        )
        recyclerProductos.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarProducto).setOnClickListener {
            abrirModalAgregar()
        }

        obtenerProductos()
        cargarCategorias()
    }

    private fun obtenerProductos() {
        val url = "http://192.168.1.7/Urban-Pixel/src/features/productos/controller/ProductoControlador.php?accion=listar"

        val queue = Volley.newRequestQueue(requireContext())
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response -> cargarProductosDesdeJson(response) },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }

    private fun cargarProductosDesdeJson(jsonArray: JSONArray) {
        productos.clear()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val producto = Producto(
                id = obj.getInt("id"),
                nombre = obj.getString("nombre"),
                cantidad = obj.getString("cantidad"),
                precio = obj.getString("precio"),
                categoria = obj.getInt("idCategoria"),
            )
            productos.add(producto)
        }
        adapter.notifyDataSetChanged()
    }

    private fun abrirModalAgregar() {
        val view = layoutInflater.inflate(R.layout.dialog_form_producto, null)
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etPrecio = view.findViewById<EditText>(R.id.etPrecio)
        val spCategoria = view.findViewById<Spinner>(R.id.spCategoria)

        val categoriaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCategorias)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = categoriaAdapter

        AlertDialog.Builder(requireContext())
            .setTitle("Registrar Producto")
            .setView(view)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val nombre = etNombre.text.toString()
                        val cantidad = etCantidad.text.toString()
                        val precio = etPrecio.text.toString()
                        val categoriaSeleccionada = spCategoria.selectedItem as? Categoria

                        if (nombre.isEmpty() || cantidad.isEmpty() || precio.isEmpty() || categoriaSeleccionada == null) {
                            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        val form = FormBody.Builder()
                            .add("nombre", nombre)
                            .add("cantidad", cantidad)
                            .add("precio", precio)
                            .add("categoria", categoriaSeleccionada.id.toString()) // Enviar ID de categoría
                            .build()

                        val request = okhttp3.Request.Builder()
                            .url("http://192.168.1.7/Urban-Pixel/src/features/productos/controller/ProductoControlador.php?accion=registrar_producto")
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
                                        if (json.has("mensaje")) {
                                            Toast.makeText(context, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                                            dismiss()
                                            obtenerProductos()
                                        } else {
                                            Toast.makeText(context, json.optString("error", "Error desconocido"), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }.show()
    }


    private fun abrirModalEditarProducto(producto: Producto) {
        val view = layoutInflater.inflate(R.layout.dialog_form_producto, null)

        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val etPrecio = view.findViewById<EditText>(R.id.etPrecio)
        val spCategoria = view.findViewById<Spinner>(R.id.spCategoria)

        // Prellenar campos
        etNombre.setText(producto.nombre)
        etCantidad.setText(producto.cantidad.toString())
        etPrecio.setText(producto.precio.toString())

        // Adaptador para el Spinner
        val categoriaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaCategorias)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategoria.adapter = categoriaAdapter

        // Seleccionar la categoría actual
        val index = listaCategorias.indexOfFirst { it.id == producto.categoria }
        if (index != -1) {
            spCategoria.setSelection(index)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Editar Producto")
            .setView(view)
            .setPositiveButton("Actualizar", null)
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnGuardar.setOnClickListener {
                val nombre = etNombre.text.toString()
                val cantidad = etCantidad.text.toString()
                val precio = etPrecio.text.toString()
                val categoria = spCategoria.selectedItem as? Categoria

                if (nombre.isEmpty() || cantidad.isEmpty() || precio.isEmpty() || categoria == null) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                actualizarProducto(
                    producto.id,
                    nombre,
                    cantidad,
                    precio,
                    categoria.id,
                    dialog
                )
            }
        }

        dialog.show()
    }
    private fun actualizarProducto(
        id: Int,
        nombre: String,
        cantidad: String,
        precio: String,
        categoriaId: Int,
        dialog: AlertDialog
    ) {
        val formBody = FormBody.Builder()
            .add("id", id.toString())
            .add("nombre", nombre)
            .add("cantidad", cantidad)
            .add("precio", precio)
            .add("categoria", categoriaId.toString())
            .build()

        val request = okhttp3.Request.Builder()
            .url("http://192.168.1.7/Urban-Pixel/src/features/productos/controller/ProductoControlador.php?accion=actualizar")
            .post(formBody)
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
                        if (json.has("mensaje")) {
                            Toast.makeText(context, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            obtenerProductos()
                        } else {
                            Toast.makeText(context, json.optString("error", "Error al actualizar"), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


    private fun cargarCategorias() {
        val url = "http://192.168.1.7/Urban-Pixel/src/features/categorias/controller/CategoriaControlador.php?accion=listar"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                listaCategorias.clear()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val id = item.getInt("id")
                    val nombre = item.getString("nombre")
                    listaCategorias.add(Categoria(id, nombre))
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error al cargar categorías", Toast.LENGTH_SHORT).show()
                Log.e("Volley", "Error: ${error.message}")
            }
        )
        Volley.newRequestQueue(requireContext()).add(request)
    }



//    private fun actualizarEstadoProducto(id: Int) {
//        val producto = productos.find { it.id == id } ?: return
//        val estadoActual = if (producto.estado == "Activo") 1 else 0
//
//        val form = FormBody.Builder()
//            .add("id", id.toString())
//            .add("estado", estadoActual.toString())
//            .build()
//
//        val request = Request.Builder()
//            .url("http://192.168.1.7/Urban-Pixel/src/features/productos/controller/ProductoControlador.php?accion=actualizarEstado")
//            .post(form)
//            .build()
//
//        OkHttpClient().newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                activity?.runOnUiThread {
//                    Toast.makeText(context, "Error de red", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                activity?.runOnUiThread {
//                    if (response.isSuccessful) {
//                        val json = JSONObject(response.body?.string() ?: "")
//                        if (json.has("mensaje")) {
//                            Toast.makeText(context, json.getString("mensaje"), Toast.LENGTH_SHORT).show()
//                            obtenerProductos()
//                        } else {
//                            Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//        })
//    }

    private fun confirmarEliminacion(id: Int) {
        // TODO: Implementar funcionalidad de eliminación con confirmación
    }
}