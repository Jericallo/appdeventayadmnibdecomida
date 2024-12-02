package com.example.im_delice.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Comida
import com.example.im_delice.core.entities.Usuario
import com.example.im_delice.core.tools.RecyclerListadoComidaAdapter
import com.example.im_delice.core.tools.RecyclerListadoUsuarioAdapter
import com.example.im_delice.ui.activity.AgregarComidaAdmin
import com.example.im_delice.ui.activity.AgregarUsuarioAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ComidasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComidasFragment : Fragment() {
    private lateinit var list: MutableList<Comida>
    private lateinit var adapter: RecyclerListadoComidaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =  inflater.inflate(R.layout.fragment_comidas, container, false)

        Log.d("Comida   f   reagment", "Oncreate llamado: recargando comida")

        val rvListadoComida = layout.findViewById<RecyclerView>(R.id.rvListadoComida)
     


        // Configuración del botón de agregar usuario
        val btnAgregarComida = layout.findViewById<Button>(R.id.btnAgregarComidaAdmin)
        btnAgregarComida.setOnClickListener {
          //  Intent para abrir la actividad de agregar usuario
            val intent = Intent(requireContext(), AgregarComidaAdmin::class.java)
            startActivity(intent)


        }


        val Filtracion =layout.findViewById<RadioGroup>(R.id.Grupo_Filtracion)
              // Configura el listener
        Filtracion.setOnCheckedChangeListener { _, checkedId ->

            // Ejecuta acciones según la opción seleccionada
            when (checkedId) {
                R.id.Radio_de_mayor_a_menor_Nombre ->{

                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorNombreAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida.adapter = adapter

                }

                R.id.Radio_de_mayor_a_menor_precio -> {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorPrecioDesc()

                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){

                                val pokemon = Comida(

                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }

                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()
                            }
                        }
                    }


                    rvListadoComida.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)

                    rvListadoComida.adapter = adapter

                }
                R.id.Radio_de_menor_a_mayor_precio -> {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorPrecioAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida.adapter = adapter
                }


                R.id.Categoría_ordenar -> {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorCategoriaAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida.adapter = adapter


                }
                R.id.ordenar_Por_Estado -> {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorEstadoAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida.adapter = adapter


                }
                else -> {

                    Log.d("ELSEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", "HOLAAAAAAAAAAAAAAAAAA")

                    //se carga normal
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerTodosComidas()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida.adapter = adapter


                }
            }
        }




        // Inflate the layout for this fragment
        return layout
    }

    override fun onResume() {
            super.onResume()

        // Restaurar el estado predeterminado (sin selección)
         val Filtracion2 = view?.findViewById<RadioGroup>(R.id.Grupo_Filtracion)
            Filtracion2?.clearCheck() // Limpia la selección del RadioGroup
    //    cargarcomida()
        Log.d("CategoriaFragment onResume", "HOLAAAAAAAAAAAAAAAAAA")




//        val Filtracion =view?.findViewById<RadioGroup>(R.id.Grupo_Filtracion)
        // Configura el listener
        /*
        Filtracion?.setOnCheckedChangeListener { _, checkedId ->


            // Ejecuta acciones según la opción seleccionada
            when (checkedId) {
                R.id.Radio_de_mayor_a_menor_precio -> {
                    val rvListadoComida = view?.findViewById<RecyclerView>(R.id.rvListadoComida)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorPrecioDesc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida?.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida?.adapter = adapter

                }
                R.id.Radio_de_menor_a_mayor_precio -> {
                    val rvListadoComida = view?.findViewById<RecyclerView>(R.id.rvListadoComida)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorPrecioAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida?.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida?.adapter = adapter
                }


                R.id.Categoría_ordenar -> {
                    Log.d("CategoriaFragment", "onResume llamado: recargando por  categoria")

                    val rvListadoComida = view?.findViewById<RecyclerView>(R.id.rvListadoComida)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorCategoriaAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida?.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida?.adapter = adapter
                }

                R.id.ordenar_Por_Estado -> {
                    val rvListadoComida = view?.findViewById<RecyclerView>(R.id.rvListadoComida)
                    lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            requireContext(),
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()
                        val comidaDao = bd.ComidaDao()
                        val respuesta = comidaDao.ObtenerComidasOrdenadasPorEstadoAsc()
                        // Cambiar al hilo principal para actualizar la UI
                        if(respuesta.size > 0){
                            val listDP:MutableList<Comida> = mutableListOf()
                            for (res in respuesta){
                                val pokemon = Comida(
                                    id = res.id,
                                    nombre = res.nombre,
                                    descripcion =res.descripcion,
                                    precio = res.precio,
                                    categoria = res.categoria,
                                    fotoUrl = res.fotoUrl,
                                    estado = res.estado,
                                    Max_Ingredientes = res.Max_Ingredientes






                                )

                                listDP.add(pokemon)
                            }
                            adapter.setDataSet(listDP)
                            withContext(Dispatchers.Main){adapter.notifyDataSetChanged()}
                        }
                    }


                    rvListadoComida?.layoutManager = LinearLayoutManager(context)

                    list = mutableListOf()
                    adapter = RecyclerListadoComidaAdapter(list, viewLifecycleOwner)
                    rvListadoComida?.adapter = adapter

                }

                else -> {
                    //se carga normal
                    Log.d("CategoriaFragment", "onResume llamado: No hizo filtros")

                    // Recargar categorías al volver al fragmento
                    cargarcomida()


                }
            }
        }
        */








    }





}