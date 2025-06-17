package com.example.parcial2.view.producto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.example.parcial2.R
import com.example.parcial2.model.Producto


class ProductoAdapter(
    private val productos: List<Producto>,
    private val onEditarClick: (Producto) -> Unit,
    private val onEliminarClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        //val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)

        val btnEstado: Button = itemView.findViewById(R.id.btnEstado)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.txtNombre.text = producto.nombre
        holder.txtCantidad.text = "Stock: ${producto.cantidad}"
        holder.txtPrecio.text = "Precio: $${producto.precio}"
       // holder.txtCategoria.text = "ID: ${producto.id}"

        // Estado visual (simulado como siempre activo, ajusta según necesites)
        holder.btnEstado.text = "Activo"
        val context = holder.itemView.context
        holder.btnEstado.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.verde_estado))

        // Acciones
        holder.btnEditar.setOnClickListener { onEditarClick(producto) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(producto) }

        // (Opcional) Podrías implementar un onEstadoClick si necesitas cambiar estado en productos
        // holder.btnEstado.setOnClickListener { onEstadoClick(producto) }
    }

    override fun getItemCount(): Int = productos.size
}
