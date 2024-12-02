package com.example.im_delice.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.im_delice.R
import com.example.im_delice.ui.fragments.RegistrarFragment

class MainFragemto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_fragemto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtén el extra del Intent (si existe)
        val mostrarRegistro = intent.getBooleanExtra("mostrarRegistro", false)

        // Si el extra es true, carga el fragmento de registro
        if (mostrarRegistro) {
            //CARGA EL FRAGMENTO
            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add(R.id.fcvPrincipal,RegistrarFragment())
                }



            }

        }


    }


}