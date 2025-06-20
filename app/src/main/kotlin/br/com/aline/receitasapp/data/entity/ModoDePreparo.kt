package br.com.aline.receitasapp.data.entity

import androidx.room.*

@Entity(foreignKeys = [
    ForeignKey(
        entity = Receita::class,
        parentColumns = ["id"],
        childColumns = ["id_receita"],
        onDelete = ForeignKey.CASCADE
    )
])
data class ModoDePreparo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val passos: List<String>,
    @ColumnInfo(name = "id_receita") val idReceita:Int
)
