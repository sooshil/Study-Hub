package com.sukajee.feature.reader.domain.repository

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.utils.Resource

interface ReaderRepository {
    suspend fun getBook(bookId: String): Resource<Book>
    suspend fun updateReadingProgress(bookId: String, currentPage: Int)
    suspend fun toggleBookmark(bookId: String)
}
