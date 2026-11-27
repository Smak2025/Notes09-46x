package ru.gr09462.notes.domain.database

import androidx.compose.foundation.layout.FlowRowOverflowScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Transaction
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): Flow<List<NoteWithCategory>>

    @Transaction
    @Query("SELECT * FROM notes WHERE categoryId=:categoryId ORDER BY id DESC")
    fun getByCategory(categoryId: Long): Flow<List<NoteWithCategory>>
}