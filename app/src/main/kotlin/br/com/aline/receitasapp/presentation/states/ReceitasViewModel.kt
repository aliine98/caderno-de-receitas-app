package br.com.aline.receitasapp.presentation.states

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.aline.receitasapp.domain.model.IngredienteDTO
import br.com.aline.receitasapp.domain.model.ModoDePreparoDTO
import br.com.aline.receitasapp.domain.model.ReceitaDTO
import br.com.aline.receitasapp.domain.repository.ReceitaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReceitasViewModel(private val repository:ReceitaRepository) : ViewModel() {
    /*val state: LiveData<ReceitasState> = liveData {
        emit(ReceitasState.Loading)
//        val state = try {
            val receitas = repository.getAll()
            if(receitas.isEmpty()) {
                ReceitasState.Empty
            } else {
                ReceitasState.Success(receitas)
            }
        } catch (e:Exception) {
            Log.e("erro_receita_vm",e.message.toString())
            ReceitasState.Error(e.message.toString())
        }
        emit(state)
    }*/
    private val _listaReceitasState = MutableStateFlow<ReceitasState>(ReceitasState.Loading)
    val listaReceitasState: StateFlow<ReceitasState> = _listaReceitasState
    private val _receitaCompletaState = MutableSharedFlow<ReceitaCompletaState>(1)
    val receitaCompetaState: SharedFlow<ReceitaCompletaState> = _receitaCompletaState

    init {
        getAll()
    }

    private fun getAll() = viewModelScope.launch {
        repository.getAll().flowOn(Dispatchers.Main)
            .catch {
                Log.d("receitas",it.stackTraceToString())
                _listaReceitasState.emit(ReceitasState.Error)
            }
            .collect { receitas ->
                if(receitas.isEmpty()) {
                    _listaReceitasState.emit(ReceitasState.Empty)
                } else {
                    _listaReceitasState.emit(ReceitasState.Success(receitas))
                }
            }

    }

    fun insertReceita(receitaDTO: ReceitaDTO) {
        viewModelScope.launch {
            repository.addReceita(receitaDTO)
        }
    }

    fun updateReceita(receitaDTO: ReceitaDTO) {
        viewModelScope.launch {
            repository.updateReceita(receitaDTO)
        }
    }

    fun deleteReceita(receitaDTO: ReceitaDTO) {
        viewModelScope.launch {
            repository.deleteReceita(receitaDTO)
        }
    }

    fun addOrUpdateIngrediente(ingrediente: IngredienteDTO) {
        viewModelScope.launch {
            repository.addOrUpdateIngrediente(ingrediente)
        }
    }

    fun deleteIngrediente(ingrediente: IngredienteDTO) {
        viewModelScope.launch {
            repository.deleteIngrediente(ingrediente)
        }
    }

    fun getReceita(id:Int) = viewModelScope.launch {
        repository.getReceitaById(id).flowOn(Dispatchers.Main)
            .onStart {
                _receitaCompletaState.emit(ReceitaCompletaState.Loading)
            }
            .catch {
                _receitaCompletaState.emit(ReceitaCompletaState.Error)
                Log.d("receitas",it.stackTraceToString())
            }
            .collect { receita ->
                Log.d("receitas",receita.toString())
                _receitaCompletaState.emit(ReceitaCompletaState.Success(receita))
            }
    }

    fun addOrUpdateModoPreparo(modoDePreparoDTO: ModoDePreparoDTO) {
        viewModelScope.launch {
            repository.addOrUpdateModoDePreparo(modoDePreparoDTO)
        }
    }
}