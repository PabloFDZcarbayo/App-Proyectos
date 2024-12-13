package com.utad.aplicacion_proyectos.Activities


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.utad.aplicacion_proyectos.Data.Database.Entities.Project_Entitie
import com.utad.aplicacion_proyectos.Data.Model.Project
import com.utad.aplicacion_proyectos.Provider.Language_provider
import com.utad.aplicacion_proyectos.Provider.Project_provider
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.databinding.ActivityAddProjectBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class Add_Project_activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProjectBinding
    private var projectToEdit: Project? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*Carga los lenguajes al iniciar la activity en segundo plano, ademas comprobamos si se ha pasado un proyecto para editar,
        en lugar de crear uno nuevo, aseguramos que los lenguajes se carguen en el spinner antes de cargar los datos del proyecto*/
        lifecycleScope.launch {
            loadLanguages {
                if (intent.hasExtra("project")) {
                    projectToEdit =
                        intent.getParcelableExtra<Project>("project", Project::class.java)
                    projectToEdit?.let {
                        populateFieldsWithProject(it)
                    }
                }
            }
        }

        //Evento al pulsar sobre la fecha, el cual abrirá el calendario
        binding.etDate.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showDatePickerDialog()
                true
            } else {
                false
            }
        }

        /*Evento del btn addProject, comprobara si estamos creando un nuevo proyecto o editando uno existente,
        si estamos editando llamaremos a la funcion UpdateFields, si estamos creando llamaremos a la Add_Project
         */
        binding.btnSaveProject.setOnClickListener {
            if (validateInputs()) {
                if (projectToEdit != null) {
                    UpdateFields()
                    clearInputFields()
                    navigateToProjects()
                } else {
                    Add_Project()
                    clearInputFields()
                    navigateToProjects()
                }
            }
        }
    }

    /*Esta funcion llamara al provider para cargar los lenguajes guardados en la base de datos,
    una vez cargados los lenguajes se cargaran en el spinner mediante un adaptador.
    He añadido un callback que se llamara cuando los lenguajes se hayan cargado, de esta forma podemos actualizar el spinner si estamos editando un proyecto
     */
    private suspend fun loadLanguages(onLanguagesLoaded: (() -> Unit)? = null) {
        val languages = Language_provider.loadLanguages()
        val languagesNames = languages.map { it.name }
        withContext(Dispatchers.Main) { // Update UI on the main thread
            val adapter = ArrayAdapter(
                this@Add_Project_activity,
                android.R.layout.simple_spinner_item,
                languagesNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spLanguage.adapter = adapter
            onLanguagesLoaded?.invoke()
        }
    }


    //Comprobamos que los campos no esten vacios, en caso contrario mostraremos un Toast o error
    private fun validateInputs(): Boolean {
        val isNameValid = binding.etProjectName.text.isNotBlank()
        val isDescriptionValid = binding.etProjectDescription.text.isNotBlank()
        val isTimeValid = binding.etTime.text.isNotBlank()
        val isDateValid = binding.etDate.text.isNotBlank()
        val isDetailsValid = binding.etDetails.text.isNotBlank()
        val isPriorityValid =
            binding.btnLow.isChecked || binding.btnMedium.isChecked || binding.btnHigh.isChecked
        val isLanguageValid = binding.spLanguage.selectedItem != null
        return if (isNameValid && isDescriptionValid && isDateValid && isDetailsValid && isPriorityValid && isTimeValid && isLanguageValid) {
            true
        } else {
            if (!isNameValid) binding.etProjectName.error = "Project name is required"
            if (!isDescriptionValid) binding.etProjectDescription.error = "Description is required"
            if (!isDateValid) binding.etDate.error = "Date is required"
            if (!isDetailsValid) binding.etDetails.error = "Details are required"
            if (!isTimeValid) binding.etTime.error = "Time is required"
            if (!isPriorityValid) Toast.makeText(this, "Priority is required", Toast.LENGTH_SHORT)
                .show()
            if (!isLanguageValid) Toast.makeText(this, "Language is required", Toast.LENGTH_SHORT)
                .show()
            false
        }
    }

    //Limpiamos los campos
    private fun clearInputFields() {
        binding.etProjectName.setText("")
        binding.etProjectDescription.setText("")
        binding.etDate.setText("")
        binding.etDetails.setText("")
    }

    /*Esta funcion se encarga de crear un calendario para el usuario, donde podra escoger la fecha de inicio del proyecto,
    para luego establecerla en el campo correspondiente
     */
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            R.style.CustomDatePicker,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDay = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.etDate.setText(selectedDay)
            }, year, month, day
        )
        datePickerDialog.setOnShowListener {
            // Captura los botones del diálogo
            val positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            val negativeButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)

            // Aplica el colorSecondary manualmente
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.light_blue))
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.light_blue))
        }
        datePickerDialog.show()

    }

    //Esta funcion es llamada al pulsar sobre el btn addProject, se encarga de recoger los datos introducidos por el usuario y crear un nuevo proyecto
    private fun getProjectFromInputFields(): Project_Entitie {
        val projectName = binding.etProjectName.text.toString()
        val projectDescription = binding.etProjectDescription.text.toString()
        val projectDate = binding.etDate.text.toString()
        val projectTime = binding.etTime.text.toString()
        val projectLanguage = binding.spLanguage.selectedItem.toString()
        val projectPriority = when {
            binding.btnLow.isChecked -> "Low"
            binding.btnMedium.isChecked -> "Medium"
            binding.btnHigh.isChecked -> "High"
            else -> ""
        }
        val projectDetails = binding.etDetails.text.toString()

        return Project_Entitie(
            name = projectName,
            description = projectDescription,
            date = projectDate,
            time = projectTime,
            language = projectLanguage,
            priority = projectPriority,
            details = projectDetails
        )
    }

    //Funcion para añadir un proyecto, llamaremos al provider para insertar el proyecto en la bbdd
    private fun Add_Project() {
        lifecycleScope.launch {
            val project = getProjectFromInputFields()
            Project_provider.insertProject(project)
        }
        Toast.makeText(this, "Project added", Toast.LENGTH_SHORT).show()
        clearInputFields()
        navigateToProjects()

    }

    //Esta funcion se encarga de cargar los datos del proyecto a editar en los campos correspondientes
    private fun populateFieldsWithProject(project: Project) {
        binding.etProjectName.setText(project.name)
        binding.etProjectDescription.setText(project.description)
        binding.etDate.setText(project.date)
        binding.etTime.setText(project.time)
        binding.etDetails.setText(project.details)

        //En este paso buscamos el lenguaje en el spinner, y lo seleccionamos
        val selectedLanguage = project.language
        val adapter = (binding.spLanguage.adapter as ArrayAdapter<String>)
        val position = adapter.getPosition(selectedLanguage)
        if (position >= 0) {
            binding.spLanguage.setSelection(position)
        }
        when (project.priority) {
            "Low" -> binding.btnLow.isChecked = true
            "Medium" -> binding.btnMedium.isChecked = true
            "High" -> binding.btnHigh.isChecked = true
        }

    }


    /*Funcion para actualizar un proyecto, comprobamos que teniamos un intent para editar y recogemos los nuevos datos,
    sin embargo utilzamos copy() (id = projectToEdit!!.id) para mantener el id del proyecto, y asi asegurar que se actualice, en lugar de crear uno nuevo.
    De esta forma pasamos al provider el proyecto actualizado
     */

    private fun UpdateFields() {
        if (projectToEdit != null) {
            lifecycleScope.launch {
                val updatedProject = getProjectFromInputFields().copy(id = projectToEdit!!.id)
                Project_provider.updateProject(updatedProject)
                Toast.makeText(this@Add_Project_activity, "Project updated", Toast.LENGTH_SHORT)
                    .show()
                navigateToProjects()
            }
        }
    }


    //Cambiamos de activity
    private fun navigateToProjects() {
        val navigateToProjects = Intent(this, Projects_RV_Activity::class.java)
        startActivity(navigateToProjects)
    }

}


