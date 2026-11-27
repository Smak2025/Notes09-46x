package ru.gr09462.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.gr09462.notes.domain.database.Note
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

            val notes by viewModel.notes.collectAsState(emptyList())
            val categories by viewModel.categories.collectAsState(emptyList())
            val filter by viewModel.filter.collectAsState()

            NotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        EditNotePanel(
                            viewModel.currentNote,
                            categories = categories,
                            modifier = Modifier.fillMaxWidth(),
                            onSaveNote = { title, text, categoryId ->
                                viewModel.saveNote(title, text, categoryId)
                            }
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            FilterChip(
                                selected = filter == 0L,
                                onClick = { viewModel.setFilter(0L) },
                                label = { Text("Все") },
                            )
                            categories.forEach { c ->
                                FilterChip(
                                    selected = filter == c.id,
                                    onClick = { viewModel.setFilter(c.id) },
                                    label = { Text(c.name) },
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(notes, key = { it.note.id }){
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

