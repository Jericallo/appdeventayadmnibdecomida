package com.example.im_delice.core.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Categorias",

    indices = [Index(value = ["nombre"], unique = true)])
data class Categoria(
    @PrimaryKey(autoGenerate = true)  val id: Int,
    val nombre: String,
    val foto: String,
    val descripcion:String

)
