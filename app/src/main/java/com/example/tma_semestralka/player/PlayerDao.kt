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

    @Query("SELECT * FROM players WHERE id = :id")
    suspend fun getPlayerById(id: Int): Player?

    @Query("SELECT * FROM players WHERE LOWER(firstName || ' ' || lastName) LIKE '%' || LOWER(:query) || '%'")
    fun searchPlayers(query: String): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY firstName ASC")
    fun getPlayersSortedByFirstName(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY lastName ASC")
    fun getPlayersSortedByLastName(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY CAST(numberOfShirt AS INTEGER) ASC")
    fun getPlayersSortedByNumber(): Flow<List<Player>>

    @Query("SELECT * FROM players ORDER BY position ASC")
    fun getPlayersSortedByPosition(): Flow<List<Player>>

}