package com.nraliim.storyapp.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nraliim.storyapp.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val nameKey = stringPreferencesKey("name")
    private val emailKey = stringPreferencesKey("email")
    private val passwordKey = stringPreferencesKey("password")
    private val userIdKey = stringPreferencesKey("userId")
    private val tokenKey = stringPreferencesKey("token")
    private val stateKey = booleanPreferencesKey("state")

    fun getUser(): Flow<User> {
        return dataStore.data.map {
            User(
                it[nameKey] ?: "",
                it[emailKey] ?: "",
                it[passwordKey] ?: "",
                it[userIdKey] ?: "",
                it[tokenKey] ?: "",
                it[stateKey] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit {
            it[nameKey] = user.name
            it[emailKey] = user.email
            it[passwordKey] = user.email
            it[userIdKey] = user.userId
            it[tokenKey] = user.token
            it[stateKey] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[nameKey] = ""
            it[emailKey] = ""
            it[passwordKey] = ""
            it[userIdKey] = ""
            it[tokenKey] = ""
            it[stateKey] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}