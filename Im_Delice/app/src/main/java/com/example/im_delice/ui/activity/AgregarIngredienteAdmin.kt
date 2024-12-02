package com.example.im_delice.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Comida
import com.example.im_delice.core.entities.Ingrediente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarIngredienteAdmin : AppCompatActivity() {
    private lateinit var etNameAdminIngedinte: EditText
    private lateinit var etPrecioExtraAdmin: EditText
    private lateinit var tvLinkFotoAdminIngrediente: EditText
    private lateinit var Radio_CategoriaIngredienteAdmin: RadioGroup
    private var dataSet: MutableList<Categoria> = mutableListOf()

    private lateinit var tvErrorAdminIngrediente: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_ingrediente_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Radio_CategoriaIngredienteAdmin = findViewById(R.id.Radio_CategoriaIngredienteAdmin)

        //elmiminar vistas por si hay algo guardado
        Radio_CategoriaIngredienteAdmin.removeAllViews()
        //imprime las categorias
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                applicationContext,
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val categoriaDao = bd.CategoriaDao()
            val respuesta = categoriaDao.obtenerCategoria()
            //enpieza a imprimir las categorias
            if(respuesta.size > 0){
                val listDP:MutableList<Categoria> = mutableListOf()
                for (res in respuesta){
                    val pokemon = Categoria(
                        id = res.id,
                        nombre = res.nombre,
                        descripcion = res.descripcion,

                        foto = res.foto
                    )
                    dataSet.add(pokemon)

                    listDP.add(pokemon)
                }
                withContext(Dispatchers.Main){
                    for (categoria in listDP ){
                        val radioButton = RadioButton(applicationContext)
                        radioButton.text = categoria.nombre
                        radioButton.id = View.generateViewId() // Asigna un ID único al RadioButton

                        Radio_CategoriaIngredienteAdmin.addView(radioButton)



                    }
                }
            }else{
                withContext(Dispatchers.Main) { // Cambio al hilo principal para mostrar el diálogo
                    AlertDialog.Builder(this@AgregarIngredienteAdmin)
                        .setTitle("Agregar Ingrediente")
                        .setMessage("Primero debes de tener algun registro de la categoria")
                        .setPositiveButton("ok") { _, _ ->
                            finish()
                        }
                        .show()
                }


            }
        }

        etNameAdminIngedinte = findViewById(R.id.etNameAdminIngedinte)
        etPrecioExtraAdmin = findViewById(R.id.etPrecioExtraAdmin)
        tvLinkFotoAdminIngrediente = findViewById(R.id.tvLinkFotoAdminIngrediente)

        tvErrorAdminIngrediente = findViewById(R.id.tvErrorAdminIngrediente)

        val btnRegistrarse = findViewById<Button>(R.id.btnRegisterAdminIngrediente)
        btnRegistrarse.setOnClickListener {
            cambiarEstadoVistas(false)
            ingresar()




        }
        findViewById<Button>(R.id.btnNuevoIntentoAdminIngrediente).setOnClickListener {
            cambiarEstadoVistas(true)
            mostrarAlerta()
        }
    }
    fun ingresar() {
        val categoriaId = Radio_CategoriaIngredienteAdmin.checkedRadioButtonId

        val categoria = if (categoriaId != -1) {

            val selectedRadioButton = Radio_CategoriaIngredienteAdmin.findViewById<RadioButton>(categoriaId)
            if(selectedRadioButton==null){
                null
            }else{
                selectedRadioButton.text.toString() // Extraer el texto del RadioButton seleccionado

            }
        } else {

            null // O un valor por defecto si no se seleccionó ninguna categoría
        }

        if (etNameAdminIngedinte.text.toString() == ""
            || tvLinkFotoAdminIngrediente.text.toString() == ""||categoria==null||etPrecioExtraAdmin.text.isEmpty()


        ) {
            mostrarAlerta("Credenciales vacios", View.VISIBLE)
            return

        }
//sacamos el id de la cateforia q fue cambiada
        var idcategoria=0
       for (buscar in dataSet){
           if(categoria==buscar.nombre){
               idcategoria=buscar.id

           }
       }

        val agregar= Ingrediente(
            id = 0,
            nombre = etNameAdminIngedinte.text.toString().trim(),//trim quita espacios
            foto = tvLinkFotoAdminIngrediente.text.toString(),
            Categoria = idcategoria,



            disponibilidad = true,
            precioExtra = etPrecioExtraAdmin.text.toString().toDouble()




            // apellido_paterno = "Arevalo",
            // apellido_materno = "Navarro",
            //correo = "james.a.garfield@examplepetstore.com",
            //password = "123456"
        )
        //envia a buscar el registro de la tabla de ingredientes
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                applicationContext,
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val IngredienreDao = bd.IngredienteDao()
            val existe = IngredienreDao.verificarIngrediente(etNameAdminIngedinte.text.toString().trim())//trim quita espacios)
// Cambiar al hilo principal para actualizar la UI

            if (existe > 0) {
                withContext(Dispatchers.Main) {
                    mostrarAlerta("Nombre de la Ingrediente ya esta registrado", View.VISIBLE)
                }
                return@launch
            }
            if ( IngredienreDao.InsertarIngrediente(
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

        etNameAdminIngedinte.isEnabled = estado
        etPrecioExtraAdmin.isEnabled = estado
        tvLinkFotoAdminIngrediente.isEnabled = estado
        Radio_CategoriaIngredienteAdmin.isEnabled = estado

        findViewById<Button>(R.id.btnRegisterAdminIngrediente).isEnabled = estado


    }
    /**nu
    Método para mostrar/ocultar la alerta
    @param mensaje Mensaje a mostrar, si se manda vacio indica que la alerta no mostrará texto
    @param visible Estado de la alerta, si viene vacio no se mostrará la alerta
     */
    fun mostrarAlerta(mensaje: String="", visible:Int= View.INVISIBLE) {

        tvErrorAdminIngrediente.apply {
            text= mensaje
            visibility = visible
        }
        findViewById<Button>(R.id.btnNuevoIntentoAdminIngrediente).visibility = visible

    }
}