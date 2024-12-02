package com.example.im_delice.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.im_delice.core.entities.Comida
import com.example.im_delice.core.entities.Usuario

@Dao
interface ComidaDao {
    /*
   obtener todos los Comida de la tabla
    */
    @Query("SELECT * FROM Comidas")
    fun ObtenerTodosComidas():List<Comida>
    /*
    obtener un usuario por su id
     */
    @Query("SELECT * FROM Comidas WHERE id = :id")
    fun ObtenerComidaPorId(id:Int): Comida
    /*
    insertar un usuario y regresara el id del usuario insertado
     */
    @Insert
    fun InsertarComida(obj: Comida):Long
    /*
    actualizar un usuario y regresara el numero de filas actualizadas
     */
    @Update
    fun ActualizarComida(obj: Comida):Int
    /*
    eliminar un usuario y regresara el numero de filas eliminadas
     */
    @Delete
    fun EliminarComida(obj: Comida):Int
    //Evalua si cuantos nombres hay en este tabla
    @Query("SELECT COUNT(*) FROM Comidas WHERE nombre = :nombre")
    fun verificarComida(nombre: String): Int




    /*
   Obtener todas las Comidas ordenadas por precio de mayor a menor
*/
    @Query("SELECT * FROM Comidas ORDER BY precio DESC")
    fun ObtenerComidasOrdenadasPorPrecioDesc(): List<Comida>



    /*
   Obtener todas las Comidas ordenadas por precio de menor a mayor
*/
    @Query("SELECT * FROM Comidas ORDER BY precio ASC")
    fun ObtenerComidasOrdenadasPorPrecioAsc(): List<Comida>



    /*
   Obtener todas las Comidas ordenadas por categoría de menor a mayor
*/
    @Query("SELECT * FROM Comidas ORDER BY categoria ASC")
    fun ObtenerComidasOrdenadasPorCategoriaAsc(): List<Comida>
    /*
   Obtener todas las Comidas ordenadas por estado (alfabéticamente)
*/
    @Query("SELECT * FROM Comidas ORDER BY estado ASC")
    fun ObtenerComidasOrdenadasPorEstadoAsc(): List<Comida>

    /*
       Obtener todas las Comidas ordenadas por nombre alfabéticamente (de la A a la Z)
    */
    @Query("SELECT * FROM Comidas ORDER BY nombre ASC")
    fun ObtenerComidasOrdenadasPorNombreAsc(): List<Comida>


}