package com.utad.aplicacion_proyectos.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.utad.aplicacion_proyectos.Adapter.Language_Adapter
import com.utad.aplicacion_proyectos.Data.Model.Language
import com.utad.aplicacion_proyectos.Provider.Language_provider
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.databinding.ActivityLanguagesRvBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Languages_RV_activity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesRvBinding
    private lateinit var lenguages_List: MutableList<Language>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: Language_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLanguagesRvBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Toolbar personalizado
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Inicializamos el recyclerview
        initRecyclerView()

        binding.toolbar.setNavigationOnClickListener() {
            navigateToProjects()
        }

        //Evento al pulsar sobre el boton de añadir lenguaje
        binding.btnAddLanguage.setOnClickListener {
            val intent = Intent(this, Add_Language_activity::class.java)
            startActivity(intent)
        }
    }

    /*Funcion que inicializa el recyclerview, carga los lenguajes guardados en la base de datos desde el provider,
    si no hay lenguajes, se muestra un mensaje de que no hay lenguajes, si hay lenguajes  muestra el recyclerview
     */
    private fun initRecyclerView() {
        lifecycleScope.launch {
            lenguages_List = Language_provider.loadLanguages()

            withContext(Dispatchers.Main) {
                val textView = binding.tvNotLanguagesYet

                if (lenguages_List.isEmpty()) {
                    textView.text = "No languages avaible yet"
                } else {
                    textView.text = ""
                    layoutManager = LinearLayoutManager(this@Languages_RV_activity)
                    adapter = Language_Adapter(lenguages_List) { lenguage ->
                        deleteLanguage(lenguage)

                    }
                    binding.rvLanguages.layoutManager = layoutManager
                    binding.rvLanguages.adapter = adapter
                }
            }
        }
    }

    //Funcion que elimina un lenguaje de la base de datos, y notifica al adaptador
    private fun deleteLanguage(lenguage: Language) {
        lifecycleScope.launch(Dispatchers.IO) {
            Language_provider.deleteLanguage(lenguage)
            lenguages_List.remove(lenguage)
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                if (lenguages_List.isEmpty()) {
                    binding.tvNotLanguagesYet.text = "No languages avaible yet"

            }
            }
        }
    }

    //Funcion para cambiar de activity
    private fun navigateToProjects() {
        val navigateToProjects = Intent(this, Projects_RV_Activity::class.java)
        startActivity(navigateToProjects)
    }
}
