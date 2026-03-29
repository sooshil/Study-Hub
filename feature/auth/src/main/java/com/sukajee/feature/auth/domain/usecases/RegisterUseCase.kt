package com.sukajee.feature.auth.domain.usecases

import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import com.sukajee.feature.auth.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        grade: String
    ): Resource<User> = repository.register(name.trim(), email.trim(), password, grade)
}
