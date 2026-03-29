package com.sukajee.feature.auth.domain.usecases

import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import com.sukajee.feature.auth.domain.repository.AuthRepository


class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<User> =
        repository.login(email.trim(), password)
}
