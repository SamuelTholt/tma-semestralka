package com.example.tma_semestralka.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tma_semestralka.AppDatabase
import com.example.tma_semestralka.databinding.FragmentPlayersBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class PlayersFragment : Fragment(), AddEditPlayerFragment.AddEditPlayerListener,
    PlayerDetailsAdapter.PlayerDetailsClickListener {

    private var _binding: FragmentPlayersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlayerDetailsAdapter
    private var dao: PlayerDao? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = AppDatabase.getDatabase(requireContext()).playerDao()
        initRecyclerView()
        attachListeners()
        observePlayers()
    }

    private fun initRecyclerView() {
        adapter = PlayerDetailsAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun attachListeners() {
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun observePlayers() {
        lifecycleScope.launch {
            dao?.getAllPlayers()?.collect { players ->
                adapter.submitList(players)
            }
        }
    }

    private fun showBottomSheet(player: Player? = null) {
        val bottomSheet = AddEditPlayerFragment.newInstance(player)
        bottomSheet.setTargetFragment(this, 0)
        bottomSheet.show(parentFragmentManager, AddEditPlayerFragment.TAG)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
