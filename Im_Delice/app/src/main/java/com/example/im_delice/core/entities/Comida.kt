package com.example.im_delice.core.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Comidas",
    indices = [Index(value = ["nombre"], unique = true)])
data class Comida(
    @PrimaryKey(autoGenerate = true)  val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val fotoUrl: String,
    val categoria: Int, // "desayuno", "almuerzo", etc.
    val estado: String, // "habilitado" o "deshabilitado"
    val Max_Ingredientes:Int //Máximo de ingredientes
)
