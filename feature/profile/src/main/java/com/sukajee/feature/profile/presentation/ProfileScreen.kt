package com.sukajee.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.theme.Indigo500
import com.sukajee.core.design.theme.Purple500
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToLogin -> onLogout()
            }
        }
    }

    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.LogoutDismissed) },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out of your account?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.LogoutConfirmed) }) {
                    Text("Sign Out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(ProfileEvent.LogoutDismissed) }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(colors = listOf(Indigo500, Purple500)))
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.user?.avatarEmoji ?: "🎓",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                    Column {
                        Text(
                            text = state.user?.name ?: "Student",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = state.user?.email ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(0.2f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = state.user?.grade ?: "Grade 10",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        state.user?.let { user ->
            item {
                // Achievement stats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBadge("${user.booksRead}", "Books Read", Color(0xFF6366f1), Modifier.weight(1f))
                    StatBadge("${user.hoursStudied}h", "Study Time", Color(0xFF8b5cf6), Modifier.weight(1f))
                    StatBadge("${user.streakDays}", "Day Streak", Color(0xFFf59e0b), Modifier.weight(1f))
                    StatBadge(user.gradeAverage, "Average", Color(0xFF10b981), Modifier.weight(1f))
                }
            }
        }

        item {
            // Settings section
            Text(
                text = "Preferences",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    ToggleSettingItem(
                        icon = Icons.Outlined.Notifications,
                        label = "Push Notifications",
                        subtitle = "Get reminders to study",
                        checked = state.notificationsEnabled,
                        onToggle = { viewModel.onEvent(ProfileEvent.NotificationsToggled(it)) }
                    )
                    Divider(modifier = Modifier.padding(start = 64.dp))
                    ToggleSettingItem(
                        icon = Icons.Outlined.DarkMode,
                        label = "Dark Mode",
                        subtitle = "Easy on the eyes at night",
                        checked = state.darkModeEnabled,
                        onToggle = { viewModel.onEvent(ProfileEvent.DarkModeToggled(it)) }
                    )
                    Divider(modifier = Modifier.padding(start = 64.dp))
                    ToggleSettingItem(
                        icon = Icons.Outlined.WifiOff,
                        label = "Download on Wi-Fi only",
                        subtitle = "Save mobile data",
                        checked = state.downloadOverWifiOnly,
                        onToggle = { viewModel.onEvent(ProfileEvent.WifiOnlyToggled(it)) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Account",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    SettingItem(icon = Icons.Outlined.PrivacyTip, label = "Privacy Policy",
                        onClick = {})
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 64.dp),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                    SettingItem(icon = Icons.Outlined.Help, label = "Help & Support",
                        onClick = {})
                    Divider(modifier = Modifier.padding(start = 64.dp))
                    SettingItem(
                        icon = Icons.Outlined.ExitToApp,
                        label = "Sign Out",
                        labelColor = MaterialTheme.colorScheme.error,
                        onClick = { viewModel.onEvent(ProfileEvent.LogoutRequested) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "StudyHub v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatBadge(value: String, label: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, color = color)
            Text(text = label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
private fun ToggleSettingItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null,
            tint = labelColor.copy(if (labelColor == MaterialTheme.colorScheme.onSurface) 1f else 1f),
            modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium, color = labelColor)
    }
}
