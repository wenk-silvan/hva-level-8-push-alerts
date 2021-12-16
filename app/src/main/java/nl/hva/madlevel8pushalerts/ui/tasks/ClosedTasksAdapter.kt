package nl.hva.madlevel8pushalerts.ui.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import nl.hva.madlevel8pushalerts.R
import nl.hva.madlevel8pushalerts.databinding.ItemTaskClosedBinding
import nl.hva.madlevel8pushalerts.databinding.ItemTaskOpenBinding
import nl.hva.madlevel8pushalerts.models.Task
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ClosedTasksAdapter(
    private val tasks: List<Task>
) :
    RecyclerView.Adapter<ClosedTasksAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTaskClosedBinding.bind(itemView)

        fun databind(task: Task) {
            if (task.closedAt == null) {
                throw Exception("Closed task must have a closedAt timestamp.")
            }
            binding.tvTaskName.text = task.title
            binding.tvTaskCreatedAt.text = formatTimestamp(task.createdAt, "Created: ")
            binding.tvTaskClosedAt.text = formatTimestamp(task.closedAt, "Closed: ")
            binding.tvTaskNumber.text = "#${task.number}"
            binding.tvAssigned.text = task.user!!.name
            binding.tvTaskDescription.text = shortDescription(task.description)
            binding.tvTaskSource.text = task.source
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task_closed, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(tasks[position])
    }

    private fun formatTimestamp(timestamp: Timestamp, prefix: String = ""): String {
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MM-yyyy - hh:mm", Locale.getDefault())
        return prefix + sdf.format(Date(milliseconds)).toString()
    }

    private fun shortDescription(description: String): String {
        return if (description.length > 20) description.substring(0, 20) + "..." else description
    }
}