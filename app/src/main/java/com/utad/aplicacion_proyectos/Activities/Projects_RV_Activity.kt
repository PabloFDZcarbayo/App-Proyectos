package com.utad.aplicacion_proyectos.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.utad.aplicacion_proyectos.Adapter.Project_Adapter
import com.utad.aplicacion_proyectos.Data.Model.Project
import com.utad.aplicacion_proyectos.Provider.Language_provider
import com.utad.aplicacion_proyectos.Provider.Project_provider
import com.utad.aplicacion_proyectos.Provider.Project_provider.Companion.deleteProject
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.databinding.ActivityProyectosRvBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Projects_RV_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityProyectosRvBinding
    private lateinit var projects_list: MutableList<Project>
    private lateinit var adapter: Project_Adapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProyectosRvBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()

        binding.btnAddProject.setOnClickListener {
            lifecycleScope.launch {
                val disponibleLanguages = Language_provider.loadLanguages()
                if (disponibleLanguages.isEmpty()) {
                    Toast.makeText(
                        this@Projects_RV_Activity,
                        "No languages available,you have to add languages first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(this@Projects_RV_Activity, Add_Project_activity::class.java)
                    startActivity(intent)
                }

            }
        }

    }

    private fun initRecyclerView() {
        lifecycleScope.launch {
            projects_list = Project_provider.loadProjects()
            withContext(Dispatchers.Main) {
                val textView = binding.tvNotProjectsYet
                layoutManager = LinearLayoutManager(this@Projects_RV_Activity)
                binding.rvProjects.layoutManager = layoutManager

                if (projects_list.isEmpty()) {
                    textView.text = "No projects avaible yet"
                    adapter = Project_Adapter(mutableListOf(),
                        onDeleteClickListener = {},
                        onItemClickListener = {}
                    )
                } else {
                    textView.text = ""

                    adapter = Project_Adapter(projects_list,
                        onDeleteClickListener = { project ->
                            deleteProject(project)
                        },
                        onItemClickListener = { project ->
                            var intent = Intent(this@Projects_RV_Activity, Add_Project_activity::class.java)
                            intent = intent.putExtra("project", project)
                            startActivity(intent)
                        }
                    )
                }

                binding.rvProjects.adapter = adapter


            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.projects_toolbar_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_Languages -> {
                navigateToLanguages()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToLanguages() {
        val navigateToLanguages = Intent(this, Languages_RV_activity::class.java)
        startActivity(navigateToLanguages)
    }

    private fun deleteProject(project: Project) {
        lifecycleScope.launch {
            Project_provider.deleteProject(project)
            projects_list.remove(project)
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                if (projects_list.isEmpty()) {
                    binding.tvNotProjectsYet.text = "No projects avaible yet"
                }
            }
        }
    }


}



