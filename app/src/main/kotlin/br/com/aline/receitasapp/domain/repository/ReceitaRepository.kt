package br.com.aline.receitasapp.domain.repository

import br.com.aline.receitasapp.data.entity.Ingrediente
import br.com.aline.receitasapp.data.entity.ReceitaCompleta
import br.com.aline.receitasapp.domain.model.IngredienteDTO
import br.com.aline.receitasapp.domain.model.ModoDePreparoDTO
import br.com.aline.receitasapp.domain.model.ReceitaCompletaDTO
import br.com.aline.receitasapp.domain.model.ReceitaDTO
import kotlinx.coroutines.flow.Flow

interface ReceitaRepository {
    suspend fun getAll(): Flow<List<ReceitaDTO>>
    suspend fun addReceita(receita:ReceitaDTO)
    suspend fun updateReceita(receita: ReceitaDTO)
    suspend fun deleteReceita(receita: ReceitaDTO)
    suspend fun getReceitaById(idReceita:Int): Flow<ReceitaCompletaDTO>
    suspend fun addOrUpdateIngrediente(ingrediente: IngredienteDTO)
    suspend fun deleteIngrediente(ingrediente: IngredienteDTO)
    suspend fun addOrUpdateModoDePreparo(modoDePreparo: ModoDePreparoDTO)
}