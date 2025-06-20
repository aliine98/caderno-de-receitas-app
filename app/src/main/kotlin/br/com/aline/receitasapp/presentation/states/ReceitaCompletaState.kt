package br.com.aline.receitasapp.presentation.states

import br.com.aline.receitasapp.domain.model.ReceitaCompletaDTO

interface ReceitaCompletaState {
    data object Loading: ReceitaCompletaState
    data class Success(val receita: ReceitaCompletaDTO): ReceitaCompletaState
    data object Error: ReceitaCompletaState
}