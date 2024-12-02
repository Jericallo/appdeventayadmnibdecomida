package com.example.im_delice.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnIngresar).setOnClickListener {
            cambiarEstadoVistas(false)
            ingresar()
        }
        findViewById<Button>(R.id.btnNuevoIntento).setOnClickListener {
            cambiarEstadoVistas(true)
            mostrarAlerta()
        }
        findViewById<Button>(R.id.btnCrearCuenta).setOnClickListener {
            val intent = Intent(this, MainFragemto::class.java)
            intent.putExtra("mostrarRegistro", true) // Agrega el extra
            startActivity(intent)
        }
        iniciarBaseDeDatos()


        //Evaluacion si se mete al usuario cuando se mete
        val sharedPref = this?.getSharedPreferences(
            getString(R.string.cuentas_pref_compratidas), Context.MODE_PRIVATE)
        // val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val correo = sharedPref?.getString(getString(R.string.correo_pref), "")
        findViewById<EditText>(R.id.txtCorreo).setText(correo)
        val contra = sharedPref?.getString(getString(R.string.contraseña_pref), "")
        findViewById<EditText>(R.id.txtContraseña).setText(contra)
        val rol = sharedPref?.getString(getString(R.string.rol_pref), "")

        if (findViewById<EditText>(R.id.txtContraseña).text.toString()!=""
            &&findViewById<EditText>(R.id.txtCorreo).text.toString()!=""
            ){
            //Validamos los roles
                if(rol=="Admin"){
                    // Redirigir al menú de usuarios
                    val intent = Intent(this@MainActivity, Menu_Usuarios::class.java)
                    startActivity(intent)
                }else{
                    //hola
                }


        }

    }
    private fun iniciarBaseDeDatos() {
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                applicationContext,
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val usuarioDao = bd.usuarioDao()
       //     val usuarios = usuarioDao.ObtenerTodosUsuarios() // Método que consulta los usuarios

            val existe = usuarioDao.verificarEmail("andres.arevalonavarro@gmail.com")
            if (existe > 0) {
                Log.e("Validation", "El correo ya está registrado")
            } else {
                if ( usuarioDao.InsertarUsuario(
                        Usuario(
                            id = 0,
                            nombre = "Andres",
                            email = "andres.arevalonavarro@gmail.com",
                            contraseña = "1229",
                            rol = "Admin",
                            foto ="https://scontent.fgdl3-1.fna.fbcdn.net/v/t39.30808-6/465266607_3860735847502315_4807496057620899440_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=fidTZAAg2jQQ7kNvgF2VUl-&_nc_zt=23&_nc_ht=scontent.fgdl3-1.fna&_nc_gid=AiKjmq7knPd9HK_h7rpFY9k&oh=00_AYDDFig2iYwde-35KzmxyWssw1EK1xl8XskrYDu-CpuvHw&oe=674AD806"
                            // apellido_paterno = "Arevalo",
                            // apellido_materno = "Navarro",
                            //correo = "james.a.garfield@examplepetstore.com",
                            //password = "123456"
                        )
                    )>0){
                    val listadoUsuarios:List<Usuario> = usuarioDao.ObtenerTodosUsuarios()
                    if (listadoUsuarios.size>0){
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext,listadoUsuarios.size.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

                }

            }



        }

    }
    fun cambiarEstadoVistas(estado: Boolean) {
        findViewById<EditText>(R.id.txtCorreo).isEnabled = estado
        findViewById<EditText>(R.id.txtContraseña).isEnabled = estado
        findViewById<Button>(R.id.btnIngresar).isEnabled = estado
        findViewById<Button>(R.id.btnCrearCuenta).isEnabled = estado
    }
    fun ingresar(){
        if(findViewById<EditText>(R.id.txtCorreo).text.toString()==""||findViewById<EditText>(R.id.txtContraseña).text.toString()==""){
            mostrarAlerta("Correo vacío o contraseña vacio", View.VISIBLE)
            return
        }
        //envia a buscar el registro de la tabla de usuarios
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                applicationContext,
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val usuarioDao = bd.usuarioDao()
            val existe = usuarioDao.verificarEmail(findViewById<EditText>(R.id.txtCorreo).text.toString())

            val usuario = usuarioDao.ObtenerUsuarioPorEmail(findViewById<EditText>(R.id.txtCorreo).text.toString())
            // Cambiar al hilo principal para actualizar la UI
            withContext(Dispatchers.Main) {
                if (existe > 0) {
                    Log.e("Exitoso", "El correo encontrado")
                    if (usuario.contraseña == findViewById<EditText>(R.id.txtContraseña).text.toString()) {
                        //Guardar el correo en las preferencias compartidas
                        val sharedPref = getSharedPreferences(
                            getString(R.string.cuentas_pref_compratidas), Context.MODE_PRIVATE)

                        //val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
                        with (sharedPref.edit()) {
                            // putString(getString(R.string.correo_pref), findViewById<EditText>(R.id.txtCorreo).text.toString())
                            putString(getString(R.string.correo_pref), findViewById<EditText>(R.id.txtCorreo).text.toString())
                            putString(getString(R.string.contraseña_pref), findViewById<EditText>(R.id.txtContraseña).text.toString())
                            putString(getString(R.string.rol_pref), usuario.rol)
                            // putString("Apellido","Arevalo")
                            //putInt("Edad:", 25)
                            // putFloat("Peso", 70.5f))


                            // apply()
                            apply()
                        }
                        if(usuario.rol=="Admin"){
                            // Redirigir al menú de usuarios
                            val intent = Intent(this@MainActivity, Menu_Usuarios::class.java)
                            startActivity(intent)
                        }else{
                            //hola
                        }

                    }else{
                        mostrarAlerta("Contraseña incorrecta", View.VISIBLE)
                    }

                } else {
                    mostrarAlerta("Correo no registrado o incorrecto", View.VISIBLE)
                }
            }



        }





    }
    /**nu
    Método para mostrar/ocultar la alerta
    @param mensaje Mensaje a mostrar, si se manda vacio indica que la alerta no mostrará texto
    @param visible Estado de la alerta, si viene vacio no se mostrará la alerta
     */
    fun mostrarAlerta(mensaje: String="", visible:Int= View.INVISIBLE) {
        findViewById<TextView>(R.id.tvError).apply {
            text = mensaje
            visibility = visible
        }
        findViewById<Button>(R.id.btnNuevoIntento).visibility = visible
    }


}