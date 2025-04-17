package com.example.tma_semestralka.player

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.tma_semestralka.AppDatabase
import com.example.tma_semestralka.databinding.FragmentPlayerInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerInfoFragment : Fragment() {
    private var playerId: Int = -1
    private var _binding: FragmentPlayerInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var playerDao: PlayerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playerId = it.getInt("player_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerDao = AppDatabase.getDatabase(requireContext()).playerDao()

        // Načítať údaje hráča z databázy
        lifecycleScope.launch {
            val player = withContext(Dispatchers.IO) {
                playerDao.getPlayerById(playerId)
            }
            player?.let {
                binding.firstNameTv.text = it.firstName
                binding.lastNameTv.text = it.lastName
                binding.numberOfShirtTv.text = it.numberOfShirt.toString()
                binding.positionTv.text = it.position
                binding.goalsTv.text = it.goals.toString()
                binding.assistsTv.text = it.assists.toString()
                binding.yellowCardsTv.text = it.yellowCards.toString()
                binding.redCardsTv.text = it.redCards.toString()
                binding.minutesPlayedTv.text = it.minutesPlayed.toString()
            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        fun newInstance(playerId: Int): PlayerInfoFragment {
            val fragment = PlayerInfoFragment()
            val args = Bundle()
            args.putInt("player_id", playerId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}