package com.example.tma_semestralka.player

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tma_semestralka.databinding.PlayerItemRowBinding

class PlayersAdapter(private val listener: PlayerDetailsClickListener) : ListAdapter<Player, PlayersAdapter.PlayerDetailsViewHolder>(DiffUtilCallback()) {

    inner class PlayerDetailsViewHolder(private val binding: PlayerItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.editBtn.setOnClickListener {
                listener.onEditPlayerClick(getItem(adapterPosition))
            }
            binding.deleteBtn.setOnClickListener {
                listener.onDeletePlayerClick(getItem(adapterPosition))
            }

            binding.infoBtn.setOnClickListener {
                listener.onInfoPlayerClick(getItem(adapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(player: Player) {
            binding.firstNameTv.text = player.firstName
            binding.lastNameTv.text = player.lastName
            binding.numberOfShirtTv.text = player.numberOfShirt.toString()
            binding.positionTv.text = player.position
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Player, newItem: Player) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerDetailsViewHolder {
        return PlayerDetailsViewHolder(PlayerItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlayerDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface PlayerDetailsClickListener {
        fun onEditPlayerClick(player: Player)
        fun onDeletePlayerClick(player: Player)
        fun onInfoPlayerClick(player: Player)
    }
}