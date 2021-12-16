package nl.hva.madlevel8pushalerts.ui.tasks

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import nl.hva.madlevel8pushalerts.R
import nl.hva.madlevel8pushalerts.databinding.ItemTaskOpenBinding
import nl.hva.madlevel8pushalerts.models.Task
import java.text.SimpleDateFormat
import java.util.*

class OpenTasksAdapter(
    private val tasks: List<Task>,
    val onClickBtnAssign: (Task) -> Unit,
    val onClickBtnClose: (Task) -> Unit,
    val onClickBtnUnassign: (Task) -> Unit,
    val onClickCard: (Task) -> Unit,
) :
    RecyclerView.Adapter<OpenTasksAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTaskOpenBinding.bind(itemView)

        fun databind(task: Task) {
            binding.tvTaskName.text = task.title
            binding.tvTaskCreatedAt.text = formatTimestamp(task.createdAt)
            binding.tvTaskNumber.text = "#${task.number}"
            binding.tvTaskDescription.text = shortDescription(task.description)
            binding.tvTaskSource.text = task.source

            binding.btnAssign.setOnClickListener { onClickBtnAssign(task) }
            binding.btnClose.setOnClickListener { onClickBtnClose(task) }
            binding.btnUnassign.setOnClickListener { onClickBtnUnassign(task) }
            binding.mcvChapter.setOnClickListener { onClickCard(task) }

            if (task.user == null) {
                binding.btnAssign.visibility = View.VISIBLE
                binding.tvAssigned.visibility = View.GONE
                binding.btnUnassign.visibility = View.GONE
                binding.btnClose.visibility = View.GONE
                binding.mcvChapter.setCardBackgroundColor(Color.parseColor("#f3f6f4"))
            } else {
                binding.mcvChapter.setCardBackgroundColor(Color.parseColor("#ffffff"))
                binding.btnAssign.visibility = View.GONE
                binding.tvAssigned.visibility = View.VISIBLE
                binding.tvAssigned.text = task.user.name
                if (task.user._id == Firebase.auth.currentUser!!.uid) {
                    binding.btnUnassign.visibility = View.VISIBLE
                    binding.btnClose.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task_open, parent, false)
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

    private fun shortDescription(description: String): String {
        return if (description.length > 25) description.substring(0, 25) + "..." else description
    }
}