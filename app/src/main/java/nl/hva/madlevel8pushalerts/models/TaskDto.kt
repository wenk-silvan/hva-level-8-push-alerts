package nl.hva.madlevel8pushalerts.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskDto(
    @DocumentId val _id: String = "",
    val title: String = "",
    val description: String = "",
    val userId: String,
    val createdAt: Timestamp,
    val closedAt: Timestamp?,
    val number: Int,
) : Parcelable {
    constructor() : this("", "", "", "", Timestamp.now(), Timestamp.now(), 0)
}