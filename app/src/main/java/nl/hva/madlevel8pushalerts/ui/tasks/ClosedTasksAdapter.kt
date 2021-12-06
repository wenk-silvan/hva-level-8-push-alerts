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
            binding.tvTaskName.text = task.title
            binding.tvTaskCreatedAt.text = formatTimestamp(task.createdAt)
            binding.tvTaskNumber.text = "#${task.number}"
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

    private fun formatTimestamp(timestamp: Timestamp): String {
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MM-yyyy - hh:mm", Locale.getDefault())
        return sdf.format(Date(milliseconds)).toString()
    }
}