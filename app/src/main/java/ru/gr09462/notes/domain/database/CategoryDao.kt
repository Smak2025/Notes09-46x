package ru.gr09462.notes.domain.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY id")
    fun getAll(): Flow<List<Category>>

    @Insert
    suspend fun insert(category: Category): Long
    @Delete
    suspend fun delete(category: Category)
}