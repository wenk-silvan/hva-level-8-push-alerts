package nl.hva.madlevel8pushalerts.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import nl.hva.madlevel8pushalerts.databinding.FragmentTabOpenBinding
import nl.hva.madlevel8pushalerts.models.Task
import nl.hva.madlevel8pushalerts.viewModels.TasksViewModel

class TabOpenFragment : Fragment() {
    private var _binding: FragmentTabOpenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TasksViewModel by activityViewModels()
    private val tasks: ArrayList<Task> = arrayListOf()
    private lateinit var recyclerViewAdapter: OpenTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tasks.value?.let { tasks.addAll(it) }
        initChipGroup()
        initRecyclerView()
        observeTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initChipGroup() {
        binding.chipFilterMine.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.getMyTasks()
            else viewModel.getTasks(true)
        }
        binding.chipFilterUnassigned.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.getUnassignedTasks()
            else viewModel.getTasks(true)
        }
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = OpenTasksAdapter(
            tasks,
            { t: Task -> onClickBtnAssign(t) },
            { t: Task -> onClickBtnClose(t) },
            { t: Task -> onClickBtnUnassign(t) },
            { t: Task -> onClickCard(t) })
        binding.rvTasks.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvTasks.adapter = recyclerViewAdapter
        binding.rvTasks.isNestedScrollingEnabled = false
        binding.rvTasks.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
    }

    private fun observeTasks() {
        viewModel.tasks.observe(viewLifecycleOwner) {
            tasks.clear()
            tasks.addAll(it.filter { t -> t.closedAt == null })
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun onClickBtnAssign(task: Task) {
        viewModel.updateTask(task._id, "userId", Firebase.auth.currentUser!!.uid)
    }

    private fun onClickBtnClose(task: Task) {
        viewModel.updateTask(task._id, "closedAt", Timestamp.now())
    }

    private fun onClickBtnUnassign(task: Task) {
        viewModel.updateTask(task._id, "userId", "")
    }

    private fun onClickCard(task: Task) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(task.title)
            .setMessage(task.description)
            .show()
    }
}