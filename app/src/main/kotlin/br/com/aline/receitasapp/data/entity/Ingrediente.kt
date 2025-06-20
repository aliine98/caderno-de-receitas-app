package br.com.aline.receitasapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Receita::class,
        parentColumns = ["id"],
        childColumns = ["id_receita"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Ingrediente(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo val nome:String,
    @ColumnInfo(name = "id_receita") val idReceita:Int
)