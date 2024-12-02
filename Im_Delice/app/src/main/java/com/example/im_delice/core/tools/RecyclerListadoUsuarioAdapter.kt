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
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.im_delice.R
import com.example.im_delice.core.db.ImDeliceDataBase
import com.example.im_delice.core.entities.Usuario
import com.example.im_delice.ui.fragments.MenuUsuariosFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerListadoUsuarioAdapter(private var dataSet: MutableList<Usuario>,
                                    private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<RecyclerListadoUsuarioAdapter.ViewHolder>() {

    /**
     * Clase que traduce las vistas del layout individual, para
     * utilizarlas en código y establecer sus valores
     * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPerfil: ImageView
        val tvNombre: EditText
        val tvCorreo: EditText
        val tvContraseña: EditText
        val radioGroup: RadioGroup




        val  btnEditarUsuario: Button
        val  btnEliminarUsuario: Button
        init {
            ivPerfil = view.findViewById(R.id.ivPerfil)
            tvNombre = view.findViewById(R.id.tvNombre)
            tvCorreo = view.findViewById(R.id.tvCorreo)
            tvContraseña = view.findViewById(R.id.tvContraseña)
            radioGroup = view.findViewById(R.id.tvrol)

            btnEditarUsuario = view.findViewById(R.id.btnEditarUsuario)
            btnEliminarUsuario = view.findViewById(R.id.btnEliminarUsuario)
        }
    }
    /**
     * Método para inflar o darle vida al layout que mostrará
     * la información de cada elemento
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuarios, parent, false)
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
        holder.tvNombre.setText(dataSet[position].nombre)
        holder.tvCorreo.setText(dataSet[position].email)
        holder.tvContraseña.setText(dataSet[position].contraseña)
        // Asigna el rol al RadioGroup
        when (dataSet[position].rol) {
            "Admin" -> holder.radioGroup.check(R.id.radioAdmin)
            "Usuario" -> holder.radioGroup.check(R.id.radioUser)
        }
        holder.ivPerfil.desdeUrl(dataSet[position].foto)
        holder.btnEditarUsuario.setOnClickListener {

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Editar usuario")
                .setMessage("¿Estás seguro de que deseas Editar este Usuario?")
                .setPositiveButton("Sí") { _, _ ->
                    // Recoger los valores modificados desde los EditText
                    val nombre = holder.tvNombre.text.toString()
                    val correo = holder.tvCorreo.text.toString().trim()//quita espacios antes y despues
                    val contraseña = holder.tvContraseña.text.toString()
                    val rol = when (holder.radioGroup.checkedRadioButtonId) {
                        R.id.radioAdmin -> "Admin"
                        R.id.radioUser -> "Usuario"
                        else -> "Usuario"
                    }
                    if (nombre==""||correo==""||contraseña==""){
                        // Mostrar una alerta
                        AlertDialog.Builder(holder.itemView.context)
                            .setTitle("Error")
                            .setMessage("Por favor, complete todos los campos.")
                            .setPositiveButton("Aceptar", null)
                            .show()
                        return@setPositiveButton // Salir del OnClickListener si hay campos vacíos
                    }
                    val persona = Usuario(
                        id =dataSet[position].id,
                        nombre = nombre,
                        email = correo,
                        contraseña = contraseña,

                        rol = rol,
                        foto = dataSet[position].foto
                    )

                    // Código para cambiar el usuario


                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val usuarioDao = bd.usuarioDao()

                        val existe= usuarioDao.verificarEmail(persona.email)
                        if (correo==dataSet[position].email) {
                            usuarioDao.ActualizarUsuario(persona)

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
                                usuarioDao.ActualizarUsuario(persona)
                            }
                        }


                        withContext(Dispatchers.Main) {
                            dataSet[position]=persona
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, dataSet.size)
                            Toast.makeText(holder.itemView.context, "Usuario editado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()



        }

        holder.btnEliminarUsuario.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Eliminar usuario")
                .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
                .setPositiveButton("Sí") { _, _ ->
                    // Código para eliminar el usuario
                    val usuario = dataSet[position]

                    lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val bd = Room.databaseBuilder(
                            holder.itemView.context,
                            ImDeliceDataBase::class.java,
                            "ImDeliceDataBase"
                        ).build()

                        val usuarioDao = bd.usuarioDao()
                        usuarioDao.EliminarUsuario(usuario)

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
    fun setDataSet(dataSetL:MutableList<Usuario>){
        this.dataSet = dataSetL
    }


}