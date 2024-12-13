package com.utad.aplicacion_proyectos.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.utad.aplicacion_proyectos.Data.Model.Project
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.ViewHolder.Project_ViewHolder

class Project_Adapter(
    private val projects: MutableList<Project>,
    private val onDeleteClickListener: (Project) -> Unit,
    private val onItemClickListener: (Project) -> Unit
) : RecyclerView.Adapter<Project_ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Project_ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Project_ViewHolder(layoutInflater.inflate(R.layout.item_project, parent, false))
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    override fun onBindViewHolder(holder: Project_ViewHolder, position: Int) {
        val item = projects[position]
        holder.render(item, onDeleteClickListener, onItemClickListener)
    }

}