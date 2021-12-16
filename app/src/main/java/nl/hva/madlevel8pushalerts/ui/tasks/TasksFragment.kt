package nl.hva.madlevel8pushalerts.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import nl.hva.madlevel8pushalerts.R
import nl.hva.madlevel8pushalerts.databinding.FragmentTasksBinding
import nl.hva.madlevel8pushalerts.services.EVENTS
import nl.hva.madlevel8pushalerts.viewModels.TasksViewModel

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTasks(false)
        initTabLayout()
        observeNotification()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initTabLayout() {
        val fm = requireActivity().supportFragmentManager
        binding.viewPager.adapter = TabLayoutFragmentAdapter(fm, lifecycle)
        binding.tabLayout.removeAllTabs()
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.tiTasksOpen)).setIcon(R.drawable.ic_baseline_list_24)
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.tiTasksClosed)).setIcon(R.drawable.ic_baseline_history_24)
        )

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun observeNotification() {
        EVENTS.newNotification.observe(viewLifecycleOwner) {
            viewModel.addTask(it.title, it.description, it.source)
        }
    }
}