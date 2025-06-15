package com.example.parcial2.view.usuario
import com.android.volley.Request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.privacysandbox.tools.core.model.Method
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.R
import com.example.parcial2.model.Usuario
import okhttp3.*
import org.json.JSONArray

class UsuariosFragment : Fragment() {

    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var adapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerUsuarios = view.findViewById(R.id.recyclerUsuarios)
        recyclerUsuarios.layoutManager = LinearLayoutManager(requireContext())

        adapter = UsuarioAdapter(
            usuarios,
            onEditarClick = { usuario -> abrirModalEditar(usuario) },
            onEliminarClick = { usuario -> confirmarEliminacion(usuario.id) }
        )
        recyclerUsuarios.adapter = adapter

        view.findViewById<Button>(R.id.btnAgregarUsuario).setOnClickListener {
            abrirModalAgregar()
        }

        obtenerUsuarios()
    }

    private fun obtenerUsuarios() {
        val url = "http://192.168.101.71//Urban-Pixel/src/features/users/controller/ClienteControlador.php?accion=listar"

        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                cargarUsuariosDesdeJson(response)
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(request)
    }

    private fun cargarUsuariosDesdeJson(jsonArray: JSONArray) {
        usuarios.clear()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val usuario = Usuario(
                id = obj.getInt("id"),
                nombre = obj.getString("nombre"),
                apellido = obj.getString("apellido"),
                correo = obj.getString("correo"),
                clave = obj.getString("clave"),
                idRol = obj.getInt("idRol"),
                idEstado = obj.getString("idEstado")
            )
            usuarios.add(usuario)
        }
        adapter.notifyDataSetChanged()
    }

    private fun abrirModalAgregar() {
        // TODO: Implementar
    }

    private fun abrirModalEditar(usuario: Usuario) {
        // TODO: Implementar
    }

    private fun confirmarEliminacion(idUsuario: Int) {
        // TODO: Implementar
    }
}
