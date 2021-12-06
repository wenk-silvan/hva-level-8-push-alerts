package nl.hva.madlevel8pushalerts.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.hva.madlevel8pushalerts.models.Task
import nl.hva.madlevel8pushalerts.models.TaskDto
import nl.hva.madlevel8pushalerts.models.User
import nl.hva.madlevel8pushalerts.services.TasksRepository
import nl.hva.madlevel8pushalerts.services.TasksRetrievalError

class TasksViewModel (application: Application) : AndroidViewModel(application) {
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

    private fun fromDto(t: TaskDto): Task {
        return Task(
            t._id,
            t.title,
            t.description,
            User(),
            t.createdAt,
            t.closedAt,
            t.number
        )
    }
}
