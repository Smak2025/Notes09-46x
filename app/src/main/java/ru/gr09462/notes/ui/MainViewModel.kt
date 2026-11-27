package ru.gr09462.notes.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.gr09462.notes.domain.database.Category
import ru.gr09462.notes.domain.database.Note
import ru.gr09462.notes.domain.database.NoteDao
import ru.gr09462.notes.domain.database.NoteWithCategory
import ru.gr09462.notes.domain.database.NotesDatabase

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db: NotesDatabase = NotesDatabase.getInstance(app.applicationContext)
    private val noteDao: NoteDao = db.noteDao()
    private val categoryDao = db.categoryDao()

    val categories: Flow<List<Category>> = categoryDao.getAll()

    private val _filter = MutableStateFlow(0L)   // 0L = «все категории»
    val filter: StateFlow<Long> = _filter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: Flow<List<NoteWithCategory>> = _filter.flatMapLatest { id ->
        if (id == 0L) noteDao.getAll() else noteDao.getByCategory(id)
    }

    fun setFilter(categoryId: Long) { _filter.value = categoryId }

    fun saveNote(title: String, text: String, categoryId: Long){
        Note(title = title, text = text, categoryId = categoryId).also { note ->
            viewModelScope.launch {
                noteDao.insert(note)
            }
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch { noteDao.delete(note) }
    }

}