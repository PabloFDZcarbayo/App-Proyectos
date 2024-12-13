package com.utad.aplicacion_proyectos.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.utad.aplicacion_proyectos.Data.Database.AppDatabase
import com.utad.aplicacion_proyectos.Data.Database.Entities.Language_Entitie
import com.utad.aplicacion_proyectos.Data.Database.ProjectsApplication
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.databinding.ActivityAddLanguageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Add_Language_activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLanguageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddLanguageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Evento al pulsar sobre el boton de guardar, que llamara a la funcion que añadira el lenguaje o mostrara un error
        binding.btnSaveLanguage.setOnClickListener {
            val language = binding.etAddLanguage.text.toString().lowercase()
            if (language.isNotEmpty()) {
                Add_Language(language)
                binding.etAddLanguage.text.clear()

            } else {
                binding.etAddLanguage.error = "Language is required"

            }
        }
    }

    /*Esta funcion llamara al provider para insertar el lenguaje en la bbdd,
    Primero comprobaremos que el lenguaje no exista en la bbdd, si no existe lo añadiremos
     */
    private fun Add_Language(language: String) {
        lifecycleScope.launch {
            val existingLanguage =
                ProjectsApplication.database.languageDao().getLanguageByName(language)
            if (existingLanguage.isEmpty()) {
                val newLanguage = Language_Entitie(name = language)
                ProjectsApplication.database.languageDao().insertLanguage(newLanguage)
                Toast.makeText(this@Add_Language_activity, "Language added", Toast.LENGTH_SHORT)
                    .show()
                NavigateToLanguages()
            } else
                Toast.makeText(this@Add_Language_activity, "You already know this language", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    //Funcion para navegar a la activity de lenguajes
    private fun NavigateToLanguages() {
       val NavigateToLanguages = Intent(this, Languages_RV_activity::class.java)
        startActivity(NavigateToLanguages)
    }
}

