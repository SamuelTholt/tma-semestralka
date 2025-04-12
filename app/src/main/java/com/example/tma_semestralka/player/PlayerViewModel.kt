package com.example.tma_semestralka.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModel(
    private val dao: PlayerDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortTypePlayer.BY_FIRST_NAME)
    private val _state = MutableStateFlow(PlayerState())

    private val _players = _sortType.flatMapLatest { sortType ->
        when(sortType) {
            SortTypePlayer.BY_FIRST_NAME -> dao.getAllByFirstName()
            SortTypePlayer.BY_LAST_NAME -> dao.getAllByLastName()
            SortTypePlayer.BY_NUMBER_OF_SHIRT -> dao.getAllByNumberOfShirt()
            SortTypePlayer.BY_GOALS -> dao.getAllByGoals()
            SortTypePlayer.BY_ASSISTS -> dao.getAllByAssists()
            SortTypePlayer.BY_YELLOW_CARDS -> dao.getAllByYellowCards()
            SortTypePlayer.BY_RED_CARDS -> dao.getAllByRedCards()
            SortTypePlayer.BY_MINUTES_PLAYED -> dao.getAllByMinutes()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _players) { state, sortType, players ->
        state.copy(
            players = players,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlayerState())

    fun onEvent(event: PlayerEvent) {
        when(event) {
            PlayerEvent.HideDialog -> {
                _state.update { it.copy(isAddingPlayer = false) }
            }
            PlayerEvent.savePlayer -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val numberOfShirt = state.value.numberOfShirt
                val position = state.value.position
                val goals = state.value.goals
                val assists = state.value.assists
                val yellowCards = state.value.yellowCards
                val redCards = state.value.redCards
                val minutesPlayed = state.value.minutesPlayed
            }

            is PlayerEvent.setAssists -> {
                _state.update { it.copy(assists = event.assists) }
            }
            is PlayerEvent.setFirstName -> {
                _state.update { it.copy(firstName = event.firstName) }
            }
            is PlayerEvent.setGoals -> {
                _state.update { it.copy(goals = event.goals) }
            }
            is PlayerEvent.setLastName -> {
                _state.update { it.copy(lastName = event.lastName) }
            }
            is PlayerEvent.setMinutesPlayed -> {
                _state.update { it.copy(minutesPlayed = event.minutesPlayed) }
            }
            is PlayerEvent.setPosition -> {
                _state.update { it.copy(position = event.position) }
            }
            is PlayerEvent.setRedCards -> {
                _state.update { it.copy(redCards = event.redCards) }
            }
            is PlayerEvent.setYellowCards -> {
                _state.update { it.copy(yellowCards = event.yellowCards) }
            }
            PlayerEvent.showDialog -> {
                _state.update { it.copy(isAddingPlayer = true) }
            }
            is PlayerEvent.sortPlayers -> {
                _sortType.value = event.sortType
            }
            is PlayerEvent.deletePlayer -> {
                viewModelScope.launch {
                    dao.deletePlayer(event.player)
                }
            }
        }
    }
}