package com.example.im_delice.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.im_delice.R
import com.example.im_delice.core.tools.DemoCollectionAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Menu_Usuarios : AppCompatActivity() {
    private val demoCollectionAdapter by lazy { DemoCollectionAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_usuarios)  // Asegúrate de que el layout correcto se está utilizando

        val viewPager = findViewById<ViewPager2>(R.id.vp2)
        viewPager.adapter = demoCollectionAdapter
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position){
                0 -> "Cuentas"
                1 -> "Comida"
                2 -> "Categoria"
                3 -> "Ingredientes"
                else -> "ola"
            } //"OBJECT ${(position + 1)}"
        }.attach()

    }
}