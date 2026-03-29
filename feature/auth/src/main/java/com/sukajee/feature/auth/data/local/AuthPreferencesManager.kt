package com.sukajee.feature.auth.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthPreferencesManager(private val context: Context) {

    private object Keys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_GRADE = stringPreferencesKey("user_grade")
    }

    val authToken: Flow<String?> = context.authDataStore.data
        .map { it[Keys.AUTH_TOKEN] }

    val isLoggedIn: Flow<Boolean> = context.authDataStore.data
        .map { it[Keys.AUTH_TOKEN] != null }

    suspend fun saveAuthData(token: String, userId: String, name: String, email: String, grade: String) {
        context.authDataStore.edit { prefs ->
            prefs[Keys.AUTH_TOKEN] = token
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_NAME] = name
            prefs[Keys.USER_EMAIL] = email
            prefs[Keys.USER_GRADE] = grade
        }
    }

    suspend fun clearAuthData() {
        context.authDataStore.edit { it.clear() }
    }
}
