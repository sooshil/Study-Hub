package com.sukajee.feature.library.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.components.BookGridCard
import com.sukajee.core.design.components.BookListCard
import com.sukajee.core.design.theme.Indigo500
import com.sukajee.core.design.theme.Purple500
import com.sukajee.core.design.theme.ShapeTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(
    onBookClick: (String) -> Unit,
    viewModel: LibraryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(colors = listOf(Indigo500, Purple500)))
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("My Library", style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold, color = Color.White)
                        Text("${state.books.size} books available",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f))
                    }
                    IconButton(onClick = { viewModel.onEvent(LibraryEvent.ToggleViewMode) }) {
                        Icon(
                            imageVector = if (state.isGridView) Icons.Outlined.List else Icons.Outlined.GridView,
                            contentDescription = "Toggle view",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onEvent(LibraryEvent.SearchQueryChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search books or subjects...", color = Color.White.copy(0.6f)) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Search, contentDescription = null, tint = Color.White.copy(0.8f))
                    },
                    singleLine = true,
                    shape = ShapeTextField,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White.copy(0.5f),
                        unfocusedBorderColor = Color.White.copy(0.3f),
                        focusedContainerColor = Color.White.copy(0.15f),
                        unfocusedContainerColor = Color.White.copy(0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        }

        // Subject filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.subjects.forEach { subject ->
                FilterChip(
                    selected = state.selectedSubject == subject,
                    onClick = { viewModel.onEvent(LibraryEvent.SubjectSelected(subject)) },
                    label = { Text(subject, style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        val displayBooks = state.filteredBooks.ifEmpty { state.books }

        if (state.isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(displayBooks) { book ->
                    BookGridCard(
                        emoji = book.emoji,
                        title = book.title,
                        grade = "${book.subject} · ${book.grade}",
                        progress = book.progress,
                        accentColor = hexToColor(book.accentColorHex),
                        onClick = { onBookClick(book.id) }
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(displayBooks) { book ->
                    BookListCard(
                        emoji = book.emoji,
                        title = book.title,
                        subject = "${book.subject} · ${book.grade}",
                        progress = book.progress,
                        accentColor = hexToColor(book.accentColorHex),
                        onClick = { onBookClick(book.id) }
                    )
                }
            }
        }
    }
}
