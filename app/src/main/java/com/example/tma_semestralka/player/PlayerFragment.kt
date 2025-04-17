package com.example.tma_semestralka.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.tma_semestralka.AppDatabase
import com.example.tma_semestralka.R
import com.example.tma_semestralka.databinding.FragmentPlayerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class PlayerFragment : Fragment(), AddEditPlayerFragment.AddEditPlayerListener,
    PlayersAdapter.PlayerDetailsClickListener {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlayersAdapter
    private var dao: PlayerDao? = null


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
            dao?.getAllPlayers()?.collect { players ->
                adapter.submitList(players)
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