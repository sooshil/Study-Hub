package com.sukajee.feature.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.components.StudyHubPasswordTextField
import com.sukajee.core.design.components.StudyHubPrimaryButton
import com.sukajee.core.design.components.StudyHubTextButton
import com.sukajee.core.design.components.StudyHubTextField
import com.sukajee.core.design.theme.ShapeTextField
import org.koin.androidx.compose.koinViewModel

private val GRADES = listOf("Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var gradeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RegisterEffect.NavigateToHome -> onRegisterSuccess()
                is RegisterEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.padding(start = (-8).dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = "Create Account 🎓",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Join thousands of students learning smarter",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        StudyHubTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(RegisterEvent.NameChanged(it)) },
            label = "Full Name",
            placeholder = "Your full name",
            leadingIcon = Icons.Outlined.Person,
            errorMessage = state.nameError
        )

        StudyHubTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
            label = "Email",
            placeholder = "student@email.com",
            leadingIcon = Icons.Outlined.Email,
            errorMessage = state.emailError,
            keyboardType = KeyboardType.Email
        )

        // Grade picker
        Column {
            Text(
                text = "GRADE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            ExposedDropdownMenuBox(
                expanded = gradeExpanded,
                onExpandedChange = { gradeExpanded = it }
            ) {
                OutlinedTextField(
                    value = state.grade,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    leadingIcon = {
                        Icon(Icons.Outlined.School, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gradeExpanded) },
                    shape = ShapeTextField,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
                ExposedDropdownMenu(
                    expanded = gradeExpanded,
                    onDismissRequest = { gradeExpanded = false }
                ) {
                    GRADES.forEach { grade ->
                        DropdownMenuItem(
                            text = { Text(grade) },
                            onClick = {
                                viewModel.onEvent(RegisterEvent.GradeChanged(grade))
                                gradeExpanded = false
                            }
                        )
                    }
                }
            }
        }

        StudyHubPasswordTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
            label = "Password",
            placeholder = "At least 6 characters",
            leadingIcon = Icons.Outlined.Lock,
            errorMessage = state.passwordError
        )

        StudyHubPasswordTextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
            label = "Confirm Password",
            placeholder = "Repeat your password",
            errorMessage = state.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(4.dp))

        StudyHubPrimaryButton(
            text = "Create Account",
            onClick = { viewModel.onEvent(RegisterEvent.RegisterClicked) },
            isLoading = state.isLoading
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            StudyHubTextButton(text = "Sign In", onClick = onNavigateBack)
        }
    }
}
