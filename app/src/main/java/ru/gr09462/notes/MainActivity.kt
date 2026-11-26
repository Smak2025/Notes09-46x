package ru.gr09462.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.gr09462.notes.ui.MainViewModel
import ru.gr09462.notes.ui.EditNotePanel
import ru.gr09462.notes.ui.NoteCard
import ru.gr09462.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val notes by viewModel.notes.collectAsState(listOf())

            NotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        EditNotePanel(
                            viewModel.currentNote,
                            modifier = Modifier.fillMaxWidth(),
                            onSaveNote = { title, text ->
                                viewModel.saveNote(title, text)
                            }
                        )
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(notes){
                                NoteCard(
                                    it,
                                    onEditNote = { note ->
                                        viewModel.editNote(note)
                                    },
                                    onDeleteNote = { note ->
                                        viewModel.deleteNote(note)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

