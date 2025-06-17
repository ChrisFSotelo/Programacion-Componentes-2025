package com.example.parcial2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.parcial2.view.categoria.CategoriasFragment
import com.example.parcial2.view.producto.ProductosFragment
import com.example.parcial2.view.usuario.UsuariosFragment

class PanelUsuarioActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_usuario)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Configurar la Toolbar como ActionBar
        setSupportActionBar(toolbar)

        // Configurar el toggle del menú lateral
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Fragmento inicial
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, InicioFragment())
            .commit()

        navigationView.setNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_inicio -> InicioFragment()
                R.id.nav_productos -> ProductosFragment()
                R.id.nav_usuarios -> UsuariosFragment()
                R.id.nav_categorias -> CategoriasFragment()
                R.id.nav_contacto -> ContactoFragment()
                R.id.nav_logout -> {
                    finish()
                    return@setNavigationItemSelectedListener true
                }
                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentFrame, it as Fragment)
                    .commit()
            }

            Handler(Looper.getMainLooper()).postDelayed({
                drawerLayout.closeDrawer(GravityCompat.START)
            }, 150)
            true
        }
    }

    // Necesario para que el ícono funcione correctamente
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}
