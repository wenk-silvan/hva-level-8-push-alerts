package nl.hva.madlevel8pushalerts.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

data class Task(
    val _id: String = "",
    val title: String = "",
    val description: String = "",
    val user: User?,
    val createdAt: Timestamp,
    val closedAt: Timestamp?,
    val number: Int)