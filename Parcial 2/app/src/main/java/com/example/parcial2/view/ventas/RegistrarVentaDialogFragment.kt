package com.example.parcial2.view.ventas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.model.ProductoSeleccionado
import com.example.parcial2.model.Usuario
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class RegistrarVentaDialogFragment : DialogFragment() {

    private lateinit var spinnerClientes: Spinner
    private lateinit var spinnerProducto: Spinner

    private lateinit var etCiudad: EditText
    private lateinit var etDireccion: EditText
    private lateinit var btnAgregarProducto: Button
    private lateinit var layoutProductosSeleccionados: LinearLayout
    private lateinit var btnConfirmarVenta: Button

    private val listaClientes = mutableListOf<Usuario>()
    private val todosLosProductos = mutableListOf<ProductoSeleccionado>()
    private val productosSeleccionados = mutableListOf<ProductoSeleccionado>()


    private val baseUrl = "http://192.168.1.9/Urban-Pixel/src"

    interface VentaRegistradaListener {
        fun onVentaRegistrada()
    }
    var ventaRegistradaListener: VentaRegistradaListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_registrar_venta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinnerClientes = view.findViewById(R.id.spinnerClientes)
        spinnerProducto = view.findViewById(R.id.spinnerProducto)

        etCiudad = view.findViewById(R.id.etCiudad)
        etDireccion = view.findViewById(R.id.etDireccion)
        btnAgregarProducto = view.findViewById(R.id.btnAgregarProducto)
        layoutProductosSeleccionados = view.findViewById(R.id.layoutProductosSeleccionados)
        btnConfirmarVenta = view.findViewById(R.id.btnConfirmarVenta)

        btnAgregarProducto.isEnabled = false
        cargarClientes()
        cargarProductos()

        btnAgregarProducto.setOnClickListener {
            val index = spinnerProducto.selectedItemPosition
            if (index == -1) {
                toast("Selecciona un producto")
                return@setOnClickListener
            }

            val producto = todosLosProductos[index]
            if (productosSeleccionados.any { it.id == producto.id }) {
                toast("Producto ya agregado")
                return@setOnClickListener
            }

            val seleccionado = producto.copy(seleccionado = true, cantidad = 1)
            productosSeleccionados.add(seleccionado)
            agregarProductoAlLayout(seleccionado)
        }


        btnConfirmarVenta.setOnClickListener {
            enviarVenta()
        }
    }

    private fun cargarClientes() {
        val url = "$baseUrl/features/users/controller/ClienteControlador.php?accion=listar"
        val request = JsonArrayRequest(url,
            { response ->
                listaClientes.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    listaClientes.add(
                        Usuario(
                            id = obj.getInt("id"),
                            nombre = obj.getString("nombre"),
                            apellido = obj.getString("apellido"),
                            correo = obj.getString("correo"),
                            clave = obj.getString("clave"),
                            idRol = obj.getInt("idRol"),
                            idEstado = obj.getString("idEstado")
                        )
                    )
                }

                val nombres = listaClientes.map { "${it.nombre} ${it.apellido}" }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerClientes.adapter = adapter
            },
            { toast("Error al cargar clientes") })

        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun cargarProductos() {
        val url = "$baseUrl/features/productos/controller/ProductoControlador.php?accion=listar"
        val request = JsonArrayRequest(url,
            { response ->
                todosLosProductos.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    todosLosProductos.add(
                        ProductoSeleccionado(
                            id = obj.getInt("id"),
                            nombre = obj.getString("nombre"),
                            precio = obj.getInt("precio"),
                            cantidad = 0,
                            seleccionado = false
                        )
                    )
                }

                val nombres = todosLosProductos.map { it.nombre }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombres)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProducto.adapter = adapter

                btnAgregarProducto.isEnabled = todosLosProductos.isNotEmpty()
            },
            {
                toast("Error al cargar productos")
            })

        Volley.newRequestQueue(requireContext()).add(request)
    }


    private fun agregarProductoAlLayout(producto: ProductoSeleccionado) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_producto_seleccionado, layoutProductosSeleccionados, false)

        val tvNombre = view.findViewById<TextView>(R.id.tvNombreProducto)
        val etCantidad = view.findViewById<EditText>(R.id.etCantidad)
        val btnEliminar = view.findViewById<ImageButton>(R.id.btnEliminarProducto)

        tvNombre.text = producto.nombre
        etCantidad.setText(producto.cantidad.toString())

        etCantidad.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val valor = s?.toString()?.toIntOrNull()
                producto.cantidad = if (valor != null && valor > 0) valor else 1
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnEliminar.setOnClickListener {
            layoutProductosSeleccionados.removeView(view)
            productosSeleccionados.remove(producto)
        }

        layoutProductosSeleccionados.addView(view)
    }

    private fun enviarVenta() {
        if (!validarFormulario()) return

        val cliente = listaClientes[spinnerClientes.selectedItemPosition]
        val seleccionados = productosSeleccionados.filter { it.cantidad > 0 }

        val form = FormBody.Builder()
            .add("idCliente", cliente.id.toString())
            .add("ciudad", etCiudad.text.toString())
            .add("direccion", etDireccion.text.toString())

        seleccionados.forEachIndexed { index, producto ->
            form.add("idsProductos[]", producto.id.toString())
            form.add("cantidades[]", producto.cantidad.toString())
        }

        enviarFormulario(form.build())
    }

    private fun validarFormulario(): Boolean {
        if (spinnerClientes.selectedItemPosition == -1) {
            toast("Selecciona un cliente")
            return false
        }

        if (etCiudad.text.isNullOrBlank() || etDireccion.text.isNullOrBlank()) {
            toast("Todos los campos son obligatorios")
            return false
        }

        if (productosSeleccionados.none { it.cantidad > 0 }) {
            toast("Agrega al menos un producto con cantidad válida")
            return false
        }

        return true
    }

    private fun enviarFormulario(formBody: FormBody) {
        val request = Request.Builder()
            .url("$baseUrl/features/factura/controller/FacturaControlador.php?accion=agregar")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    toast("Error de red: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                activity?.runOnUiThread {
                    Log.d("VENTA_RESPONSE", "Cuerpo recibido: $body")
                    try {
                        val json = JSONObject(body ?: "{}")
                        if (json.has("mensaje")) {
                            toast(json.getString("mensaje"))
                            ventaRegistradaListener?.onVentaRegistrada()

                            dismiss()
                        } else {
                            toast(json.optString("error", "Error desconocido"))
                        }
                    } catch (e: Exception) {
                        Log.e("VENTA_ERROR", "Error al procesar JSON: ${e.message}")
                        toast("Error al procesar respuesta")
                    }
                }
            }
        })
    }

    private fun toast(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

}
