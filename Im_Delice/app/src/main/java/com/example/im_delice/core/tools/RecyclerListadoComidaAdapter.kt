package com.example.im_delice.core.tools

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Comida
import com.example.im_delice.core.entities.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class RecyclerListadoComidaAdapter (private var dataSet: MutableList<Comida>,
                                    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerListadoComidaAdapter.ViewHolder>() {
    private var dataSetCategoria: MutableList<Categoria> = mutableListOf()

    /**
     * Clase que traduce las vistas del layout individual, para
     * utilizarlas en código y establecer sus valores
     * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPerfilComida: ImageView
        val tvNombreComida: EditText
        val tvDescripcionComida: EditText
        val tvprecioComida: EditText
        val tvCategoriaComida: RadioGroup
        val tvEstadoComida: RadioGroup
        val tvIngredientesMaxAdminComida: EditText




        val  btnEditarComida: Button
        val  btnEliminarComida: Button
        init {
            ivPerfilComida = view.findViewById(R.id.ivPerfilComida)
            tvNombreComida = view.findViewById(R.id.tvNombreComida)
            tvDescripcionComida = view.findViewById(R.id.tvDescripcionComida)
            tvprecioComida = view.findViewById(R.id.tvprecioComida)
            tvCategoriaComida = view.findViewById(R.id.tvCategoriaComida)
            tvEstadoComida = view.findViewById(R.id.tvEstadoComida)
            tvIngredientesMaxAdminComida=view.findViewById(R.id.tvIngredientesMaxAdminComida)


            btnEditarComida = view.findViewById(R.id.btnEditarComida)
            btnEliminarComida = view.findViewById(R.id.btnEliminarComida)
        }
    }
    /**
     * Método para inflar o darle vida al layout que mostrará
     * la información de cada elemento
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comidas, parent, false)
        return ViewHolder(view)
    }
    /**
     * Método para obtener la cantidad de elementos en el arreglo
     * */
    override fun getItemCount() = dataSet.size
    /**
     * Método para establecer la información del diseño de vista individual obtenida del arreglo por cada elemento
     * según si posición
     * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCategoriaComida.removeAllViews()

        holder.tvNombreComida.setText(dataSet[position].nombre)
        holder.tvDescripcionComida.setText(dataSet[position].descripcion)
        holder.tvprecioComida.setText(dataSet[position].precio.toString())
        holder.tvIngredientesMaxAdminComida.setText(dataSet[position].Max_Ingredientes.toString())


        //imprime las categorias
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                holder.itemView.context,
                ImDeliceDataBase::class.java,
                "ImDeliceDataBase"
            ).build()

            val categoriaDao = bd.CategoriaDao()
            val respuesta = categoriaDao.obtenerCategoria()
            Log.d("Debug", "Categorias obtenidas: $respuesta")

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
                    dataSetCategoria.add(pokemon)

                    listDP.add(pokemon)
                }
                withContext(Dispatchers.Main){
                    for (categoria in listDP ){


                        val radioButton = RadioButton(holder.itemView.context)
                        radioButton.text = categoria.nombre
                        radioButton.id = View.generateViewId() // Asigna un ID único al RadioButton
                        Log.d("Agrego", "Agrego ${categoria.nombre} en este pantalla")

                        holder.tvCategoriaComida.addView(radioButton)
                        if (dataSet[position].categoria == categoria.id) {
                            Log.d("Debug", "Comparando categoria ${dataSet[position].categoria} con ${categoria.id}")
                            radioButton.isChecked = true
                        }



                    }


                }
            }
        }



        // Asigna el rol al RadioGroup
        when (dataSet[position].estado) {
            "Disponible" -> holder.tvEstadoComida.check(R.id.radioDisponible)
            "no Disponible" -> holder.tvEstadoComida.check(R.id.radioNoDisponible)
        }
        holder.ivPerfilComida.desdeUrl(dataSet[position].fotoUrl)

        holder.btnEditarComida.setOnClickListener {

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Editar Comida")
                .setMessage("¿Estás seguro de que deseas Editar la Comida?")
                .setPositiveButton("Sí") { _, _ ->
                    if(holder.tvIngredientesMaxAdminComida.text.isEmpty()){
                        holder.tvIngredientesMaxAdminComida.setText("0")
                    }

                    // Recoger los valores modificados desde los EditText
                    val nombre = holder.tvNombreComida.text.toString().trim()//trim quita espacios antes y los despues
                    val Descripcion = holder.tvDescripcionComida.text.toString()
                    val Max_Ingredientes = holder.tvIngredientesMaxAdminComida.text.toString().toInt()
                    // Obtener la categoría seleccionada
                    val categoriaId = holder.tvCategoriaComida.checkedRadioButtonId
                    var idcategoria=0

                    val categoria = if (categoriaId != -1) {

                        val selectedRadioButton = holder.tvCategoriaComida.findViewById<RadioButton>(categoriaId)
                        if(selectedRadioButton==null){
                            null
                        }else{
                            selectedRadioButton.text.toString() // Extraer el texto del RadioButton seleccionado

                        }
                    } else {

                        null // O un valor por defecto si no se seleccionó ninguna categoría
                    }




                    val estado =when (holder.tvEstadoComida.checkedRadioButtonId) {
                    R.id.radioDisponible -> "Disponible"
                    R.id.radioNoDisponible -> "no Disponible"
                    else -> "Disponible"
                }


                    if (nombre==""||Descripcion==""||holder.tvprecioComida.text.isEmpty()||categoria==null){
                        // Mostrar una alerta
                        AlertDialog.Builder(holder.itemView.context)
                            .setTitle("Error")
                            .setMessage("Por favor, complete todos los campos.")
                            .setPositiveButton("Aceptar", null)
                            .show()
                        return@setPositiveButton // Salir del OnClickListener si hay campos vacíos
                    }
                    val precio = holder.tvprecioComida.text.toString().toDouble()
                    if (precio==0.0){
                        AlertDialog.Builder(holder.itemView.context)
                            .setTitle("Error")
                            .setMessage("Por favor, El valor debe de ser mayor de 0. valor del precio comida:  ${precio}")
                            .setPositiveButton("Aceptar", null)
                            .show()
                        return@setPositiveButton // Salir del OnClickListener si hay campos vacíos
                    }

                    //Buscamos los id de las categorias
                    for (buscar in dataSetCategoria){
                        if(categoria==buscar.nombre){
                            idcategoria=buscar.id

                        }
                    }
                    val editarcomida = Comida(
                        id =dataSet[position].id,
                        nombre = nombre,
                        descripcion = Descripcion,
                        precio = precio,
                        categoria=idcategoria,
                        estado = estado,
                        Max_Ingredientes = Max_Ingredientes,

                        fotoUrl = dataSet[position].fotoUrl
                    )

                    // Código para cambiar el usuario


                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val comidaDao = bd.ComidaDao()
                        val existe= comidaDao.verificarComida(editarcomida.nombre)
                        if (nombre==dataSet[position].nombre) {
                            comidaDao.ActualizarComida(editarcomida)

                        }else{
                            if (existe > 0) {
                                withContext(Dispatchers.Main) {
                                    AlertDialog.Builder(holder.itemView.context)
                                        .setTitle("Error")
                                        .setMessage("el nombre que quieres editar ya esta Registrado.")
                                        .setPositiveButton("Aceptar", null)
                                        .show()
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, dataSet.size)
                                }
                                return@launch
                            }else{
                                comidaDao.ActualizarComida(editarcomida)
                            }
                        }



                        withContext(Dispatchers.Main) {
                            dataSet[position]=editarcomida
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Comida editado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()



        }

        holder.btnEliminarComida.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Comida")
                .setMessage("¿Estás seguro de que deseas eliminar esta Comida?")
                .setPositiveButton("Sí") { _, _ ->
                    // Código para eliminar el usuario
                    val comida = dataSet[position]

                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val comidadao = bd.ComidaDao()
                        comidadao.EliminarComida(comida)

                        withContext(Dispatchers.Main) {
                            dataSet.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Comida eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()


        }


    }
    /**
     * Método para establecer la nueva información al dataset
     */
    fun setDataSet(dataSetL:MutableList<Comida>){
        this.dataSet = dataSetL
    }


}