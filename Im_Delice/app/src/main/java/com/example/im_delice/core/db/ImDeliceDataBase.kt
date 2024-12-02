package com.example.im_delice.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.im_delice.core.daos.ComidaDao
import com.example.im_delice.core.daos.IngredienteDao
import com.example.im_delice.core.daos.CategoriaDao
import com.example.im_delice.core.daos.UsuarioDao
import com.example.im_delice.core.entities.Comida
import com.example.im_delice.core.entities.Ingrediente
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Usuario

@Database(
    entities = [Usuario::class, Comida::class, Categoria::class, Ingrediente::class],
    version = 1
)
abstract class ImDeliceDataBase : RoomDatabase() {
    /*
   genera una instancia de la interfaz UsuarioDao
    */
    abstract fun usuarioDao(): UsuarioDao
    /*
    genera una instancia de la interfaz ComidaDao
     */
    abstract fun ComidaDao(): ComidaDao


    abstract fun CategoriaDao(): CategoriaDao



    abstract fun IngredienteDao(): IngredienteDao
    


}