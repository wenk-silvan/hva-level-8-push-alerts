package nl.hva.madlevel8pushalerts.models

data class NotificationEvent(
    val title: String,
    val description: String,
    val source: String,
)