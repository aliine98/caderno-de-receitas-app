package br.com.aline.receitasapp.data.repository

import br.com.aline.receitasapp.data.dao.ReceitaDAO
import br.com.aline.receitasapp.data.entity.ReceitaCompleta
import br.com.aline.receitasapp.domain.model.IngredienteDTO
import br.com.aline.receitasapp.domain.model.ModoDePreparoDTO
import br.com.aline.receitasapp.domain.model.ReceitaCompletaDTO
import br.com.aline.receitasapp.domain.model.ReceitaDTO
import br.com.aline.receitasapp.domain.repository.ReceitaRepository
import br.com.aline.receitasapp.extensions.toDTO
import br.com.aline.receitasapp.extensions.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ReceitaRepositoryImpl(val dao: ReceitaDAO):ReceitaRepository {
    override suspend fun getAll(): Flow<List<ReceitaDTO>> = withContext(Dispatchers.IO) {
        dao.getAll().map {receitas ->
            receitas.map {
                it.toDTO()
            }
        }
    }

    override suspend fun addReceita(receita: ReceitaDTO) = withContext(Dispatchers.IO) {
        dao.addReceita(receita.toEntity())
    }

    override suspend fun updateReceita(receita: ReceitaDTO) = withContext(Dispatchers.IO) {
        dao.updateReceita(receita.toEntity())
    }

    override suspend fun deleteReceita(receita: ReceitaDTO) = withContext(Dispatchers.IO) {
        dao.deleteReceita(receita.toEntity())
    }

    override suspend fun getReceitaById(idReceita: Int): Flow<ReceitaCompletaDTO> = withContext(Dispatchers.IO) {
        dao.getReceitaById(idReceita).map { it.toDTO() }
    }

    override suspend fun addOrUpdateIngrediente(ingrediente: IngredienteDTO) = withContext(Dispatchers.IO) {
        dao.addOrUpdateIngrediente(ingrediente.toEntity())
    }

    override suspend fun deleteIngrediente(ingrediente: IngredienteDTO) = withContext(Dispatchers.IO) {
        dao.deleteIngrediente(ingrediente.toEntity())
    }

    override suspend fun addOrUpdateModoDePreparo(modoDePreparo: ModoDePreparoDTO) = withContext(Dispatchers.IO) {
        dao.addOrUpdateModoDePreparo(modoDePreparo.toEntity())
    }
}