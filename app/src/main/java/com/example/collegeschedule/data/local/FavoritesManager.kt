package com.example.collegeschedule.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoritesManager(private val context: Context) {


    companion object {
        val SELECTED_GROUP_KEY = stringPreferencesKey("selected_group")
    }

    val selectedGroupFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_GROUP_KEY]
        }

    suspend fun saveSelectedGroup(groupName: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_GROUP_KEY] = groupName
        }
    }

    suspend fun clearSelectedGroup() {
        context.dataStore.edit { preferences ->
            preferences.remove(SELECTED_GROUP_KEY)
        }
    }
}