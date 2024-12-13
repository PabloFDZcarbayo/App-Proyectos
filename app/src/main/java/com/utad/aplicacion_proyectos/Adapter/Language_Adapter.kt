package com.utad.aplicacion_proyectos.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utad.aplicacion_proyectos.Data.Model.Language
import com.utad.aplicacion_proyectos.R
import com.utad.aplicacion_proyectos.ViewHolder.Language_ViewHolder

class Language_Adapter (
    private val languages: MutableList<Language>,
    private val onDeleteClickListener: (Language)-> Unit):RecyclerView.Adapter<Language_ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Language_ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Language_ViewHolder(layoutInflater.inflate(R.layout.item_language, parent, false))
    }
    override fun getItemCount(): Int {
        return languages.size
    }


    override fun onBindViewHolder(holder: Language_ViewHolder, position: Int) {
        val item= languages[position]
        holder.render(item, onDeleteClickListener)
    }
}