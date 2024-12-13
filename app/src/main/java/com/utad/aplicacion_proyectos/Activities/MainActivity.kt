package com.utad.aplicacion_proyectos.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.utad.aplicacion_proyectos.Data.Database.Repository.UserRepository
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        //Evento al pulsar sobre el boton de registro
        binding.btnMakeAccount.setOnClickListener { navigateToRegistro() }

        //Evento al pulsar sobre el boton de login
        binding.btnLogin.setOnClickListener {
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            if (username.isEmpty()) {
                binding.etUserName.error = "Error: User is required"
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Error: Password is required"
            } else {


                lifecycleScope.launch {
                    val isValid = validateUser(username, password)
                    if (isValid) {
                            navigateToProjects()
                        }
                    }


                }


            }

    }


    private fun navigateToRegistro() {
        val navigateToRegistro = Intent(this, Registro_activity::class.java)
        startActivity(navigateToRegistro)

    }

    /* Esta funciona valida los datos con datastore
     Primero lee los datos que tenemos almacenados y los compara con los introducidos por el usuario*/

    private suspend fun validateUser(username: String, password: String): Boolean {
        return try {
            val users = UserRepository.getUsers(this@MainActivity)
            // Validar si el nombre de usuario existe y si la contraseña coincide
            val storedPassword = users[username]
             if (storedPassword != null && storedPassword == password) {
                true
            } else {
                // Si no coincide, mostrar un mensaje de error
                Toast.makeText(
                    this@MainActivity,
                    "Error: User or password is incorrect",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        } catch (e: Exception) {
            print(e)
            false
        }
    }


private fun navigateToProjects() {
    val navigateToProjects = Intent(this, Projects_RV_Activity::class.java)
    startActivity(navigateToProjects)
    finish()
}

}


