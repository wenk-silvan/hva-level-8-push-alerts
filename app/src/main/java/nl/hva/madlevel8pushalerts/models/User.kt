package nl.hva.madlevel8pushalerts.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    @DocumentId val _id: String = "",
    val name: String = "",
    val email: String = "",
    ) : Parcelable {
    constructor() : this("", "", "")
}