package com.example.tma_semestralka.admin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins WHERE email = :email")
    suspend fun getAdminByEmail(email: String): AdminEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdmin(admin: AdminEntity)
}