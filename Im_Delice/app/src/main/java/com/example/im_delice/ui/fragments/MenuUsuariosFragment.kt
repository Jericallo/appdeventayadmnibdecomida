package com.example.im_delice.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Usuario
import com.example.im_delice.core.tools.RecyclerListadoUsuarioAdapter
import com.example.im_delice.ui.activity.AgregarUsuarioAdmin
import com.example.im_delice.ui.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuUsuariosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuUsuariosFragment : Fragment() {
    private lateinit var list: MutableList<Usuario>
    private lateinit var adapter: RecyclerListadoUsuarioAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =  inflater.inflate(R.layout.menu_usuarios_fragment, container, false)

        //Evaluacion si se mete al usuario cuando se mete
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.cuentas_pref_compratidas), Context.MODE_PRIVATE)
        // val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val correo = sharedPref?.getString(getString(R.string.correo_pref), "")
        val contra = sharedPref?.getString(getString(R.string.contraseña_pref), "")
        val rvListadoUsuarios = layout.findViewById<RecyclerView>(R.id.rvListadoUsuarios)
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                requireContext(),
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val usuarioDao = bd.usuarioDao()
            val respuesta = usuarioDao.ObtenerTodosUsuarios()
            // Cambiar al hilo principal para actualizar la UI
            if(respuesta.size > 0){
                val listDP:MutableList<Usuario> = mutableListOf()
                for (res in respuesta){
                    val pokemon = Usuario(
                        id = res.id,
                        nombre = res.nombre,
                        email = res.email,
                        contraseña = res.contraseña,

                        rol = res.rol,
                        foto = res.foto
                    )

                    listDP.add(pokemon)
                }
                adapter.setDataSet(listDP)
                withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
            }
        }

        rvListadoUsuarios.layoutManager = LinearLayoutManager(context)

        list = mutableListOf()
        adapter = RecyclerListadoUsuarioAdapter(list, viewLifecycleOwner)
        rvListadoUsuarios.adapter = adapter

        // Configuración del botón de agregar usuario
        val btnAgregarUsuario = layout.findViewById<Button>(R.id.btnAgregarUsuarioAdmin)
        btnAgregarUsuario.setOnClickListener {
            // Intent para abrir la actividad de agregar usuario
            val intent = Intent(requireContext(), AgregarUsuarioAdmin::class.java)
            startActivity(intent)


        }
        // Configuración del botón de agregar usuario
        val btnCerrarSesion = layout.findViewById<Button>(R.id.CerraraesionAdmin)
        btnCerrarSesion.setOnClickListener {
            //Guardar el correo en las preferencias compartidas
            val sharedPref = activity?.getSharedPreferences(
                getString(R.string.cuentas_pref_compratidas), Context.MODE_PRIVATE
            )

            sharedPref?.edit()?.clear()?.apply() // Limpiar las preferencias compartidas

            // Intent para abrir la actividad de agregar usuario
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpiar el stack de actividades

            startActivity(intent)

            // Mostrar mensaje de cierre de sesión exitoso
            Toast.makeText(requireContext(), "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()

        }
        // Inflate the layout for this fragment
        return layout
    }
    private fun cargarCategorias() {
        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                requireContext(),
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val usuarioDao = bd.usuarioDao()
            val respuesta = usuarioDao.ObtenerTodosUsuarios()
            // Cambiar al hilo principal para actualizar la UI
            if(respuesta.size > 0){
                val listDP:MutableList<Usuario> = mutableListOf()
                for (res in respuesta){
                    val pokemon = Usuario(
                        id = res.id,
                        nombre = res.nombre,
                        email = res.email,
                        contraseña = res.contraseña,

                        rol = res.rol,
                        foto = res.foto
                    )

                    listDP.add(pokemon)
                }
                adapter.setDataSet(listDP)
                withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
            }
        }



    }
    override fun onResume() {
        super.onResume()
        // Recargar categorías al volver al fragmento
        cargarCategorias()
        Log.d("CategoriaFragment", "onResume llamado: recargando Usuarios")

    }




}