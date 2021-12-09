package nl.hva.madlevel8pushalerts.models

import com.google.firebase.Timestamp

data class Task(
    val _id: String = "",
    val title: String = "",
    val description: String = "",
    val user: User?,
    val createdAt: Timestamp,
    val closedAt: Timestamp?,
    val number: Int,
    val source: String = "",
)