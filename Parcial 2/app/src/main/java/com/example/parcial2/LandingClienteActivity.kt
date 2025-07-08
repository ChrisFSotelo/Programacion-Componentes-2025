package com.example.parcial2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.parcial2.view.categoria.CategoriasFragment
import com.example.parcial2.view.producto.ProductosFragment
import com.example.parcial2.view.ventas.VentasFragment
import com.google.android.material.navigation.NavigationView

class LandingClienteActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_cliente)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Configurar la Toolbar
        setSupportActionBar(toolbar)

        // Toggle menú lateral
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Obtener ID del cliente autenticado
        val idCliente = intent.getIntExtra("idCliente", -1)
        if (idCliente != -1) {
            cargarDatosUsuarioDrawer(idCliente)
        }

        // Fragmento inicial
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, InicioFragment())
            .commit()

        navigationView.setNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_inicio -> InicioFragment()
                R.id.nav_productos -> ProductosFragment()
                R.id.nav_ventas -> VentasFragment()
                R.id.nav_logout -> {
                    finish()
                    return@setNavigationItemSelectedListener true
                }
                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, it)
                    .commit()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                drawerLayout.closeDrawer(GravityCompat.START)
            }, 150)
            true
        }
    }

    private fun cargarDatosUsuarioDrawer(idCliente: Int) {
        val url =
            "http://192.168.1.9/Urban-Pixel/src/features/users/controller/ClienteControlador.php?accion=obtenerPorId&id=$idCliente"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if (!response.has("error")) {
                    val nombre = response.getString("nombre")
                    val apellido = response.getString("apellido")
                    val correo = response.getString("correo")

                    val headerView = navigationView.getHeaderView(0)
                    val tvNombre = headerView.findViewById<TextView>(R.id.drawer_username)
                    val tvCorreo = headerView.findViewById<TextView>(R.id.drawer_email)
                    tvNombre.text = "$nombre $apellido"
                    tvCorreo.text = correo
                }
            },
            { error ->
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
