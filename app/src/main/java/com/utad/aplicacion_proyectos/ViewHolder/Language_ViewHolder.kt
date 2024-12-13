package com.utad.aplicacion_proyectos.ViewHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.utad.aplicacion_proyectos.databinding.ItemLanguageBinding
import com.utad.aplicacion_proyectos.Data.Model.Language

class Language_ViewHolder(view : View) :ViewHolder(view) {
    private val binding = ItemLanguageBinding.bind(view)
    private lateinit var language: Language

    fun render (item: Language, onDeleteClickListener: (Language) -> Unit){
        language = item
        binding.tvNewLanguage.text= item.name
        binding.ivDeleteLanguage.setOnClickListener {
           showDeleteConfirmation (item, onDeleteClickListener)
        }
    }

    private fun showDeleteConfirmation(language: Language, onDeleteListener: (Language) -> Unit) {
        // Mostrar el cuadro de confirmación
        val alert = AlertDialog.Builder(itemView.context)
            .setTitle("Delete ${language.name}")
            .setMessage("Are you sure you want to delete ${language.name}?")
            .setPositiveButton("Yes") { _, _ ->
                onDeleteListener(language)
                Toast.makeText(itemView.context, "Language deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alert.show()
    }
}


