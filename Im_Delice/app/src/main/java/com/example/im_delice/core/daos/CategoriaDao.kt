package com.example.im_delice.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.im_delice.core.entities.Categoria

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM Categorias")
    fun obtenerCategoria(): List<Categoria>


    //Evalua si cuantos nombres hay en este tabla
    @Query("SELECT * FROM Categorias WHERE nombre = :nombre")
    fun BuscarPorNombre(nombre: String): List<Categoria>


    @Query("SELECT * FROM Categorias WHERE id = :id")
    fun ObtenerCategoriaPorId(id:Int):Categoria

    @Insert
    fun insertarCategoria(obj: Categoria):Long

    /*
   actualizar un usuario y regresara el numero de filas actualizadas
    */
    @Update
    fun ActualizarCategoria(obj: Categoria):Int


    @Delete
    fun eliminarCategoria(obj: Categoria):Int


    //Evalua si cuantos nombres hay en este tabla
    @Query("SELECT COUNT(*) FROM Categorias WHERE nombre = :nombre")
    fun verificarCategoria(nombre: String): Int




}