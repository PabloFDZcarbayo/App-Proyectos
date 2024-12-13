package com.utad.aplicacion_proyectos.Activities


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.utad.aplicacion_proyectos.Data.Database.Repository.UserRepository
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.databinding.ActivityRegistroBinding
import kotlinx.coroutines.runBlocking


class Registro_activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnMakeAccount.setOnClickListener {
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if (username.isEmpty()) {
                binding.etUserName.error = "Error: User is required"
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Error: Password is required"
            }
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Error: Password is required"
            }


            if (password != confirmPassword) {
                Toast.makeText(this, "Error: Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
                clearInputFields()

            } else {

                runBlocking {
                UserRepository.saveUser(this@Registro_activity, username, password)
                        Toast.makeText(
                            this@Registro_activity,
                            "Account created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToMain()
                    }
                }
            }

        }

    private fun navigateToMain() {
        val navigateToMain = Intent(this, MainActivity::class.java)
        startActivity(navigateToMain)
    }

    private fun clearInputFields() {
        binding.etUserName.setText("")
        binding.etPassword.setText("")
        binding.etConfirmPassword.setText("")
    }


}
