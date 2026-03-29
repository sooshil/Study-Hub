package com.sukajee.feature.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.components.StudyHubPasswordTextField
import com.sukajee.core.design.components.StudyHubPrimaryButton
import com.sukajee.core.design.components.StudyHubTextButton
import com.sukajee.core.design.components.StudyHubTextField
import com.sukajee.core.design.theme.Indigo500
import com.sukajee.core.design.theme.Purple500
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onLoginSuccess()
                is LoginEffect.NavigateToRegister -> onNavigateToRegister()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Indigo500, Purple500),
                    endY = 400f
                )
            )
    ) {
        // Header section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📚", style = MaterialTheme.typography.displaySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "StudyHub",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your personal learning companion",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        // Bottom sheet form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp)
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Welcome back! 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sign in to continue your learning journey",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            StudyHubTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                label = "Email",
                placeholder = "student@email.com",
                leadingIcon = Icons.Outlined.Email,
                errorMessage = state.emailError,
                keyboardType = KeyboardType.Email
            )

            StudyHubPasswordTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = "Password",
                placeholder = "Enter your password",
                leadingIcon = Icons.Outlined.Lock,
                errorMessage = state.passwordError
            )

            Spacer(modifier = Modifier.height(4.dp))

            StudyHubPrimaryButton(
                text = "Sign In",
                onClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
                isLoading = state.isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                StudyHubTextButton(
                    text = "Sign Up",
                    onClick = onNavigateToRegister
                )
            }

            if (state.error != null) {
                Snackbar(
                    action = {
                        StudyHubTextButton(
                            text = "Dismiss",
                            onClick = { viewModel.onEvent(LoginEvent.ErrorDismissed) }
                        )
                    }
                ) {
                    Text(text = state.error ?: "")
                }
            }
        }
    }
}
