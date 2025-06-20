package br.com.aline.receitasapp.presentation

import android.database.CursorWindow
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.ActivityMainBinding
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 40 * 1024 * 1024) //the 40MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }*/

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
                    supportActionBar?.title = "Caderno de Receitas"
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}