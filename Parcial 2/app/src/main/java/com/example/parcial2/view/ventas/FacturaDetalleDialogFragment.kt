package com.example.parcial2.view.ventas

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.parcial2.R
import com.example.parcial2.model.FacturaCompleta
import java.text.NumberFormat
import java.util.Locale

class FacturaDetalleDialogFragment(
    private val factura: FacturaCompleta
) : DialogFragment() {

    private val formatoMoneda = NumberFormat.getNumberInstance(Locale("es", "CO"))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_factura_detalle, null)

        // Referencias
        val tvCliente = view.findViewById<TextView>(R.id.tvCliente)
        val tvCiudad = view.findViewById<TextView>(R.id.tvCiudad)
        val tvDireccion = view.findViewById<TextView>(R.id.tvDireccion)
        val tvFechaHora = view.findViewById<TextView>(R.id.tvFechaHora)
        val tvEstado = view.findViewById<TextView>(R.id.tvEstado)
        val layoutProductos = view.findViewById<LinearLayout>(R.id.layoutProductos)
        val tvSubtotal = view.findViewById<TextView>(R.id.tvSubtotal)
        val tvIva = view.findViewById<TextView>(R.id.tvIva)
        val tvTotal = view.findViewById<TextView>(R.id.tvTotal)
        val btnCerrar = view.findViewById<View>(R.id.btnCerrar)

        // Datos generales
        tvCliente.text = "Cliente: ${factura.cliente}"
        tvCiudad.text = "Ciudad: ${factura.ciudad}"
        tvDireccion.text = "Dirección: ${factura.direccion}"
        tvFechaHora.text = "Fecha y Hora: ${factura.fecha} ${factura.hora}"

        val estadoTexto = when (factura.estado) {
            1 -> "En proceso"
            2 -> "Enviado"
            3 -> "Cancelado"
            else -> "Desconocido"
        }
        tvEstado.text = "Estado: $estadoTexto"

        // Lista de productos
        for (i in factura.productos.indices) {
            val fila = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_fila_producto_venta, layoutProductos, false)

            fila.findViewById<TextView>(R.id.tvNombreProducto).text = factura.productos[i]
            fila.findViewById<TextView>(R.id.tvCantidad).text = factura.cantidades[i].toString()
            fila.findViewById<TextView>(R.id.tvPrecioUnitario).text = "$${formatoMoneda.format(factura.preciosUnitarios[i])}"
            fila.findViewById<TextView>(R.id.tvTotalProducto).text = "$${formatoMoneda.format(factura.precioVentas[i])}"

            layoutProductos.addView(fila)
        }

        // Resumen
        val ivaValor = factura.subtotal * factura.iva
        tvSubtotal.text = "Subtotal: $${formatoMoneda.format(factura.subtotal)}"
        tvIva.text = "IVA (${(factura.iva * 100).toInt()}%): $${"%.2f".format(ivaValor)}"
        tvTotal.text = "Total: $${formatoMoneda.format(factura.total)}"

        // Botón cerrar
        btnCerrar.setOnClickListener { dismiss() }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}
