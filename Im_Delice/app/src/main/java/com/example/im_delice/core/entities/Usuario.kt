package com.example.im_delice.core.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

//@Index(value = ["email"], unique = true):

//Crea un índice en la columna email.
//La propiedad unique = true asegura que no se puedan insertar valores duplicados en la columna email.
//tableName = "usuarios":
//Establece el nombre de la tabla como usuarios.

@Entity(tableName = "Usuarios",
    indices = [Index(value = ["email"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true)  val id: Int,
    val nombre: String,
    val email: String,
    val contraseña: String,
    val rol: String, // "cliente" o "administrador"
    val foto: String

)
