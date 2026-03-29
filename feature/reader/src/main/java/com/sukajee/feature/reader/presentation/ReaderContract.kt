package com.sukajee.feature.reader.presentation

import com.sukajee.core.common.model.Book

data class ReaderState(
    val book: Book? = null,
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val isBookmarked: Boolean = false,
    val showControls: Boolean = true,
    val fontSize: Int = 16,
    val isDarkMode: Boolean = false,
    val error: String? = null
)

sealed interface ReaderEvent {
    data class PageChanged(val page: Int) : ReaderEvent
    data object ToggleBookmark : ReaderEvent
    data object ToggleControls : ReaderEvent
    data object IncreaseFontSize : ReaderEvent
    data object DecreaseFontSize : ReaderEvent
    data object ToggleDarkMode : ReaderEvent
    data object NextPage : ReaderEvent
    data object PreviousPage : ReaderEvent
}
