package com.example.tma_semestralka.player

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tma_semestralka.R
import com.example.tma_semestralka.databinding.FragmentAddEditPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("DEPRECATION")
class AddEditPlayerFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddEditPlayerBinding
    private var listener: AddEditPlayerListener? = null
    private var player: Player? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as AddEditPlayerListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddEditPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            player = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                arguments?.getParcelable("player", Player::class.java)
            else
                arguments?.getParcelable("player")
        }
        player?.let { setExistingDataOnUi(it) }
        attachUiListener()
    }

    @SuppressLint("SetTextI18n")
    private fun setExistingDataOnUi(player: Player) {
        binding.playerFirstNameEditText.setText(player.firstName)
        binding.playerLastNameEditText.setText(player.lastName)
        binding.playerNumberEditText.setText(player.numberOfShirt.toString())
        binding.playerPositionEditText.setText(player.position)
        binding.playerGoalsEditText.setText(player.goals.toString())
        binding.playerAssistsEditText.setText(player.assists.toString())
        binding.playerYellowCardsEditText.setText(player.yellowCards.toString())
        binding.playerRedCardsEditText.setText(player.redCards.toString())
        binding.playerMinutesEditText.setText(player.minutesPlayed.toString())
        binding.saveBtn.text = "Update"
    }

    private fun attachUiListener() {
        binding.saveBtn.setOnClickListener {
            val firstName = binding.playerFirstNameEditText.text.toString()
            val lastName = binding.playerLastNameEditText.text.toString()
            val numberOfShirt = binding.playerNumberEditText.text.toString()
            val position = binding.playerPositionEditText.text.toString()
            val goals = binding.playerGoalsEditText.text.toString()
            val assists = binding.playerAssistsEditText.text.toString()
            val yellowCards = binding.playerYellowCardsEditText.text.toString()
            val redCards = binding.playerRedCardsEditText.text.toString()
            val minutesPlayed = binding.playerMinutesEditText.text.toString()
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && numberOfShirt.isNotEmpty() &&
                position.isNotEmpty() && goals.isNotEmpty() && assists.isNotEmpty() &&
                yellowCards.isNotEmpty() && redCards.isNotEmpty() && minutesPlayed.isNotEmpty()) {
                val player1 = Player(
                    player?.id ?: 0, firstName, lastName, numberOfShirt.toInt(), position,
                    goals.toInt(), assists.toInt(), yellowCards.toInt(), redCards.toInt(),
                    minutesPlayed.toInt()
                )
                Log.d("PlayerData", "Saving player: $player1")
                listener?.onSaveBtnClicked(player != null, player1)
            }
            dismiss()
        }
    }

    companion object {
        const val TAG = "AddEditPlayerFragment"

        @JvmStatic
        fun newInstance(player: Player?) = AddEditPlayerFragment().apply {
            arguments = Bundle().apply {
                putParcelable("player", player)
            }
        }
    }

    interface AddEditPlayerListener {
        fun onSaveBtnClicked(isUpdate: Boolean, player: Player)
    }
}