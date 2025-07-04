package com.example.parcial2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial2.R
import com.example.parcial2.model.Factura

class FacturaAdapter(
    private val facturas: List<Factura>,
    private val onInfoClick: (Factura) -> Unit,
    private val onEliminarClick: (Factura) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)
        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = facturas[position]

        holder.txtCliente.text = "Cliente: ${factura.cliente}"
        holder.txtProductos.text = "Productos: ${factura.productos}"
        holder.txtFecha.text = "Fecha: ${factura.fecha}"
        holder.txtHora.text = "Hora: ${factura.hora}"
        holder.txtTotal.text = "Total: $${String.format("%,d", factura.total)}"

        holder.btnEstado.text = when (factura.estado) {
            1 -> "En proceso"
            2 -> "Enviado"
            3 -> "Cancelado"
            else -> "Desconocido"
        }

        holder.btnEstado.setBackgroundColor(
            when (factura.estado) {
                1 -> 0xFF2196F3.toInt() // azul
                2 -> 0xFF4CAF50.toInt() // verde
                3 -> 0xFFF44336.toInt() // rojo
                else -> 0xFF9E9E9E.toInt() // gris
            }
        )

        holder.btnInfo.setOnClickListener { onInfoClick(factura) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(factura) }
    }

    override fun getItemCount(): Int = facturas.size

    class FacturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCliente: TextView = itemView.findViewById(R.id.txtCliente)
        val txtProductos: TextView = itemView.findViewById(R.id.txtProductos)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtHora: TextView = itemView.findViewById(R.id.txtHora)
        val txtTotal: TextView = itemView.findViewById(R.id.txtTotal)
        val btnEstado: Button = itemView.findViewById(R.id.btnEstado)
        val btnInfo: Button = itemView.findViewById(R.id.btnInfo)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }
}
