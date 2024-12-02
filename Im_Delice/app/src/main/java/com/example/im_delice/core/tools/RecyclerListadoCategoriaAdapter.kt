package com.example.im_delice.core.tools

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Categoria
import com.example.im_delice.core.entities.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerListadoCategoriaAdapter (private var dataSet: MutableList<Categoria>,
                                       private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerListadoCategoriaAdapter.ViewHolder>() {

    /**
     * Clase que traduce las vistas del layout individual, para
     * utilizarlas en código y establecer sus valores
     * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPerfilCategoria: ImageView
        val tvNombreCategoria: EditText
        val tvDescripcionCategoria: EditText





        val  btnEliminarCategoria: Button
        val  btnEditarCategoria: Button
        init {
            ivPerfilCategoria = view.findViewById(R.id.ivPerfilCategoria)
            tvNombreCategoria = view.findViewById(R.id.tvNombreCategoria)
            tvDescripcionCategoria = view.findViewById(R.id.tvDescripcionCategoria)


            btnEditarCategoria = view.findViewById(R.id.btnEditarCategoria)
            btnEliminarCategoria = view.findViewById(R.id.btnEliminarCategoria)
        }
    }
    /**
     * Método para inflar o darle vida al layout que mostrará
     * la información de cada elemento
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
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
        holder.tvNombreCategoria.setText(dataSet[position].nombre)
        holder.tvDescripcionCategoria.setText(dataSet[position].descripcion)

        holder.ivPerfilCategoria.desdeUrl(dataSet[position].foto)


        holder.btnEditarCategoria.setOnClickListener {

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Editar usuario")
                .setMessage("¿Estás seguro de que deseas Editar este Categoria? las comidas asignadaas de esta categoria tambien van a cambiar")
                .setPositiveButton("Sí") { _, _ ->
                    // Recoger los valores modificados desde los EditText
                    val nombre = holder.tvNombreCategoria.text.toString().trim()//trim quita espacios
                    val descripcion = holder.tvDescripcionCategoria.text.toString()

                    if (nombre==""||descripcion==""){
                        // Mostrar una alerta
                        AlertDialog.Builder(holder.itemView.context)
                            .setTitle("Error")
                            .setMessage("Por favor, complete todos los campos.")
                            .setPositiveButton("Aceptar", null)
                            .show()
                        return@setPositiveButton // Salir del OnClickListener si hay campos vacíos
                    }
                    val modificar = Categoria(
                        id =dataSet[position].id,
                        nombre = nombre,
                        descripcion = descripcion,

                        foto = dataSet[position].foto
                    )

                    // Código para cambiar el usuario


                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val categoriaDao = bd.CategoriaDao()
                       val existe= categoriaDao.verificarCategoria(modificar.nombre)
                        if (nombre==dataSet[position].nombre) {
                            categoriaDao.ActualizarCategoria(modificar)

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
                                categoriaDao.ActualizarCategoria(modificar)
                            }
                        }


                        withContext(Dispatchers.Main) {
                            dataSet[position]=modificar
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Categoria editado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()



        }

        holder.btnEliminarCategoria.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar Categoria")
                .setMessage("¿Estás seguro de que deseas eliminar esta categoria? Tambien se eliminara las comidas que estan en la categoria")
                .setPositiveButton("Sí") { _, _ ->
                    // Código para eliminar el usuario
                    val eliminar = dataSet[position]

                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val categoriaDao = bd.CategoriaDao()
                        categoriaDao.eliminarCategoria(eliminar)

                        withContext(Dispatchers.Main) {
                            dataSet.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Categoria eliminado", Toast.LENGTH_SHORT).show()
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
    fun setDataSet(dataSetL:MutableList<Categoria>){
        this.dataSet = dataSetL
    }


}