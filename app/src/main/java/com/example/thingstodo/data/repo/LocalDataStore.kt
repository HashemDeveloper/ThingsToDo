package com.example.thingstodo.data.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_NAME)

@ViewModelScoped
class LocalDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private object PreferenceKeys {
        val sortKey = stringPreferencesKey(Constants.PREFERENCE_KEY)
    }
    private val dataStore = context.datastore

    suspend fun persistSortState(priority: Priority) {
        dataStore.edit { pref->
            pref[PreferenceKeys.sortKey] = priority.name
        }
    }

    val readSortState: Flow<String> = dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }
        .map { pref ->
            val sortState = pref[PreferenceKeys.sortKey] ?: Priority.NONE.name
            sortState
        }
}