package com.example.tma_semestralka

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tma_semestralka.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nastavenie toolbaru ako ActionBar
        setSupportActionBar(binding.toolbar)

        // NavController pre navigáciu medzi fragmentami
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Prepojenie NavigationView s NavigationControllerom
        binding.navigationView.setupWithNavController(navController)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_players -> navController.navigate(R.id.playerFragment)
                R.id.nav_exit -> finish() // Ukončí aplikáciu
            }
            // Zatvorenie Drawer menu po kliknutí
            binding.drawerLayout.closeDrawers()
            true
        }

        // Nastavenie DrawerToggle na otváranie/zatváranie menu
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = "${destination.label}"
        }
    }
}
