package com.sukajee.feature.home.data

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.model.BookStatus


object SampleData {

    val books = listOf(
        Book(
            "1",
            "Mathematics Grade 10",
            "Mathematics",
            "Grade 10",
            "📐",
            "#6366f1",
            300,
            180,
            status = BookStatus.IN_PROGRESS
        ),
        Book(
            "2",
            "Physics Fundamentals",
            "Physics",
            "Grade 11",
            "⚛️",
            "#8b5cf6",
            250,
            50,
            status = BookStatus.IN_PROGRESS
        ),
        Book(
            "3",
            "World History",
            "History",
            "Grade 10",
            "🌍",
            "#3b82f6",
            400,
            400,
            status = BookStatus.COMPLETED
        ),
        Book(
            "4",
            "English Literature",
            "English",
            "Grade 10",
            "📖",
            "#10b981",
            200,
            80,
            status = BookStatus.IN_PROGRESS
        ),
        Book(
            "5",
            "Chemistry Basics",
            "Chemistry",
            "Grade 11",
            "🧪",
            "#f59e0b",
            280,
            0,
            status = BookStatus.NOT_STARTED
        ),
        Book(
            "6",
            "Biology: Life Sciences",
            "Biology",
            "Grade 10",
            "🌱",
            "#ec4899",
            320,
            0,
            status = BookStatus.NOT_STARTED
        ),
        Book(
            "7",
            "Computer Science",
            "ICT",
            "Grade 11",
            "💻",
            "#06b6d4",
            180,
            180,
            status = BookStatus.COMPLETED
        ),
        Book(
            "8",
            "Geography",
            "Geography",
            "Grade 10",
            "🗺️",
            "#84cc16",
            240,
            120,
            status = BookStatus.IN_PROGRESS
        )
    )

    val recentBooks get() = books.filter { it.status == BookStatus.IN_PROGRESS }.take(3)
    val recommendedBooks get() = books.filter { it.status == BookStatus.NOT_STARTED }.take(4)
    val completedBooks get() = books.filter { it.status == BookStatus.COMPLETED }
}
