package com.example.parcial2.view.categoria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.parcial2.R
import com.example.parcial2.model.Categoria

class CategoriaAdapter(
    private val categorias: List<Categoria>,
    private val onEditarClick: (Categoria) -> Unit,
    private val onEliminarClick: (Categoria) -> Unit,
//    private val onEstadoClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombreCategoria: TextView = itemView.findViewById(R.id.txtNombreCategoria)
        val btnEstadoCategoria: Button = itemView.findViewById(R.id.btnEstadoCategoria)
        val btnEditarCategoria: Button = itemView.findViewById(R.id.btnEditarCategoria)
        val btnEliminarCategoria: Button = itemView.findViewById(R.id.btnEliminarCategoria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]

        holder.txtNombreCategoria.text = categoria.nombre

        // TO DO Estado visual
//        val context = holder.itemView.context
//        if (categoria.estado == 1) {
//            holder.btnEstadoCategoria.text = "Activo"
//            holder.btnEstadoCategoria.setBackgroundTintList(
//                ContextCompat.getColorStateList(context, R.color.verde_estado)
//            )
//        } else {
//            holder.btnEstadoCategoria.text = "Inactivo"
//            holder.btnEstadoCategoria.setBackgroundTintList(
//                ContextCompat.getColorStateList(context, R.color.rojo_estado)
//            )
//        }

        // Acciones
        holder.btnEditarCategoria.setOnClickListener { onEditarClick(categoria) }
        holder.btnEliminarCategoria.setOnClickListener { onEliminarClick(categoria) }
//        holder.btnEstadoCategoria.setOnClickListener { onEstadoClick(categoria) }
    }

    override fun getItemCount(): Int = categorias.size
}
