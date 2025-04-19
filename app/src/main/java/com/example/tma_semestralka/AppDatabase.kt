package com.example.tma_semestralka

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.tma_semestralka.admin.AdminDao
import com.example.tma_semestralka.admin.AdminEntity
import com.example.tma_semestralka.player.Player
import com.example.tma_semestralka.player.PlayerDao
import com.example.tma_semestralka.post.Post
import com.example.tma_semestralka.post.PostDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Player::class, AdminEntity::class, Post::class], version = 2)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun playerDao() : PlayerDao
    abstract fun adminDao() : AdminDao
    abstract fun postDao() : PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "FootballAppDatabase"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            Log.d("AppDatabase", "onCreate called")

                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val adminDao = database.adminDao()
                                    val hashPassword = BCrypt.withDefaults()
                                        .hashToString(12, "admin123".toCharArray())
                                    val admin = AdminEntity(
                                        email = "admin@admin.com",
                                        password = hashPassword)
                                    adminDao.insertAdmin(admin)

                                    Log.d("AppDatabase", "Admin inserted")

                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration(true)
                    .build()
                    INSTANCE = instance
                    return instance
            }
        }

    }
}
