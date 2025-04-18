package com.example.tma_semestralka.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.tma_semestralka.AppDatabase
import com.example.tma_semestralka.R
import com.example.tma_semestralka.databinding.FragmentPlayerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class PlayerFragment : Fragment(), AddEditPlayerFragment.AddEditPlayerListener,
    PlayersAdapter.PlayerDetailsClickListener {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlayersAdapter
    private var dao: PlayerDao? = null

    private var currentSort: String = "Číslo"
    private var currentQuery: String = ""
    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = AppDatabase.getDatabase(requireContext()).playerDao()
        initRecyclerView()
        attachListeners()
        observePlayers()

        setupSearchView()

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currentSort = parent.getItemAtPosition(position).toString()
                observePlayers()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300)
                    currentQuery = newText.orEmpty()
                    observePlayers()
                }
                return true
            }
        })
    }

    private fun initRecyclerView() {
        adapter = PlayersAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun attachListeners() {
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet(player: Player? = null) {
        val bottomSheet = AddEditPlayerFragment.newInstance(player)
        bottomSheet.setTargetFragment(this, 0)
        bottomSheet.show(parentFragmentManager, AddEditPlayerFragment.TAG)
    }

    private fun observePlayers() {
        lifecycleScope.launch {
            when {
                currentQuery.isNotEmpty() -> {
                    dao?.searchPlayers(currentQuery)?.collect { players ->
                        adapter.submitList(players)
                    }
                }
                else -> {
                    when (currentSort) {
                        "Číslo" -> dao?.getPlayersSortedByNumber()
                        "Meno" -> dao?.getPlayersSortedByFirstName()
                        "Priezvisko" -> dao?.getPlayersSortedByLastName()
                        "Pozícia" -> dao?.getPlayersSortedByPosition()
                        else -> dao?.getAllPlayers()
                    }?.collect { players ->
                        adapter.submitList(players)
                    }
                }
            }
        }
    }
    override fun onSaveBtnClicked(isUpdate: Boolean, player: Player) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (isUpdate)
                dao?.updatePlayer(player)
            else
                dao?.savePlayer(player)
        }
    }

    override fun onEditPlayerClick(player: Player) {
        showBottomSheet(player)
    }

    override fun onDeletePlayerClick(player: Player) {
        lifecycleScope.launch(Dispatchers.IO) {
            dao?.deletePlayerById(player.id)
        }
    }

    override fun onInfoPlayerClick(player: Player) {
        val action = PlayerFragmentDirections.actionPlayerFragmentToPlayerInfoFragment(player.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}