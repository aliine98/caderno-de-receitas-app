package br.com.aline.receitasapp.presentation.receitas

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.ListagemReceitasBinding
import br.com.aline.receitasapp.domain.model.ReceitaDTO
import br.com.aline.receitasapp.presentation.EventType
import br.com.aline.receitasapp.presentation.states.ReceitasState
import br.com.aline.receitasapp.presentation.states.ReceitasViewModel
import br.com.aline.receitasapp.presentation.states.ReceitaViewModelFactory
import br.com.aline.receitasapp.presentation.dialogs.DialogInput
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class ReceitasListagemFragment : Fragment() {

    private lateinit var binding: ListagemReceitasBinding
    private lateinit var receitasAdapter: ReceitasAdapter
    private val receitasViewModel: ReceitasViewModel by viewModels { ReceitaViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListagemReceitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupAdapter()
        observeStates()
    }

    private fun observeStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                receitasViewModel.listaReceitasState.collect { state ->
                    when(state) {
                        ReceitasState.Empty -> {
                            binding.pbReceitas.isVisible = false
                            binding.tvEmpty.isVisible = true
                            receitasAdapter.submitList(null)
                        }
                        is ReceitasState.Error -> {
                            binding.pbReceitas.isVisible = false
                            binding.tvEmpty.isVisible = false
                            Toast.makeText(
                                context,
                                getString(R.string.receita_state_erro),
                                Toast.LENGTH_LONG).show()
                        }
                        ReceitasState.Loading -> {
                            binding.pbReceitas.isVisible = true
                        }
                        is ReceitasState.Success -> {
                            binding.tvEmpty.isVisible = false
                            binding.pbReceitas.isVisible = false
                            receitasAdapter.submitList(state.receitas)
                        }
                    }
                }
            }
        }
        /*com liveData
        receitasViewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                ReceitasState.Empty -> {
                    binding.pbReceitas.isVisible = false
                    binding.tvEmpty.isVisible = true
                }
                is ReceitasState.Error -> {
                    binding.pbReceitas.isVisible = false
                    binding.tvEmpty.isVisible = false
                    Toast.makeText(
                        context,
                        "Não foi possível carregas as receitas! Tente novamente mais tarde!",
                        Toast.LENGTH_LONG).show()
                }
                ReceitasState.Loading -> {
                    binding.pbReceitas.isVisible = true
                }
                is ReceitasState.Success -> {
                    binding.tvEmpty.isVisible = false
                    binding.pbReceitas.isVisible = false
                    receitasAdapter.submitList(state.receitas)
                }
            }
        }*/
    }

    private fun setupAdapter() {
        receitasAdapter = ReceitasAdapter(this) { event,receita ->
            when(event) {
                EventType.DELETE -> {
                    receitasViewModel.deleteReceita(receita)
                }
                EventType.EDIT -> {
                    setFragmentResultListener(DialogInput.FRAGMENT_RESULT) {_,bundle ->
                        val tituloReceita = bundle.getString(DialogInput.INPUT_VALUE) ?: ""
                        val imageUri = Uri.parse(bundle.getString(DialogInput.IMAGE_URI))
                        if(imageUri.toString() != "") {
                            val capa = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageUri)
                            receitasViewModel.updateReceita(receita.copy(titulo = tituloReceita, capa = capa))
                        } else {
                            receitasViewModel.updateReceita(receita.copy(titulo = tituloReceita))
                        }
                    }
                }
                EventType.VIEW -> {
                    val bundle = bundleOf("RECEITA_ID" to receita.id,"RECEITA_NOME" to receita.titulo)
                    findNavController().navigate(R.id.action_ListagemReceitas_to_ReceitaDetalhe,bundle)
                }
            }
        }
        binding.listaReceitas.adapter = receitasAdapter
    }

    private fun setupListeners() {
        binding.fab.setOnClickListener {
            showDialog()
            setFragmentResultListener(DialogInput.FRAGMENT_RESULT) {_,bundle ->
                val tituloReceita = bundle.getString(DialogInput.INPUT_VALUE) ?: ""
                val imageUri = Uri.parse(bundle.getString(DialogInput.IMAGE_URI))
                if(imageUri.toString() != "") {
                    val capa = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageUri)
                    receitasViewModel.insertReceita(ReceitaDTO(titulo=tituloReceita,capa=capa))
                } else {
                    receitasViewModel.insertReceita(ReceitaDTO(titulo = tituloReceita,capa=null))
                }
            }
        }
    }

    private fun showDialog() {
        DialogInput.show(
            getString(R.string.nova_receita),
            getString(R.string.titulo_receita),
            parentFragmentManager,
            isReceita = true
        )
    }
}