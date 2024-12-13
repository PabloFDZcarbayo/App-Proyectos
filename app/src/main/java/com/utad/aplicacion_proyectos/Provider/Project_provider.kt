package com.utad.aplicacion_proyectos.Provider

import com.utad.aplicacion_proyectos.Data.Database.Entities.Project_Entitie
import com.utad.aplicacion_proyectos.Data.Database.ProjectsApplication
import com.utad.aplicacion_proyectos.Data.Model.Project


class Project_provider {

    companion object {
        suspend fun loadProjects(): MutableList<Project> {
            val projects_entities = ProjectsApplication.database.projectDao().getAllProjects()
            val projects = mutableListOf<Project>()
            for (project in projects_entities) {
                projects.add(
                    Project(
                        project.id,
                        project.name,
                        project.description,
                        project.date,
                        project.time,
                        project.language,
                        project.priority,
                        project.details
                    )
                )
            }
            return projects

        }

        suspend fun deleteProject(project: Project) {
            val project_entity = Project_Entitie(
                project.id,
                project.name,
                project.description,
                project.date,
                project.time,
                project.language,
                project.priority,
                project.details
            )
            ProjectsApplication.database.projectDao().deleteProject(project_entity)
        }

        suspend fun insertProject(project: Project_Entitie) {
            val project_entity = Project_Entitie(
                project.id,
                project.name,
                project.description,
                project.date,
                project.time,
                project.language,
                project.priority,
                project.details
            )
            ProjectsApplication.database.projectDao().insertProject(project_entity)
        }

        suspend fun updateProject(project: Project_Entitie) {
            val project_entity = Project_Entitie(
                project.id,
                project.name,
                project.description,
                project.date,
                project.time,
                project.language,
                project.priority,
                project.details
            )
            ProjectsApplication.database.projectDao().updateProject(project_entity)
        }
    }


}
