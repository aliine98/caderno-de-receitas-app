package br.com.aline.receitasapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener {_,destination,_ ->
            when(destination.id) {
                R.id.ReceitaDetalhe -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                R.id.ListagemReceitas -> {
                    supportActionBar?.title = getString(R.string.app_name)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}