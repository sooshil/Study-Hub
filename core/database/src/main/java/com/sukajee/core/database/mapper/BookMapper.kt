package com.sukajee.core.database.mapper

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.model.BookStatus
import com.sukajee.core.database.entity.BookEntity

fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    subject = subject,
    grade = grade,
    emoji = emoji,
    accentColorHex = accentColorHex,
    totalPages = totalPages,
    currentPage = currentPage,
    isBookmarked = isBookmarked,
    status = BookStatus.valueOf(status)
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    subject = subject,
    grade = grade,
    emoji = emoji,
    accentColorHex = accentColorHex,
    totalPages = totalPages,
    currentPage = currentPage,
    isBookmarked = isBookmarked,
    status = status.name
)
