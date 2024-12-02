package com.example.im_delice.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.im_delice.core.entities.Ingrediente
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Usuario

@Dao
interface IngredienteDao {

    @Insert
    fun InsertarIngrediente(obj: Ingrediente):Long




    @Query("SELECT * FROM Ingredientes")
    fun obtenerIngrendientes(): List<Ingrediente>

    //Evalua si cuantos emails hay en este tabla
    @Query("SELECT COUNT(*) FROM Ingredientes WHERE nombre = :nombre")
    fun verificarIngrediente(nombre: String): Int
    /*
    obtener un Ingrediente por su id
    */
    @Query("SELECT * FROM Ingredientes WHERE id = :id")
    fun ObtenerIngredientePorId(id:Int): Ingrediente



@Update
fun ActualizarIngrediente(obj: Ingrediente):Int
/*
eliminar un oingresiente y regresara el numero de filas eliminadas
 */
@Delete
fun EliminarIngrediente(obj: Ingrediente):Int


    /*
       Obtener todos los ingredientes ordenados por nombre alfabéticamente (de la A a la Z)
    */
    @Query("SELECT * FROM Ingredientes ORDER BY nombre ASC")
    fun ObtenerIngredientesOrdenadosPorNombreAsc(): List<Ingrediente>


    /*
   Obtener todos los ingredientes ordenados por categoría de menor a mayor
*/
    @Query("SELECT * FROM Ingredientes ORDER BY Categoria ASC")
    fun ObtenerIngredientesOrdenadosPorCategoriaAsc(): List<Ingrediente>


    /*
   Obtener todos los ingredientes ordenados por disponibilidad (disponibles primero)
*/
    @Query("SELECT * FROM Ingredientes ORDER BY disponibilidad DESC")
    fun ObtenerIngredientesOrdenadosPorDisponibilidad(): List<Ingrediente>



}