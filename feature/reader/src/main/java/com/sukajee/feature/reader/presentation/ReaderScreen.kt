package com.sukajee.feature.reader.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.TextDecrease
import androidx.compose.material.icons.outlined.TextIncrease
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.theme.ReaderDark
import com.sukajee.core.design.theme.ReaderHeader
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    bookId: String,
    onNavigateBack: () -> Unit,
    viewModel: ReaderViewModel = koinViewModel(parameters = { parametersOf(bookId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val bgColor = if (state.isDarkMode) ReaderDark else MaterialTheme.colorScheme.background
    val textColor = if (state.isDarkMode) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onBackground
    val headerColor = if (state.isDarkMode) ReaderHeader else MaterialTheme.colorScheme.surface

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val book = state.book ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable { viewModel.onEvent(ReaderEvent.ToggleControls) }
    ) {
        // Reading content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, bottom = 100.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Chapter header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (state.isDarkMode)
                            Color(0xFF6366f1).copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = book.emoji, style = MaterialTheme.typography.displaySmall)
                    Text(
                        text = "Chapter ${(state.currentPage / 20) + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (state.isDarkMode) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Page ${state.currentPage} of ${book.totalPages}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (state.isDarkMode) Color.White.copy(0.7f)
                        else MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Simulated content paragraphs
            val paragraphs = generateParagraphs(book.subject, state.currentPage)
            paragraphs.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = state.fontSize.sp,
                        lineHeight = (state.fontSize * 1.7).sp
                    ),
                    color = textColor,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        // Top app bar
        AnimatedVisibility(
            visible = state.showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            TopAppBar(
                title = {
                    Column {
                        Text(book.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold,
                            color = if (state.isDarkMode) Color.White else MaterialTheme.colorScheme.onSurface)
                        Text(book.subject, style = MaterialTheme.typography.labelSmall,
                            color = if (state.isDarkMode) Color.White.copy(0.6f) else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back",
                            tint = if (state.isDarkMode) Color.White else MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(ReaderEvent.ToggleDarkMode) }) {
                        Icon(Icons.Outlined.DarkMode, contentDescription = "Dark mode",
                            tint = if (state.isDarkMode) Color(0xFF6366f1) else MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = { viewModel.onEvent(ReaderEvent.ToggleBookmark) }) {
                        Icon(
                            imageVector = if (state.isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkAdd,
                            contentDescription = "Bookmark",
                            tint = if (state.isBookmarked) Color(0xFF6366f1)
                            else if (state.isDarkMode) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor
                )
            )
        }

        // Bottom controls
        AnimatedVisibility(
            visible = state.showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Progress slider
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "1",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (state.isDarkMode) Color.White.copy(0.6f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = state.currentPage.toFloat(),
                        onValueChange = { viewModel.onEvent(ReaderEvent.PageChanged(it.toInt())) },
                        valueRange = 1f..book.totalPages.toFloat(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "${book.totalPages}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (state.isDarkMode) Color.White.copy(0.6f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinearProgressIndicator(
                    progress = { book.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFF6366f1)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { viewModel.onEvent(ReaderEvent.DecreaseFontSize) }) {
                            Icon(Icons.Outlined.TextDecrease, contentDescription = "Decrease font",
                                modifier = Modifier.size(20.dp),
                                tint = if (state.isDarkMode) Color.White.copy(0.7f) else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { viewModel.onEvent(ReaderEvent.IncreaseFontSize) }) {
                            Icon(Icons.Outlined.TextIncrease, contentDescription = "Increase font",
                                modifier = Modifier.size(20.dp),
                                tint = if (state.isDarkMode) Color.White.copy(0.7f) else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { viewModel.onEvent(ReaderEvent.PreviousPage) }) {
                            Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Previous",
                                tint = if (state.isDarkMode) Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                        IconButton(onClick = { viewModel.onEvent(ReaderEvent.NextPage) }) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowForwardIos, contentDescription = "Next",
                                tint = if (state.isDarkMode) Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}

private fun generateParagraphs(subject: String, page: Int): List<String> {
    val content = mapOf(
        "Mathematics" to listOf(
            "In this chapter, we explore the fundamental properties of quadratic equations and their applications in real-world scenarios. The standard form ax² + bx + c = 0 serves as the foundation for all quadratic analysis.",
            "The discriminant b² - 4ac determines the nature of roots. When positive, two distinct real roots exist. When zero, exactly one real root (a repeated root) exists. When negative, two complex conjugate roots exist.",
            "Problem-solving with quadratics extends to geometry, physics, and economics. Understanding parabolic trajectories requires mastery of vertex form: f(x) = a(x - h)² + k, where (h, k) is the vertex.",
            "Practice exercises reinforce theoretical understanding. Always verify solutions by substituting back into the original equation."
        ),
        "Physics" to listOf(
            "Newton's Laws of Motion form the cornerstone of classical mechanics. The first law establishes that an object at rest remains at rest, and an object in motion continues moving at constant velocity unless acted upon by an external force.",
            "The second law, F = ma, quantifies the relationship between force, mass, and acceleration. This equation enables engineers to design everything from car safety systems to spacecraft trajectories.",
            "Work-energy theorem states that the net work done on an object equals the change in its kinetic energy: W_net = ΔKE = ½mv² - ½mv₀². Conservation of energy principles extend this relationship across all physical systems.",
            "Understanding these principles through laboratory experiments deepens conceptual mastery beyond theoretical knowledge."
        ),
        "History" to listOf(
            "The Industrial Revolution, spanning roughly 1760 to 1840, fundamentally transformed human civilization. Beginning in Britain, this period saw the transition from agrarian economies to manufacturing-based urban societies.",
            "Steam power, mechanized textile production, and iron foundries created unprecedented economic growth while simultaneously causing significant social disruption. Urban populations swelled as rural workers migrated to factory towns.",
            "Primary sources from this era reveal the human dimension of industrial transformation — both the opportunities created and the hardships endured by workers, particularly women and children in factories.",
            "The long-term consequences of industrialization continue to shape modern geopolitics, environmental challenges, and economic structures today."
        )
    )
    return content[subject] ?: listOf(
        "This chapter presents comprehensive coverage of the subject matter. Students are encouraged to engage actively with the material through note-taking, practice problems, and peer discussion.",
        "The concepts introduced in these pages build systematically upon previous knowledge. Understanding each section thoroughly before proceeding ensures the strongest possible foundation.",
        "Real-world applications demonstrate the relevance of theoretical concepts. Look for these examples throughout the text as they appear in margins and dedicated application boxes.",
        "Review questions at the end of each chapter reinforce comprehension and prepare students for assessment. Spend adequate time working through each question independently before consulting the answer key."
    )
}
