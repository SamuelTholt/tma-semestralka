package com.example.tma_semestralka.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    fun getAll(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY firstName ASC")
    fun getAllByFirstName(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY lastName ASC")
    fun getAllByLastName(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY numberOfShirt ASC")
    fun getAllByNumberOfShirt(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY position ASC")
    fun getAllByPositon(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY goals ASC")
    fun getAllByGoals(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY assists ASC")
    fun getAllByAssists(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY yellowCards ASC")
    fun getAllByYellowCards(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY redCards ASC")
    fun getAllByRedCards(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY minutesPlayed ASC")
    fun getAllByMinutes(): Flow<List<Player>>

    @Insert
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)
}