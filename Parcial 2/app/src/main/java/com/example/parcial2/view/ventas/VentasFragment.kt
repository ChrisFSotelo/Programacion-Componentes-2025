package com.example.parcial2.view.ventas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.adapter.FacturaAdapter
import com.example.parcial2.model.Factura
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class VentasFragment : Fragment() {

    private lateinit var recyclerVentas: RecyclerView
    private lateinit var adapter: FacturaAdapter
    private val listaFacturas = mutableListOf<Factura>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_ventas, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerVentas = view.findViewById(R.id.recyclerVentas)
        recyclerVentas.layoutManager = LinearLayoutManager(requireContext())

        adapter = FacturaAdapter(
            listaFacturas,
            onInfoClick = { factura ->
                obtenerDetallesFactura(factura.id)
            },
            onEliminarClick = { factura ->
                Toast.makeText(requireContext(), "Eliminar ID: ${factura.id}", Toast.LENGTH_SHORT).show()
            },
            onActualizarEstado = { factura, nuevoEstado ->
                actualizarEstadoVenta(factura.id, nuevoEstado)
            }
        )

        recyclerVentas.adapter = adapter

        view.findViewById<Button>(R.id.btnRegistrarVenta).setOnClickListener {
            Toast.makeText(requireContext(), "Registrar Venta", Toast.LENGTH_SHORT).show()
            // Aquí podrías abrir un modal o ir a pantalla de carrito
        }

        obtenerFacturas()
    }

    private fun obtenerFacturas() {
        val url = "http://192.168.0.16/Urban-Pixel/src/features/factura/controller/FacturaControlador.php?accion=listarVentas"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                listaFacturas.clear()

                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)

                    try {
                        val factura = Factura(
                            id = obj.optString("id").toIntOrNull() ?: 0,
                            fecha = obj.optString("fecha"),
                            hora = obj.optString("hora"),
                            subtotal = obj.optString("subtotal").toIntOrNull() ?: 0,
                            iva = obj.optString("iva").toFloatOrNull() ?: 0f,
                            total = obj.optString("total").toIntOrNull() ?: 0,
                            idCliente = obj.optString("idCliente").toIntOrNull() ?: 0,
                            ciudad = obj.optString("ciudad"),
                            direccion = obj.optString("direccion"),
                            estado = obj.optString("estado").toIntOrNull() ?: 0,
                            cliente = obj.optString("cliente", "Desconocido"),
                            productos = obj.optString("productos", "Sin productos")
                        )

                        listaFacturas.add(factura)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                adapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Error al cargar facturas", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun obtenerDetallesFactura(idFactura: Int) {
        val url = "http://192.168.0.16/Urban-Pixel/src/features/factura/controller/FacturaControlador.php?accion=obtenerDetallesVenta&idVenta=$idFactura"

        val request = com.android.volley.toolbox.JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    if (response.has("error")) {
                        Toast.makeText(requireContext(), "Error: ${response.getString("error")}", Toast.LENGTH_SHORT).show()
                        return@JsonObjectRequest
                    }

                    val facturaCompleta = com.example.parcial2.model.FacturaCompleta(
                        id = response.getInt("id"),
                        fecha = response.getString("fecha"),
                        hora = response.getString("hora"),
                        subtotal = response.getInt("subtotal"),
                        iva = response.getDouble("iva").toFloat(),
                        total = response.getInt("total"),
                        idCliente = response.getInt("idCliente"),
                        ciudad = response.getString("ciudad"),
                        direccion = response.getString("direccion"),
                        estado = response.getInt("estado"),
                        cliente = response.getString("cliente"),
                        productos = response.getString("productos").split(","),
                        preciosUnitarios = response.getString("preciosUnitarios").split(",").map { it.toIntOrNull() ?: 0 },
                        cantidades = response.getString("cantidades").split(",").map { it.toIntOrNull() ?: 0 },
                        precioVentas = response.getString("precioVentas").split(",").map { it.toIntOrNull() ?: 0 }
                    )

                    val dialog = FacturaDetalleDialogFragment(facturaCompleta)
                    dialog.show(parentFragmentManager, "detalle_factura")

                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), "Error al parsear detalle", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error al obtener detalles", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(requireContext()).add(request)
    }

    private fun actualizarEstadoVenta(idVenta: Int, estadoNuevo: Int) {
        val formBody = FormBody.Builder()
            .add("idVenta", idVenta.toString())
            .add("estadoNuevo", estadoNuevo.toString())
            .build()

        val request = okhttp3.Request.Builder()
            .url("http://192.168.0.16/Urban-Pixel/src/features/factura/controller/FacturaControlador.php?accion=actualizarEstadoVenta")
            .post(formBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string()
                requireActivity().runOnUiThread {
                    try {
                        val json = JSONObject(body ?: "{}")
                        if (json.has("mensaje")) {
                            Toast.makeText(requireContext(), json.getString("mensaje"), Toast.LENGTH_SHORT).show()
                            obtenerFacturas() // Recargar datos
                        } else if (json.has("error")) {
                            Toast.makeText(requireContext(), json.getString("error"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


}
