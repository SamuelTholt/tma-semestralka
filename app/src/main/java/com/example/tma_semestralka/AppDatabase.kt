package com.example.tma_semestralka

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tma_semestralka.player.Player
import com.example.tma_semestralka.player.PlayerDao
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@Database(entities = [Player::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun playerDao() : PlayerDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "FootballAppDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
