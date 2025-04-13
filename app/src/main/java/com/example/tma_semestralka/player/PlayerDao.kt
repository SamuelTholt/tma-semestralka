package com.example.tma_semestralka.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)

    @Query("DELETE FROM players WHERE id =:pId")
    suspend fun deletePlayerById(pId : Int)

    @Query("SELECT * FROM players")
    fun getAllPlayers() : Flow<List<Player>>
}