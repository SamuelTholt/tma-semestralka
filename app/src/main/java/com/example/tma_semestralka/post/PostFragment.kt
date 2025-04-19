package com.example.tma_semestralka.post

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tma_semestralka.AppDatabase
import com.example.tma_semestralka.databinding.FragmentPostBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class PostFragment : Fragment(), AddEditPostFragment.AddEditPostListener,
PostAdapter.PostDetailsClickListener {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PostAdapter
    private var dao: PostDao? = null

    private var currentSort: String = "Zostupne"

    private var isAdmin: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        isAdmin = sharedPref.getBoolean("isAdminLoggedIn", false)

        dao = AppDatabase.getDatabase(requireContext()).postDao()
        initRecyclerView()
        attachListeners()
        observePosts()

        binding.floatingActionButton.visibility = if (isAdmin) View.VISIBLE else View.GONE


        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currentSort = parent.getItemAtPosition(position).toString()
                observePosts()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initRecyclerView() {
        adapter = PostAdapter(this, isAdmin)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun attachListeners() {
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet(post: Post? = null) {
        val bottomSheet = AddEditPostFragment.newInstance(post)
        bottomSheet.setTargetFragment(this, 0)
        bottomSheet.show(parentFragmentManager, AddEditPostFragment.TAG)
    }

    private fun observePosts() {
        lifecycleScope.launch {
            when(currentSort) {
                "Vzostupne" -> dao?.getPostsByDateAsc()
                "Zostupne" -> dao?.getPostsByDateDesc()
                else -> dao?.getAllPosts()
            }?.collect { posts ->
                adapter.submitList(posts)
            }
        }
    }

    override fun onSaveBtnClicked(isUpdate: Boolean, post: Post) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (isUpdate)
                dao?.updatePost(post)
            else
                dao?.savePost(post)
        }
    }

    override fun onEditPostClick(post: Post) {
        showBottomSheet(post)
    }

    override fun onDeletePostClick(post: Post) {
        lifecycleScope.launch(Dispatchers.IO) {
            dao?.deletePostById(post.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}