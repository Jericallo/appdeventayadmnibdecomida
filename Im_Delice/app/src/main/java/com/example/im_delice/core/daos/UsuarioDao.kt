package com.example.im_delice.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.im_delice.core.entities.Usuario

@Dao
interface UsuarioDao {
    //Evalua si cuantos emails hay en este tabla
    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    fun verificarEmail(email: String): Int

    /*
     obtener todos los usuarios de la tabla
      */
    @Query("SELECT * FROM Usuarios")
    fun ObtenerTodosUsuarios():List<Usuario>
    //obtener los datos de un usuario por su email
    @Query("SELECT * FROM Usuarios WHERE email = :email")
    fun ObtenerUsuarioPorEmail(email:String):Usuario
    /*

    obtener un usuario por su id
     */
    @Query("SELECT * FROM Usuarios WHERE id = :id")
    fun ObtenerUsuarioPorId(id:Int):Usuario
    /*
    insertar un usuario y regresara el id del usuario insertado
     */
    @Insert
    fun InsertarUsuario(obj: Usuario):Long
    /*
    actualizar un usuario y regresara el numero de filas actualizadas
     */
    @Update
    fun ActualizarUsuario(obj: Usuario):Int
    /*
    eliminar un usuario y regresara el numero de filas eliminadas
     */
    @Delete
    fun EliminarUsuario(obj: Usuario):Int
}