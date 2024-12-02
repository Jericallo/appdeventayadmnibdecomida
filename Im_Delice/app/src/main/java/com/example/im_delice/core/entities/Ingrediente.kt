package com.example.im_delice.core.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredientes",
    indices = [Index(value = ["nombre"], unique = true)])
data class Ingrediente(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nombre: String,
    val Categoria:Int,
    val disponibilidad: Boolean, // Indica si está disponible
    val precioExtra: Double , // Costo adicional si aplica

    val foto:String
)
