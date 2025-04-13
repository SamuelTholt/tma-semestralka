package com.example.tma_semestralka.player

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val numberOfShirt: Int,
    val position: String,
    val goals: Int,
    val assists: Int,
    val yellowCards: Int,
    val redCards: Int,
    val minutesPlayed: Int
) : Parcelable
