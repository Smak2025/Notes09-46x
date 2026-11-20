package ru.gr09462.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gr09462.notes.R
import ru.gr09462.notes.domain.database.Note
import ru.gr09462.notes.ui.theme.NotesTheme
import java.sql.Array

@Composable
fun NewNotePanel(
    modifier: Modifier = Modifier,
    onSaveNote: (String, String)->Unit = { _,_ -> },
){
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    Surface(modifier = modifier){
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Заголовок заметки") }
            )
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Текст заметки") }
            )
            Button(onClick = { onSaveNote(title, text) }) {
                Text(stringResource(R.string.add_note))
            }
        }
    }
}

@Preview
@Composable
fun NewNotePanelPreview(){
    NotesTheme(darkTheme = true) {
        NewNotePanel(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    onDeleteNote: (Note)->Unit = {},
){

    Surface(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    note.title,
                    fontSize = 20.sp,
                )
                HorizontalDivider(thickness = 1.dp)
                Text(note.text)
            }
            TextButton(
                onClick = { onDeleteNote(note) },
                modifier = Modifier.padding(4.dp),
            ) {
                Text("Удалить")
            }
        }
    }
}

@Preview
@Composable
fun NoteCardPreview(){
    NotesTheme(darkTheme = true) {
        NoteCard(Note(0, "Заголовок заметки", "Текст заметки"))
    }
}