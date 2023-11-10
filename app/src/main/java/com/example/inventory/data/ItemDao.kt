package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    // funcionalidades automaticas so existe essas 3, as outras tem q fz
    @Insert
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItem(id: Int): Flow<Item?> // por retornar um Flow ja eh suspense

    @Query("SELECT * FROM items ORDER BY name")
    fun getAllItems(): Flow<List<Item>>
}