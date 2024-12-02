package com.example.im_delice.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Comida
import com.example.im_delice.core.entities.Ingrediente
import com.example.im_delice.core.tools.RecyclerListadoComidaAdapter
import com.example.im_delice.core.tools.RecyclerListadoIngredienteAdapter
import com.example.im_delice.ui.activity.AgregarComidaAdmin
import com.example.im_delice.ui.activity.AgregarIngredienteAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IngrendientesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IngrendientesFragment : Fragment() {
    private lateinit var list: MutableList<Ingrediente>
    private lateinit var adapter: RecyclerListadoIngredienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =  inflater.inflate(R.layout.fragment_ingrendientes, container, false)

        val rvListadoIngrediente= layout.findViewById<RecyclerView>(R.id.rvListadoIngrediente)


        // Configuración del botón de agregar usuario
        val btnAgregarIngrediente = layout.findViewById<Button>(R.id.btnAgregarIngrefienteAdmin)
        btnAgregarIngrediente.setOnClickListener {
            //  Intent para abrir la actividad de agregar usuario
            val intent = Intent(requireContext(), AgregarIngredienteAdmin::class.java)
            startActivity(intent)


        }



        val Filtracion =layout.findViewById<RadioGroup>(R.id.GrupoFiltracionIngredientes)
        // Configura el listener
        Filtracion.setOnCheckedChangeListener { _, checkedId ->

            // Ejecuta acciones según la opción seleccionada
            when (checkedId) {
                R.id.RadioOrdenarPorNombreIngrediente ->{
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val ingredienteDao = bd.IngredienteDao()
                        val respuesta = ingredienteDao.ObtenerIngredientesOrdenadosPorNombreAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Ingrediente> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Ingrediente(
                                    id = res.id,
                                    nombre = res.nombre,
                                    foto = res.foto,
                                    disponibilidad = res.disponibilidad,
                                    Categoria = res.Categoria,
                                    precioExtra = res.precioExtra







                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }
                    rvListadoIngrediente.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoIngredienteAdapter(list, viewLifecycleOwner)
                    rvListadoIngrediente.adapter = adapter

                }
                R.id.RadioOrdenarPorCategoriaIngrediente -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val ingredienteDao = bd.IngredienteDao()
                        val respuesta = ingredienteDao.ObtenerIngredientesOrdenadosPorCategoriaAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Ingrediente> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Ingrediente(
                                    id = res.id,
                                    nombre = res.nombre,
                                    foto = res.foto,
                                    disponibilidad = res.disponibilidad,
                                    Categoria = res.Categoria,
                                    precioExtra = res.precioExtra







                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }
                    rvListadoIngrediente.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoIngredienteAdapter(list, viewLifecycleOwner)
                    rvListadoIngrediente.adapter = adapter



                }
                R.id.RadioOrdenarPorEstadoIngrediente -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val ingredienteDao = bd.IngredienteDao()
                        val respuesta = ingredienteDao.ObtenerIngredientesOrdenadosPorDisponibilidad()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Ingrediente> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Ingrediente(
                                    id = res.id,
                                    nombre = res.nombre,
                                    foto = res.foto,
                                    disponibilidad = res.disponibilidad,
                                    Categoria = res.Categoria,
                                    precioExtra = res.precioExtra







                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }
                    rvListadoIngrediente.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoIngredienteAdapter(list, viewLifecycleOwner)
                    rvListadoIngrediente.adapter = adapter



                }



                else -> {
                    //se carga normal
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val ingredienteDao = bd.IngredienteDao()
                        val respuesta = ingredienteDao.obtenerIngrendientes()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Ingrediente> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Ingrediente(
                                    id = res.id,
                                    nombre = res.nombre,
                                    foto = res.foto,
                                    disponibilidad = res.disponibilidad,
                                    Categoria = res.Categoria,
                                    precioExtra = res.precioExtra







                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }
                    rvListadoIngrediente.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoIngredienteAdapter(list, viewLifecycleOwner)
                    rvListadoIngrediente.adapter = adapter


                }
            }
        }



        // Inflate the layout for this fragment
        return layout
    }



    override fun onResume() {
        super.onResume()

        // Restaurar el estado predeterminado (sin selección)
        val Filtracion2 = view?.findViewById<RadioGroup>(R.id.GrupoFiltracionIngredientes)
        Filtracion2?.clearCheck() // Limpia la selección del RadioGroup
        //    cargarcomida()
        Log.d("CategoriaFragment onResume", "HOLAAAAAAAAAAAAAAAAAA")






    }


}