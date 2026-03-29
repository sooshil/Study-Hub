package com.sukajee.feature.profile.domain.repository

import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<User>
    suspend fun updateProfile(name: String, grade: String): Resource<User>
    suspend fun logout()
}
