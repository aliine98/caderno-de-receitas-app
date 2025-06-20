package br.com.aline.receitasapp.presentation.states

import br.com.aline.receitasapp.domain.model.ReceitaDTO

sealed interface ReceitasState {
    data object Empty : ReceitasState
    data object Loading: ReceitasState
    data class Success(val receitas:List<ReceitaDTO>): ReceitasState
    data object Error: ReceitasState
}