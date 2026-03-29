package com.sukajee.feature.auth.data.repository

import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import com.sukajee.core.network.api.StudyHubApi
import com.sukajee.feature.auth.data.local.AuthPreferencesManager
import com.sukajee.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val api: StudyHubApi,
    private val prefsManager: AuthPreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<User> {
        return try {
            // Simulate local auth for demo (no real API)
            if (email.isBlank() || password.isBlank()) {
                return Resource.Error("Email and password are required")
            }
            if (!email.contains("@")) {
                return Resource.Error("Please enter a valid email")
            }
            if (password.length < 6) {
                return Resource.Error("Password must be at least 6 characters")
            }
            val mockUser = User(
                id = "user_001",
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                grade = "Grade 10",
                booksRead = 12,
                hoursStudied = 48,
                streakDays = 7,
                gradeAverage = "A+"
            )
            prefsManager.saveAuthData(
                token = "mock_token_${System.currentTimeMillis()}",
                userId = mockUser.id,
                name = mockUser.name,
                email = mockUser.email,
                grade = mockUser.grade
            )
            Resource.Success(mockUser)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun register(name: String, email: String, password: String, grade: String): Resource<User> {
        return try {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                return Resource.Error("All fields are required")
            }
            if (!email.contains("@")) {
                return Resource.Error("Please enter a valid email")
            }
            if (password.length < 6) {
                return Resource.Error("Password must be at least 6 characters")
            }
            val mockUser = User(
                id = "user_${System.currentTimeMillis()}",
                name = name,
                email = email,
                grade = grade
            )
            prefsManager.saveAuthData(
                token = "mock_token_${System.currentTimeMillis()}",
                userId = mockUser.id,
                name = mockUser.name,
                email = mockUser.email,
                grade = mockUser.grade
            )
            Resource.Success(mockUser)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun logout() {
        prefsManager.clearAuthData()
    }

    override fun isLoggedIn(): Flow<Boolean> = prefsManager.isLoggedIn

    override fun getCurrentUser(): Flow<User?> = prefsManager.authToken.map { token ->
        if (token != null) {
            User(
                id = "user_001",
                name = "Student",
                email = "student@studyhub.app",
                grade = "Grade 10"
            )
        } else null
    }
}
