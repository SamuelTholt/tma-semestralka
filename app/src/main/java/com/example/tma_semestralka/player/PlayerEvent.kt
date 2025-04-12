package com.example.tma_semestralka.player

sealed interface PlayerEvent {
    object savePlayer : PlayerEvent
    data class setFirstName(val firstName: String) : PlayerEvent
    data class setLastName(val lastName: String) : PlayerEvent
    data class setPosition(val position: String) : PlayerEvent
    data class setGoals(val goals: Int) : PlayerEvent
    data class setAssists(val assists: Int) : PlayerEvent
    data class setYellowCards(val yellowCards: Int) : PlayerEvent
    data class setRedCards(val redCards: Int) : PlayerEvent
    data class setMinutesPlayed(val minutesPlayed: Int) : PlayerEvent

    object showDialog: PlayerEvent
    object HideDialog: PlayerEvent
    data class sortPlayers(val sortType: SortTypePlayer) : PlayerEvent
    data class deletePlayer(val player: Player) : PlayerEvent
}