package com.utad.aplicacion_proyectos.Data.Database.Repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

//Esta clase sera la encargada de gestionar dataStore
object UserRepository {

    // Clave para el mapa de usuarios de DataStore
    private val USERS_MAP_KEY = stringPreferencesKey("users_map")

    // Función para guardar un usuario en DataStore
    suspend fun saveUser(context: Context, username: String, password: String) {

        // Crear una instancia de Gson,se utiliza para convertir objetos en cadenas JSON y viceversa.
        val gson = Gson()

        // Llama a la función getUsers para obtener el mapa de usuarios que ya están guardados en DataStore.
        val users = getUsers(context)

       // convierte el mapa inmutable users en un mapa mutable, lo que permite agregar o modificar elementos en él.
        val updatedUsers = users.toMutableMap()

        //Añade el nuevo usuario al mapa mutable con el nombre de usuario como clave y la contraseña como valor
        updatedUsers[username] = password

        //Convierte el mapa updatedUsers en una cadena JSON.
        val usersJson = gson.toJson(updatedUsers)

        // Guarda la cadena JSON en DataStore con la clave USERS_MAP_KEY.
        context.dataStore.edit { editor ->
            editor[USERS_MAP_KEY] = usersJson
        }
    }

    // Función para obtener todos los usuarios almacenados
    suspend fun getUsers(context: Context): Map<String, String> {

        //Accede a los datos de DataStore. La llamada a first() es una función de Flow que suspende la ejecución y espera hasta que los datos estén disponibles,
        val preferences = context.dataStore.data.first()

        //Recupera el valor almacenado bajo la clave USERS_MAP_KEY, Si no hay usuarios, devuelve un mapa vacío
        val usersJson = preferences[USERS_MAP_KEY] ?: "{}"

        //Crea una nueva instancia de Gson para realizar la conversión.
        val gson = Gson()

        //Se usa para que Gson sepa cómo convertir la cadena JSON de vuelta a un Map<String, String>
        val userType = object : TypeToken<Map<String, String>>() {}.type

        /*Convierte la cadena JSON (usersJson) en un objeto de tipo Map<String, String>.
        Este mapa contiene los usuarios con el nombre de usuario como clave y la contraseña como valor.
         */
        return gson.fromJson(usersJson, userType)
    }
}



