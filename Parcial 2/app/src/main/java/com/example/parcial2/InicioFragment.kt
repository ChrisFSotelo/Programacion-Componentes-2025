package com.example.parcial2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.parcial2.view.categoria.CategoriasFragment
import com.example.parcial2.view.producto.ProductosFragment
import com.example.parcial2.view.usuario.UsuariosFragment
import com.example.parcial2.view.ventas.VentasFragment

class InicioFragment : Fragment() {

    private lateinit var cardUsuarios: CardView
    private lateinit var cardProductos: CardView
    private lateinit var cardCategorias: CardView
    private lateinit var cardVentas: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardUsuarios = view.findViewById(R.id.cardUsuarios)
        cardProductos = view.findViewById(R.id.cardProductos)
        cardCategorias = view.findViewById(R.id.cardCategorias)
        cardVentas = view.findViewById(R.id.cardVentas)

        // Obtener el rol guardado
        val prefs = requireActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE)
        val rol = prefs.getString("rol", null)

        // Ocultar tarjetas según el rol
        if (rol == "2") { // Cliente
            cardUsuarios.visibility = View.GONE
            cardCategorias.visibility = View.GONE
        }

        // Configurar navegación
        cardUsuarios.setOnClickListener {
            navegarAFragment(UsuariosFragment())
        }

        cardProductos.setOnClickListener {
            navegarAFragment(ProductosFragment())
        }

        cardCategorias.setOnClickListener {
            navegarAFragment(CategoriasFragment())
        }

        cardVentas.setOnClickListener {
            navegarAFragment(VentasFragment())
        }
    }

    private fun navegarAFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, fragment)
            .addToBackStack(null)
            .commit()
    }
}
