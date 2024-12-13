package com.utad.aplicacion_proyectos.ViewHolder

import android.content.Intent
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.utad.aplicacion_proyectos.Activities.Add_Project_activity
import com.utad.aplicacion_proyectos.Data.Model.Project
import com.utad.aplicacion_proyectos.databinding.ItemProjectBinding


class Project_ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemProjectBinding.bind(view) 
    private lateinit var project: Project
    
    fun render (item: Project, onDeleteClickListener: (Project) -> Unit, onItemClickListener: (Project) -> Unit){
        project = item
        binding.tvProjectName.text= item.name
        binding.tvLanguage.text= item.language
        binding.tvTime.text= item.time+" hours"
        binding.tvPriority.text= "Priority: " + item.priority
        binding.tvDescription.text= item.description
        binding.tvDate.text= "Date of start: " + item.date
      
        binding.btnDeleteProject.setOnClickListener {
            showDeleteConfirmation (item, onDeleteClickListener)
        }
        
        binding.cardView.setOnClickListener() {
            onItemClickListener(item)

        }
    }

    private fun showDeleteConfirmation(project: Project, onDeleteListener: (Project) -> Unit) {
        // Mostrar el cuadro de confirmación
        val alert = AlertDialog.Builder(itemView.context)
            .setTitle("Delete ${project.name}")
            .setMessage("Are you sure you want to delete ${project.name}?")
            .setPositiveButton("Yes") { _, _ ->
                onDeleteListener(project)
                Toast.makeText(itemView.context, "Project deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alert.show()
    }
}



