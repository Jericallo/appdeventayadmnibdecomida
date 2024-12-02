package com.example.im_delice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegistrarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etfoto: EditText

    private lateinit var etPasswordConfirmar: EditText
    private lateinit var tvError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val layout =  inflater.inflate(R.layout.fragment_registrar, container, false)
        val btnAlreadyHaveAccount = layout.findViewById<Button>(R.id.btnAlreadyHaveAccount)
        btnAlreadyHaveAccount.setOnClickListener {
            requireActivity().onBackPressed() // Cierra el fragmento
        }
        layout.findViewById<Button>(R.id.btnNuevoIntento2).setOnClickListener {
            cambiarEstadoVistas(true)
            mostrarAlerta()
        }
        etName = layout.findViewById(R.id.etName)
        etEmail = layout.findViewById(R.id.etEmail)
        etPassword = layout.findViewById(R.id.etPassword)
        etPasswordConfirmar = layout.findViewById(R.id.etPasswordConfirmar)
        tvError = layout.findViewById(R.id.tvError2)
        etfoto = layout.findViewById(R.id.tvLinkFoto)
        val btnRegistrarse = layout.findViewById<Button>(R.id.btnRegister)
        btnRegistrarse.setOnClickListener {

            cambiarEstadoVistas(false)
            ingresar()


        }




        return layout


    }
    fun ingresar(){
        if(etName.text.toString()==""||etEmail.text.toString()==""||etPassword.text.toString()==""||etPasswordConfirmar.text.toString()==""
            ||etfoto.text.toString()==""){
            mostrarAlerta("Credenciales vacios", View.VISIBLE)
            return

        }
        if(etPassword.text.toString()!=etPasswordConfirmar.text.toString()){
            mostrarAlerta("Las contraseñas no coinciden", View.VISIBLE)
            return
        }
        val usuario= Usuario(
                id = 0,
                nombre = etName.text.toString(),
                email = etEmail.text.toString().trim(),
                contraseña = etPassword.text.toString(),
                rol = "Usuario",
                foto = etfoto.text.toString()



                // apellido_paterno = "Arevalo",
                // apellido_materno = "Navarro",
                //correo = "james.a.garfield@examplepetstore.com",
                //password = "123456"
            )
        //envia a buscar el registro de la tabla de usuarios
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                requireContext(),
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val usuarioDao = bd.usuarioDao()
            val existe = usuarioDao.verificarEmail(etEmail.text.toString().trim())
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
                        Toast.makeText(context, "Registro con exito", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed() // Cierra el fragmento

                    }


            }


        }



    }
    fun cambiarEstadoVistas(estado: Boolean) {
        etName.isEnabled = estado
        etEmail.isEnabled = estado
        etPassword.isEnabled = estado
        etPasswordConfirmar.isEnabled = estado
        requireView().findViewById<Button>(R.id.btnRegister).isEnabled = estado
        requireView().findViewById<Button>(R.id.btnAlreadyHaveAccount).isEnabled = estado


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
        requireView().findViewById<Button>(R.id.btnNuevoIntento2).visibility = visible

    }

}