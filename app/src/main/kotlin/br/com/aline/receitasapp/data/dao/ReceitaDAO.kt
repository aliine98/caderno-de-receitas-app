package br.com.aline.receitasapp.data.dao

import androidx.room.*
import br.com.aline.receitasapp.data.entity.Ingrediente
import br.com.aline.receitasapp.data.entity.ModoDePreparo
import br.com.aline.receitasapp.data.entity.Receita
import br.com.aline.receitasapp.data.entity.ReceitaCompleta
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceitaDAO {
    @Query("Select * from receita")
    fun getAll(): Flow<List<Receita>>

    @Insert
    fun addReceita(receita: Receita)

    @Update
    fun updateReceita(receita: Receita)

    @Delete
    fun deleteReceita(receita: Receita)

    @Transaction
    @Query("Select * from receita where id=:receitaId")
    fun getReceitaById(receitaId: Int): Flow<ReceitaCompleta>

    @Upsert
    fun addOrUpdateIngrediente(ingrediente: Ingrediente)

    @Delete
    fun deleteIngrediente(ingrediente: Ingrediente)

    @Upsert
    fun addOrUpdateModoDePreparo(modoDePreparo: ModoDePreparo)
}