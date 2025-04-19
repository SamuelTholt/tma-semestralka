package com.example.tma_semestralka.post

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("DELETE FROM posts WHERE id =:pId")
    suspend fun deletePostById(pId : Int)

    @Query("SELECT * FROM posts")
    fun getAllPosts() : Flow<List<Post>>

    @Query("SELECT * FROM posts ORDER BY postDate ASC")
    fun getPostsByDateAsc(): Flow<List<Post>>

    @Query("SELECT * FROM posts ORDER BY postDate DESC")
    fun getPostsByDateDesc(): Flow<List<Post>>
}