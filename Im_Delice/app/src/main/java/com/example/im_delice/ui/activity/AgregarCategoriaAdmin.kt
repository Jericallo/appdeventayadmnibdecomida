package com.example.im_delice.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Usuario
import com.example.im_delice.ui.fragments.CategoriaFragment
import com.example.im_delice.ui.fragments.RegistrarFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarCategoriaAdmin : AppCompatActivity() {
    private lateinit var etNameAdminCategoria: EditText
    private lateinit var etDescripcionAdmin: EditText
    private lateinit var tvLinkFotoAdminCategoria: EditText
    private lateinit var tvError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_categoria_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etNameAdminCategoria = findViewById(R.id.etNameAdminCategoria)
        etDescripcionAdmin = findViewById(R.id.etDescripcionAdmin)
        tvLinkFotoAdminCategoria = findViewById(R.id.tvLinkFotoAdminCategoria)
        tvError = findViewById(R.id.tvErrorAdminCategoria)


        val btnRegistrarse = findViewById<Button>(R.id.btnRegisterAdminCategoria)
        btnRegistrarse.setOnClickListener {
            cambiarEstadoVistas(false)
            ingresar()




        }
        findViewById<Button>(R.id.btnNuevoIntentoAdminCategoria).setOnClickListener {
            cambiarEstadoVistas(true)
            mostrarAlerta()
        }
    }
    fun ingresar() {

        if (etNameAdminCategoria.text.toString() == "" || etDescripcionAdmin.text.toString() == ""
            || tvLinkFotoAdminCategoria.text.toString() == ""

        ) {
            mostrarAlerta("Credenciales vacios", View.VISIBLE)
            return

        }


        val agregar= Categoria(
            id = 0,                                     //quita espacios inciales y finales
            nombre = etNameAdminCategoria.text.toString().trim(),
            descripcion = etDescripcionAdmin.text.toString(),

            foto = tvLinkFotoAdminCategoria.text.toString()



            // apellido_paterno = "Arevalo",
            // apellido_materno = "Navarro",
            //correo = "james.a.garfield@examplepetstore.com",
            //password = "123456"
        )
        //envia a buscar el registro de la tabla de usuarios
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                applicationContext,
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val categoriaDao = bd.CategoriaDao()                                    //quitar espacios iniciales y dinales
            val existe = categoriaDao.verificarCategoria(etNameAdminCategoria.text.toString().trim())
// Cambiar al hilo principal para actualizar la UI

            if (existe > 0) {
                withContext(Dispatchers.Main) {
                    mostrarAlerta("Nombre de la categoria ya esta registrado", View.VISIBLE)
                }
                return@launch
            }
            if ( categoriaDao.insertarCategoria(
                    agregar
                )>0){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, "Registro con exito", Toast.LENGTH_SHORT).show()
                    //cierre de activity
                    finish() // Cierra la actividad actual


                }


            }


        }



    }
    fun cambiarEstadoVistas(estado: Boolean) {
        etNameAdminCategoria.isEnabled = estado
        etDescripcionAdmin.isEnabled = estado
        tvLinkFotoAdminCategoria.isEnabled = estado

        findViewById<Button>(R.id.btnRegisterAdminCategoria).isEnabled = estado


    }
    /**nu
    Método para mostrar/ocultar la alerta
    @param mensaje Mensaje a mostrar, si se manda vacio indica que la alerta no mostrará texto
    @param visible Estado de la alerta, si viene vacio no se mostrará la alerta
     */
    fun mostrarAlerta(mensaje: String="", visible:Int= View.INVISIBLE) {

        tvError.apply {
            text= mensaje
            visibility = visible
        }
        findViewById<Button>(R.id.btnNuevoIntentoAdminCategoria).visibility = visible

    }
}