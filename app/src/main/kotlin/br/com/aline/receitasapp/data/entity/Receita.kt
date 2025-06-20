package br.com.aline.receitasapp.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Receita(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val titulo: String,
    @ColumnInfo val capa: Bitmap?
)
