package br.com.aline.receitasapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.aline.receitasapp.data.dao.ReceitaDAO
import br.com.aline.receitasapp.data.entity.Ingrediente
import br.com.aline.receitasapp.data.entity.ModoDePreparo
import br.com.aline.receitasapp.data.entity.Receita

@Database(entities = [Receita::class, Ingrediente::class, ModoDePreparo::class], version = 3)
@TypeConverters(Converters::class)
abstract class ReceitasDB: RoomDatabase() {
    abstract fun receitaDAO(): ReceitaDAO
}