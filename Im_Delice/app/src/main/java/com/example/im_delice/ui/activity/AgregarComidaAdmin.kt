package com.example.im_delice.ui.activity

import android.app.AlertDialog
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
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Comida
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarComidaAdmin : AppCompatActivity() {
    private lateinit var etNameAdminComida: EditText
    private lateinit var etDescripcionComidaAdmin: EditText
    private lateinit var tvLinkFotoAdminComida: EditText
    private lateinit var etPrecioComida: EditText
    private lateinit var Radio_CategoriaComidaAdmin: RadioGroup
     private lateinit var EtMaximoIngediente: EditText
    private var dataSet: MutableList<Categoria> = mutableListOf()//vamos aa sacar los id para imprimur los nombres


    private lateinit var tvErrorAdminComida: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_comida_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Radio_CategoriaComidaAdmin = findViewById(R.id.Radio_CategoriaComidaAdmin)

        //elmiminar vistas por si hay algo guardado
        Radio_CategoriaComidaAdmin.removeAllViews()
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

                        Radio_CategoriaComidaAdmin.addView(radioButton)



                    }
                }
            }else{
                withContext(Dispatchers.Main) { // Cambio al hilo principal para mostrar el diálogo
                    AlertDialog.Builder(this@AgregarComidaAdmin)
                        .setTitle("Agregar Comida")
                        .setMessage("Primero debes de tener algun registro de la categoria")
                        .setPositiveButton("ok") { _, _ ->
                            finish()
                        }
                        .show()
                }


            }
        }

        etNameAdminComida = findViewById(R.id.etNameAdminComida)
        etDescripcionComidaAdmin = findViewById(R.id.etDescripcionComidaAdmin)
        tvLinkFotoAdminComida = findViewById(R.id.tvLinkFotoAdminComida)
        etPrecioComida = findViewById(R.id.etPrecioComida)
        EtMaximoIngediente=findViewById(R.id.EtMaximoIngediente)

        tvErrorAdminComida = findViewById(R.id.tvErrorAdminComida)

        val btnRegistrarse = findViewById<Button>(R.id.btnRegisterAdminComida)
        btnRegistrarse.setOnClickListener {
            cambiarEstadoVistas(false)
            ingresar()




        }
        findViewById<Button>(R.id.btnNuevoIntentoAdminComida).setOnClickListener {
            cambiarEstadoVistas(true)
            mostrarAlerta()
        }
    }
    fun ingresar() {
        val categoriaId = Radio_CategoriaComidaAdmin.checkedRadioButtonId

        val categoria = if (categoriaId != -1) {

            val selectedRadioButton = Radio_CategoriaComidaAdmin.findViewById<RadioButton>(categoriaId)
            if(selectedRadioButton==null){
                null
            }else{
                selectedRadioButton.text.toString() // Extraer el texto del RadioButton seleccionado

            }
        } else {

            null // O un valor por defecto si no se seleccionó ninguna categoría
        }
        if(EtMaximoIngediente.text.isEmpty()){
            EtMaximoIngediente.setText("0")

        }
        if (etNameAdminComida.text.toString() == "" || etDescripcionComidaAdmin.text.toString() == ""
            || tvLinkFotoAdminComida.text.toString() == ""||categoria==null||etPrecioComida.text.isEmpty()


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


        val agregar= Comida(
            id = 0,
            nombre = etNameAdminComida.text.toString().trim(),//trim quita espacios
            descripcion = etDescripcionComidaAdmin.text.toString(),
            fotoUrl = tvLinkFotoAdminComida.text.toString(),
            categoria = idcategoria,
            precio = etPrecioComida.text.toString().toDouble(),
            estado = "Disponible",
            Max_Ingredientes = EtMaximoIngediente.text.toString().toInt()




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
            val comidaDao = bd.ComidaDao()
            val existe = comidaDao.verificarComida(etNameAdminComida.text.toString().trim())//trim quita espacios)
// Cambiar al hilo principal para actualizar la UI

            if (existe > 0) {
                withContext(Dispatchers.Main) {
                    mostrarAlerta("Nombre de la comida ya esta registrado", View.VISIBLE)
                }
                return@launch
            }
            if ( comidaDao.InsertarComida(
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

        etNameAdminComida.isEnabled = estado
        etDescripcionComidaAdmin.isEnabled = estado
        tvLinkFotoAdminComida.isEnabled = estado
        etPrecioComida.isEnabled = estado
        Radio_CategoriaComidaAdmin.isEnabled = estado
        EtMaximoIngediente.isEnabled = estado

        findViewById<Button>(R.id.btnRegisterAdminComida).isEnabled = estado


    }
    /**nu
    Método para mostrar/ocultar la alerta
    @param mensaje Mensaje a mostrar, si se manda vacio indica que la alerta no mostrará texto
    @param visible Estado de la alerta, si viene vacio no se mostrará la alerta
     */
    fun mostrarAlerta(mensaje: String="", visible:Int= View.INVISIBLE) {

        tvErrorAdminComida.apply {
            text= mensaje
            visibility = visible
        }
        findViewById<Button>(R.id.btnNuevoIntentoAdminComida).visibility = visible

    }
}