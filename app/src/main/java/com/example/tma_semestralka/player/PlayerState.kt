package com.example.tma_semestralka.player
import kotlin.collections.emptyList

data class PlayerState(
    val players: List<Player> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val numberOfShirt: Int = 0,
    val position: String = "",
    val goals: Int = 0,
    val assists: Int = 0,
    val yellowCards: Int = 0,
    val redCards: Int = 0,
    val minutesPlayed: Int = 0,
    val isAddingPlayer: Boolean = false,
    val sortType: SortTypePlayer = SortTypePlayer.BY_FIRST_NAME // Default sort type
)
