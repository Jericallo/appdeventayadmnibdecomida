package com.example.im_delice.core.tools

import android.app.AlertDialog
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
import com.example.im_delice.core.entities.Ingrediente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerListadoIngredienteAdapter (private var dataSet: MutableList<Ingrediente>,
                                         private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerListadoIngredienteAdapter.ViewHolder>() {
    private var dataSetCategoria: MutableList<Categoria> = mutableListOf()


    /**
     * Clase que traduce las vistas del layout individual, para
     * utilizarlas en código y establecer sus valores
     * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPerfilIngrediente: ImageView
        val tvNombreIngrediente: EditText
        val tvPrecioExtraIngrediente: EditText
        val tvCategoriaIngrediente: RadioGroup
        val tvEstadoIngrediente: RadioGroup




        val  btnEditarIngrediente: Button
        val  btnEliminarIngrediente: Button
        init {
            ivPerfilIngrediente = view.findViewById(R.id.ivPerfilIngrediente)
            tvNombreIngrediente = view.findViewById(R.id.tvNombreIngrediente)
            tvPrecioExtraIngrediente = view.findViewById(R.id.tvPrecioExtraIngrediente)
            tvCategoriaIngrediente = view.findViewById(R.id.tvCategoriaIngrediente)
            tvEstadoIngrediente = view.findViewById(R.id.tvEstadoIngrediente)


            btnEditarIngrediente = view.findViewById(R.id.btnEditarIngrediente)
            btnEliminarIngrediente = view.findViewById(R.id.btnEliminarIngrediente)
        }
    }
    /**
     * Método para inflar o darle vida al layout que mostrará
     * la información de cada elemento
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingrediente, parent, false)
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






        holder.tvCategoriaIngrediente.removeAllViews()

        holder.tvNombreIngrediente.setText(dataSet[position].nombre)
        holder.tvPrecioExtraIngrediente.setText(dataSet[position].precioExtra.toString())
        //imprime las categorias
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val bd = Room.databaseBuilder(
                holder.itemView.context,
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
                    dataSetCategoria.add(pokemon)

                    listDP.add(pokemon)
                }
                withContext(Dispatchers.Main){
                    for (categoria in listDP ){
                        val radioButton = RadioButton(holder.itemView.context)
                        radioButton.text = categoria.nombre
                        radioButton.id = View.generateViewId() // Asigna un ID único al RadioButton

                        holder.tvCategoriaIngrediente.addView(radioButton)
                        if (dataSet[position].Categoria == categoria.id) {
                            radioButton.isChecked = true
                        }


                    }
                }
            }
        }



        // Asigna el rol al RadioGroup
        when (dataSet[position].disponibilidad) {
            true -> holder.tvEstadoIngrediente.check(R.id.radioDisponibleIngrediente)
            false -> holder.tvEstadoIngrediente.check(R.id.radioNoDisponibleIngrediente)
        }
        holder.ivPerfilIngrediente.desdeUrl(dataSet[position].foto)




        holder.btnEditarIngrediente.setOnClickListener {



            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Editar Ingrediente")
                .setMessage("¿Estás seguro de que deseas Editar el ingrediente?")
                .setPositiveButton("Sí") { _, _ ->
                    // Recoger los valores modificados desde los EditText
                    val nombre = holder.tvNombreIngrediente.text.toString().trim()//trim quita espacios antes y los despues


                    // Obtener la categoría seleccionada
                    val categoriaId = holder.tvCategoriaIngrediente.checkedRadioButtonId
                    var idcategoria=0
                    val categoria = if (categoriaId != -1) {

                        val selectedRadioButton = holder.tvCategoriaIngrediente.findViewById<RadioButton>(categoriaId)
                        if(selectedRadioButton==null){
                            null
                        }else{
                            selectedRadioButton.text.toString() // Extraer el texto del RadioButton seleccionado

                        }
                    } else {

                        null // O un valor por defecto si no se seleccionó ninguna categoría
                    }





                    val estado =when (holder.tvEstadoIngrediente.checkedRadioButtonId) {
                        R.id.radioDisponibleIngrediente -> true
                        R.id.radioNoDisponibleIngrediente -> false
                        else -> true
                    }

                    if (nombre==""||categoria==null||
                        holder.tvPrecioExtraIngrediente.text.isEmpty()){
                        // Mostrar una alerta
                        AlertDialog.Builder(holder.itemView.context)
                            .setTitle("Error")
                            .setMessage("Por favor, complete todos los campos.")
                            .setPositiveButton("Aceptar", null)
                            .show()
                        return@setPositiveButton // Salir del OnClickListener si hay campos vacíos
                    }
                    val precioextra = holder.tvPrecioExtraIngrediente.text.toString().toDouble()
                    if (precioextra==0.0){
                        AlertDialog.Builder(holder.itemView.context)
                            .setTitle("Error")
                            .setMessage("Por favor, El valor debe de ser mayor de 0. valor del ingrediente extra ${precioextra}")
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

                    val editarIngrediente = Ingrediente(
                        id =dataSet[position].id,
                        nombre = nombre,


                        foto = dataSet[position].foto,
                        Categoria = idcategoria,
                        disponibilidad = estado,
                        precioExtra = precioextra


                    )

                    // Código para cambiar el usuario


                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val ingredienteDao = bd.IngredienteDao()
                        val existe= ingredienteDao.verificarIngrediente(editarIngrediente.nombre)

                       if (nombre==dataSet[position].nombre) {
                           ingredienteDao.ActualizarIngrediente(editarIngrediente)

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
                               ingredienteDao.ActualizarIngrediente(editarIngrediente)
                           }
                       }


                        withContext(Dispatchers.Main) {
                            dataSet[position]=editarIngrediente
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Ingrediente editado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()



        }


        holder.btnEliminarIngrediente.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Ingrediente")
                .setMessage("¿Estás seguro de que deseas eliminar este Ingrediente?")
                .setPositiveButton("Sí") { _, _ ->
                    // Código para eliminar el usuario
                    val ingrediente = dataSet[position]

                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val ingredientedao = bd.IngredienteDao()
                        ingredientedao.EliminarIngrediente(ingrediente)

                        withContext(Dispatchers.Main) {
                            dataSet.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Ingrediente eliminado", Toast.LENGTH_SHORT).show()
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
    fun setDataSet(dataSetL:MutableList<Ingrediente>){
        this.dataSet = dataSetL
    }




}