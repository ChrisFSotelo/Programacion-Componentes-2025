package com.example.nubank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovimientosAdapter(private val movimientos: List<Movimiento>) :
    RecyclerView.Adapter<MovimientosAdapter.MovimientoViewHolder>() {

    data class Movimiento(
        val descripcion: String,
        val fecha: String,
        val monto: String,
        val montoColor: Int,  // Para que puedas definir si es rojo o verde
        val iconoResId: Int   // ID del recurso del ícono
    )


    class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcono: ImageView = itemView.findViewById(R.id.ivIcono)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvMonto: TextView = itemView.findViewById(R.id.tvMonto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movement_list, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = movimientos[position]
        holder.tvDescripcion.text = movimiento.descripcion
        holder.tvFecha.text = movimiento.fecha
        holder.tvMonto.text = movimiento.monto
        holder.tvMonto.setTextColor(movimiento.montoColor)
        holder.ivIcono.setImageResource(movimiento.iconoResId)
    }

    override fun getItemCount(): Int = movimientos.size
}
