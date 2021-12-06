package nl.hva.madlevel8pushalerts.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import nl.hva.madlevel8pushalerts.models.Task
import nl.hva.madlevel8pushalerts.models.TaskDto
import nl.hva.madlevel8pushalerts.models.User

class TasksRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val taskCollection = firestore.collection(("tasks"))
    private val userCollection = firestore.collection(("users"))
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()

    val tasks: LiveData<List<Task>> get() = _tasks

    suspend fun getTasks() {
        try {
            val tempList = arrayListOf<Task>()
            val users = getUsers()
            withTimeout(5_000) {
                val data = taskCollection.get().await()
                data.documents.forEach { doc ->
                    val task = doc.toObject(TaskDto::class.java)
                    if (task != null)
                        tempList.add(fromDto(task, users))
                }
            }
            _tasks.value = tempList
        } catch (e: Exception) {
            throw TasksRetrievalError("Error while fetching tasks from Firestore: \n${e.message}")
        }
    }

    suspend fun updateTask(id: String, field: String, value: Any) {
        taskCollection.document(id).update(field, value).await()
    }

    private suspend fun getUsers(): ArrayList<User> {
        val users = arrayListOf<User>()
        withTimeout(5_000) {
            val data = userCollection.get().await()
            data.documents.forEach { doc ->
                val task = doc.toObject(User::class.java)
                if (task != null)
                    users.add(task)
            }
        }
        return users
    }

    private fun fromDto(t: TaskDto, users: ArrayList<User>): Task {
        return Task(
            t._id,
            t.title,
            t.description,
            users.firstOrNull() { u -> u._id == t.userId },
            t.createdAt,
            t.closedAt,
            t.number
        )
    }
}

class TasksRetrievalError(message: String) : Exception(message)