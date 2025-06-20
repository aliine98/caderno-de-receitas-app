package br.com.aline.receitasapp.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ReceitaCompleta(
    @Embedded val receita: Receita,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_receita"
    )
    val ingredientes: List<Ingrediente>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_receita"
    )
    val modoDePreparo: ModoDePreparo?
)