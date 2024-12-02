package com.example.im_delice.core.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.im_delice.R
import com.example.im_delice.ui.fragments.ComidasFragment
import com.example.im_delice.ui.fragments.MenuUsuariosFragment
import com.example.im_delice.ui.fragments.CategoriaFragment
import com.example.im_delice.ui.fragments.IngrendientesFragment


class DemoCollectionAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MenuUsuariosFragment()
            1-> ComidasFragment()
            2 -> CategoriaFragment()
            3 -> IngrendientesFragment()
            else ->{
                val fragment = DemoObjectFragment()
                fragment.arguments = Bundle().apply {
                    putInt(ARG_OBJECT, position + 1)
                }
                return fragment
            }
        }
        /*val fragment = DemoObjectFragment()
        fragment.arguments = Bundle().apply {
            // The object is just an integer.
            putInt(ARG_OBJECT, position + 1)

        }
        return fragment*/
    }
}

private const val ARG_OBJECT = "object"
class DemoObjectFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_collection_demo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val textView: TextView = view.findViewById(R.id.textView)
            textView.text = getInt(ARG_OBJECT).toString()
        }
    }
}