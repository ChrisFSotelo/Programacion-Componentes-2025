package com.example.parcial2.view.usuario

import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.model.Usuario
import org.json.JSONObject

class UsuarioAdapter(
    private val usuarios: List<Usuario>,
    private val onEditarClick: (Usuario) -> Unit,
    private val onEliminarClick: (Usuario) -> Unit,
    private val onEstadoClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtApellido: TextView = itemView.findViewById(R.id.txtApellido)
        val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreo)
        val btnEstado: TextView = itemView.findViewById(R.id.btnEstado)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.txtNombre.text = usuario.nombre
        holder.txtApellido.text = usuario.apellido
        holder.txtCorreo.text = usuario.correo

        holder.btnEstado.text = usuario.idEstado // Ya viene "Activo" o "Inactivo"

        // Cambiar color según el estado
        val context = holder.itemView.context
        if (usuario.idEstado == "Activo") {
            holder.btnEstado.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.verde_estado))
        } else {
            holder.btnEstado.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gris_estado))
        }

        // Acciones
        holder.btnEditar.setOnClickListener { onEditarClick(usuario) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(usuario) }
        holder.btnEstado.setOnClickListener { onEstadoClick(usuario) }
    }



    override fun getItemCount(): Int = usuarios.size
}
