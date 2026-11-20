package ru.gr09462.notes.domain.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var title: String,
    var text: String,
)