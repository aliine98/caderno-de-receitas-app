package br.com.aline.receitasapp.presentation.receitaDetalhe

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.DetalhesReceitaBinding
import br.com.aline.receitasapp.presentation.states.ReceitaCompletaState
import br.com.aline.receitasapp.presentation.states.ReceitaViewModelFactory
import br.com.aline.receitasapp.presentation.states.ReceitasViewModel
import br.com.aline.receitasapp.domain.model.IngredienteDTO
import br.com.aline.receitasapp.domain.model.ModoDePreparoDTO
import br.com.aline.receitasapp.presentation.EventType
import br.com.aline.receitasapp.presentation.dialogs.DialogInput
import kotlinx.coroutines.launch

class ReceitaDetalheFragment : Fragment() {

    companion object {
        const val CHECKBOX_STATES = "CHECKBOX_STATES"
    }
    private var idReceita = 0
    private var _binding: DetalhesReceitaBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReceitasViewModel> { ReceitaViewModelFactory() }
    private lateinit var ingredientesAdapter: IngredientesAdapter
    private lateinit var modoPreparoAdapter: PassosModoPreparoAdapter
    private var modoPreparoDTO: ModoDePreparoDTO? = null
    private lateinit var checkboxStates: HashMap<Int,Boolean>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DetalhesReceitaBinding.inflate(inflater, container, false)
        val act = activity as AppCompatActivity
        act.supportActionBar?.title = getString(R.string.receita) + arguments?.getString("RECEITA_NOME")
        checkboxStates = (savedInstanceState?.getSerializable(CHECKBOX_STATES) ?: HashMap<Int,Boolean>()) as HashMap<Int, Boolean>

        return binding.root

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CHECKBOX_STATES,checkboxStates)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        observeStates()
        setupListeners()
    }

    private fun setupAdapters() {
        ingredientesAdapter = IngredientesAdapter(this) { event, item ->
            when(event) {
                EventType.DELETE -> {
                    viewModel.deleteIngrediente(item)
                }
                EventType.EDIT -> {
                    setFragmentResultListener(DialogInput.FRAGMENT_RESULT) {_,bundle ->
                        val nome = bundle.getString(DialogInput.INPUT_VALUE) ?: ""
                        if(nome != "") {
                            val ingrediente = item.copy(nome=nome)
                            viewModel.addOrUpdateIngrediente(ingrediente)
                        }
                    }
                }
                EventType.VIEW -> {Log.e("receitas","Evento view não suportado")}
            }
        }
        binding.listaIngredientes.adapter = ingredientesAdapter

        modoPreparoAdapter = PassosModoPreparoAdapter(this,checkboxStates) { event, passo ->
            when(event) {
                EventType.DELETE -> {
                    if(modoPreparoDTO != null) {
                        modoPreparoDTO!!.passos = modoPreparoDTO!!.passos.filterNot { it === passo }
                        viewModel.addOrUpdateModoPreparo(modoPreparoDTO!!)
                    }
                }
                EventType.EDIT -> {
                    if(modoPreparoDTO != null) {
                        setFragmentResultListener(DialogInput.FRAGMENT_RESULT) {_,bundle ->
                            val passoEditado = bundle.getString(DialogInput.INPUT_VALUE) ?: ""
                            if(passoEditado != "") {
                                modoPreparoDTO!!.passos = modoPreparoDTO!!.passos.map {
                                        if (it == passo) passoEditado else it
                                    }
                                viewModel.addOrUpdateModoPreparo(modoPreparoDTO!!)
                            }
                        }
                    }
                }
                EventType.VIEW -> {Log.e("receitas","Evento view não suportado")}
            }
        }
        binding.listaPassosPreparo.adapter = modoPreparoAdapter
    }

    private fun setupListeners() {
        binding.addIngrediente.setOnClickListener {
            showDialogNovoIngrediente()
            setFragmentResultListener(DialogInput.FRAGMENT_RESULT) {_,bundle ->
                val nome = bundle.getString(DialogInput.INPUT_VALUE) ?: ""
                if(nome != "") {
                    viewModel.addOrUpdateIngrediente(IngredienteDTO(nome=nome, idReceita=idReceita))
                }
            }
        }

        binding.addPassoModoPreparo.setOnClickListener {
            showDialogNovoPasso()
            setFragmentResultListener(DialogInput.FRAGMENT_RESULT) {_,bundle ->
                val passo = bundle.getString(DialogInput.INPUT_VALUE) ?: ""
                if(passo != "") {
                    if(modoPreparoDTO == null) {
                        viewModel.addOrUpdateModoPreparo(ModoDePreparoDTO(passos= listOf(passo),
                            idReceita=idReceita))
                    } else {
                        modoPreparoDTO!!.passos = listOf(*modoPreparoDTO!!.passos.toTypedArray(), passo)
                        viewModel.addOrUpdateModoPreparo(modoPreparoDTO!!)
                    }
                }
            }
        }
    }

    private fun showDialogNovoPasso() {
        DialogInput.show(getString(R.string.novo_passo),getString(R.string.descricao),parentFragmentManager)
    }

    private fun showDialogNovoIngrediente() {
        DialogInput.show(getString(R.string.novo_ingrediente),getString(R.string.nome),parentFragmentManager)
    }

    private fun observeStates() {
        idReceita = arguments?.getInt("RECEITA_ID") ?: 0
        viewModel.getReceita(idReceita)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.receitaCompetaState.collect { state ->
                    when(state) {
                        is ReceitaCompletaState.Error -> {
                            binding.loadingIngredientes.isVisible = false
                            binding.emptyIngrediente.isVisible = false
                            binding.loadingPassosPreparo.isVisible = false
                            binding.emptyPassos.isVisible = false
                            Toast.makeText(context, getString(R.string.receita_completa_state_erro),Toast.LENGTH_LONG)
                        }
                        ReceitaCompletaState.Loading -> {
                            binding.loadingIngredientes.isVisible = true
                            binding.emptyIngrediente.isVisible = false
                            binding.loadingPassosPreparo.isVisible = true
                            binding.emptyPassos.isVisible = false
                        }
                        is ReceitaCompletaState.Success -> {
                            modoPreparoDTO = state.receita.modoDePreparoDTO
                            if(state.receita.ingredientes.isEmpty()) {
                                binding.emptyIngrediente.isVisible = true
                                binding.loadingIngredientes.isVisible = false
                                ingredientesAdapter.submitList(null)
                            } else {
                                binding.loadingIngredientes.isVisible = false
                                binding.emptyIngrediente.isVisible = false
                                ingredientesAdapter.submitList(state.receita.ingredientes)
                            }
                            if(state.receita.modoDePreparoDTO == null || state.receita.modoDePreparoDTO.passos.isEmpty()) {
                                binding.emptyPassos.isVisible = true
                                binding.loadingPassosPreparo.isVisible = false
                                modoPreparoAdapter.submitList(null)
                            } else {
                                binding.loadingPassosPreparo.isVisible = false
                                binding.emptyPassos.isVisible = false
                                modoPreparoAdapter.submitList(state.receita.modoDePreparoDTO.passos.toList())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}