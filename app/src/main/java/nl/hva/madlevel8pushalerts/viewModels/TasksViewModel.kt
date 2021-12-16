package nl.hva.madlevel8pushalerts.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.launch
import nl.hva.madlevel8pushalerts.models.Task
import nl.hva.madlevel8pushalerts.models.TaskDto
import nl.hva.madlevel8pushalerts.models.User
import nl.hva.madlevel8pushalerts.services.TasksRepository
import nl.hva.madlevel8pushalerts.services.TasksRetrievalError

class TasksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TasksRepository()

    var tasks: LiveData<List<Task>> = repository.tasks

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String> get() = _errorText

    fun getTasks(isRefresh: Boolean) {
        _errorText.value = null
        if (!isRefresh && repository.tasks.value != null) {
            return
        }

        viewModelScope.launch {
            try {
                repository.getTasks()
            } catch (error: TasksRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while fetching tasks", error.message.toString())
            }
        }
    }

    fun getMyTasks() {
        repository.getTasksByUser(Firebase.auth.currentUser!!.uid)
    }

    fun getUnassignedTasks() {
        repository.getTasksWithoutUser()
    }

    fun getOldTasks() {
        repository.getTasksOlderThanNumberOfDays(30)
    }

    fun addTask(title: String, description: String, source: String) {
        if (tasks.value == null)
            return

        viewModelScope.launch {
            try {
                val maxNumber = tasks.value!!.maxOf { t -> t.number }
                val newTask = Task(
                    "",
                    title,
                    description,
                    null,
                    Timestamp.now(),
                    null,
                    maxNumber + 1,
                    source
                )
                repository.insertTask(newTask)
                getTasks(true)
            } catch (error: TasksRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while inserting task", error.message.toString())
            }
        }

    }

    fun updateTask(taskId: String, field: String, value: Any) {
        viewModelScope.launch {
            try {
                repository.updateTask(taskId, field, value)
                getTasks(true)
            } catch (error: TasksRetrievalError) {
                _errorText.value = error.message
                Log.e("Error while updating task", error.message.toString())
            }
        }
    }
}
