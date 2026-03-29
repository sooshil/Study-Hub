package com.sukajee.feature.auth.domain.repository

import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun register(name: String, email: String, password: String, grade: String): Resource<User>
    suspend fun logout()
    fun isLoggedIn(): Flow<Boolean>
    fun getCurrentUser(): Flow<User?>
}