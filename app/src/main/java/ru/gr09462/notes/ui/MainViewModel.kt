package ru.gr09462.notes.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.gr09462.notes.domain.database.Note
import ru.gr09462.notes.domain.database.NoteDao
import ru.gr09462.notes.domain.database.NotesDatabase

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db: NotesDatabase = NotesDatabase.getInstance(app.applicationContext)
    private val noteDao: NoteDao = db.noteDao()

    val notes: Flow<List<Note>> = noteDao.getAll()

    fun saveNote(title: String, text: String){
        Note(title = title, text = text).also { note ->
            viewModelScope.launch {
                noteDao.insert(note)
            }
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }

}