package nl.hva.madlevel8pushalerts.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.hva.madlevel8pushalerts.databinding.FragmentTabOpenBinding

class TabOpenFragment : Fragment() {
    private var _binding: FragmentTabOpenBinding? = null
    private val binding get() = _binding!!
//    private val recyclerViewAdapter = AttachmentsAdapter(attachments)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun initRecyclerView() {
//        binding.rvTabAttachments.layoutManager =
//            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        binding.rvTabAttachments.adapter = recyclerViewAdapter
//        binding.rvTabAttachments.isNestedScrollingEnabled = false
//        binding.rvTabAttachments.addItemDecoration(
//            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
//        )
//    }
}