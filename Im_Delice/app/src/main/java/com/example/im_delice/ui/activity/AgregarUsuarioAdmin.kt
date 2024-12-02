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
import com.example.im_delice.core.entities.Usuario
import com.example.im_delice.ui.fragments.MenuUsuariosFragment
import com.example.im_delice.ui.fragments.RegistrarFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarUsuarioAdmin : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etfoto: EditText
    private lateinit var etrol: RadioGroup
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_usuario_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etName = findViewById(R.id.etNameAdmin)
        etEmail = findViewById(R.id.etEmailAdmin)
        etPassword = findViewById(R.id.etPasswordAdmin)
        tvError = findViewById(R.id.tvErrorAdmin)
        etfoto = findViewById(R.id.tvLinkFotoAdmin)
        etrol = findViewById(R.id.Radio_rolAdmin)

        val btnRegistrarse = findViewById<Button>(R.id.btnRegisterAdmin)
        btnRegistrarse.setOnClickListener {
            cambiarEstadoVistas(false)
            ingresar()




        }
        findViewById<Button>(R.id.btnNuevoIntentoAdmin).setOnClickListener {
            cambiarEstadoVistas(true)
            mostrarAlerta()
        }
    }
    fun ingresar() {
        val selectedId = etrol.checkedRadioButtonId

        if (etName.text.toString() == "" || etEmail.text.toString() == "" || etPassword.text.toString() == ""
            || etfoto.text.toString() == "" || selectedId == -1
        ) {
            mostrarAlerta("Credenciales vacios", View.VISIBLE)
            return

        }
        // Obtén el texto del RadioButton seleccionado
        val selectedRadioButton: RadioButton = findViewById(selectedId)
        val rol = selectedRadioButton.text.toString() // Asignar rol seleccionado como texto


        val usuario= Usuario(
            id = 0,
            nombre = etName.text.toString(),
            email = etEmail.text.toString().trim(),//trim quita espacios
            contraseña = etPassword.text.toString(),
            rol = rol,
            foto = etfoto.text.toString()



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
            val usuarioDao = bd.usuarioDao()
            val existe = usuarioDao.verificarEmail(etEmail.text.toString().trim())//trim quita espacios
// Cambiar al hilo principal para actualizar la UI

            if (existe > 0) {
                withContext(Dispatchers.Main) {
                    mostrarAlerta("Correo ya esta registrado", View.VISIBLE)
                }
                return@launch
            }
            if ( usuarioDao.InsertarUsuario(
                    usuario
                )>0){
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, "Registro con exito", Toast.LENGTH_SHORT).show()
                    MenuUsuariosFragment()
                    //cierre de activity
                    finish() // Cierra la actividad actual


                }


            }


        }



    }
    fun cambiarEstadoVistas(estado: Boolean) {
        etName.isEnabled = estado
        etEmail.isEnabled = estado
        etPassword.isEnabled = estado
        etrol.isEnabled = estado
        etfoto.isEnabled = estado
        findViewById<Button>(R.id.btnRegisterAdmin).isEnabled = estado


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
      findViewById<Button>(R.id.btnNuevoIntentoAdmin).visibility = visible

    }
}