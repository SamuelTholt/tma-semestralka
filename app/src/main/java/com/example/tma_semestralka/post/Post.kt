package com.example.tma_semestralka.post

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postHeader: String,
    val postText: String,
    val postDate: Long = System.currentTimeMillis()
) : Parcelable
