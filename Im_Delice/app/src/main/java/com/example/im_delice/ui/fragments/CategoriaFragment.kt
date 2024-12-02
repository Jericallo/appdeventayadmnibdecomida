package com.example.im_delice.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Usuario
import com.example.im_delice.core.tools.RecyclerListadoCategoriaAdapter
import com.example.im_delice.core.tools.RecyclerListadoUsuarioAdapter
import com.example.im_delice.ui.activity.AgregarCategoriaAdmin
import com.example.im_delice.ui.activity.AgregarUsuarioAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoriaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoriaFragment : Fragment() {
    private lateinit var list: MutableList<Categoria>
    private lateinit var adapter: RecyclerListadoCategoriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =  inflater.inflate(R.layout.fragment_categoria, container, false)
        val rvListadoCategoria = layout.findViewById<RecyclerView>(R.id.rvListadoCategoria)
        // Inicializar lista y adaptador antes de usar
       // list = mutableListOf()
       // adapter = RecyclerListadoCategoriaAdapter(list, viewLifecycleOwner)
       // rvListadoCategoria.layoutManager = LinearLayoutManager(context)
        //rvListadoCategoria.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                requireContext(),
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()
            val categoriaDao = bd.CategoriaDao()
            val respuesta = categoriaDao.obtenerCategoria()
            // Cambiar al hilo principal para actualizar la UI
            if(respuesta.size > 0){
                val listDP:MutableList<Categoria> = mutableListOf()
                for (res in respuesta){
                    val pokemon = Categoria(
                        id = res.id,
                        nombre = res.nombre,
                        descripcion = res.descripcion,

                        foto = res.foto
                    )

                    listDP.add(pokemon)
                }
                adapter.setDataSet(listDP)
                withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
            }
        }
        rvListadoCategoria.layoutManager = LinearLayoutManager(context)

        list = mutableListOf()
        adapter = RecyclerListadoCategoriaAdapter(list, viewLifecycleOwner)
        rvListadoCategoria.adapter = adapter

        // Configuración del botón de agregar usuario
        val btnAgregarCategoriaAdmin = layout.findViewById<Button>(R.id.btnAgregarCategoriaAdmin)
        btnAgregarCategoriaAdmin.setOnClickListener {
            // Intent para abrir la actividad de agregar Caregoria
            val intent = Intent(requireContext(), AgregarCategoriaAdmin::class.java)
            startActivity(intent)


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
            val categoriaDao = bd.CategoriaDao()
            val respuesta = categoriaDao.obtenerCategoria()
            // Cambiar al hilo principal para actualizar la UI
            if(respuesta.size > 0){
                val listDP:MutableList<Categoria> = mutableListOf()
                for (res in respuesta){
                    val pokemon = Categoria(
                        id = res.id,
                        nombre = res.nombre,
                        descripcion = res.descripcion,

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
        Log.d("CategoriaFragment", "onResume llamado: recargando categorías")

    }


}