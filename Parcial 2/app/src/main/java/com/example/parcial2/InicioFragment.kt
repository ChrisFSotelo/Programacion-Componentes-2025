package com.example.parcial2

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardUsuarios = view.findViewById<CardView>(R.id.cardUsuarios)
        val cardProductos = view.findViewById<CardView>(R.id.cardProductos)
        val cardCategorias = view.findViewById<CardView>(R.id.cardCategorias)
        val cardVentas = view.findViewById<CardView>(R.id.cardVentas)

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
