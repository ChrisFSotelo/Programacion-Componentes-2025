package com.example.parcial2.view.ventas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.adapter.FacturaAdapter
import com.example.parcial2.model.Factura
import org.json.JSONException

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
                Toast.makeText(requireContext(), "Detalle: ${factura.id}", Toast.LENGTH_SHORT).show()
            },
            onEliminarClick = { factura ->
                Toast.makeText(requireContext(), "Eliminar ID: ${factura.id}", Toast.LENGTH_SHORT).show()
                // Aquí podrías mostrar confirmación y luego hacer petición DELETE si lo necesitas
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
        val url = "http://192.168.101.71/Urban-Pixel/src/features/factura/controller/FacturaControlador.php?accion=listarVentas"

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

}
