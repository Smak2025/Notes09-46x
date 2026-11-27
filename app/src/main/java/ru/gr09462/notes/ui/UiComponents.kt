package ru.gr09462.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import ru.gr09462.notes.domain.database.Category
import ru.gr09462.notes.domain.database.Note
import ru.gr09462.notes.domain.database.NoteWithCategory
import ru.gr09462.notes.ui.theme.NotesTheme
import java.sql.Array
import kotlin.collections.forEach

@Composable
fun EditNotePanel(
    note: NoteWithCategory?,
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onSaveNote: (String, String, Long) -> Unit = { _, _, _ -> },
) {
    var title by remember(note) { mutableStateOf(note?.note?.title ?: "") }
    var text by remember(note) { mutableStateOf(note?.note?.text ?: "") }
    var categoryId by remember(note) { mutableLongStateOf(note?.note?.categoryId ?: 1L) } // «Без категории» есть всегда

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { category ->
                    FilterChip(
                        selected = category.id == categoryId,
                        onClick = { categoryId = category.id },
                        label = { Text(category.name) },
                    )
                }
            }
            Button(
                onClick = {
                    onSaveNote(title, text, categoryId)
                    title = ""; text = ""
                },
                enabled = title.isNotBlank(),
            ) { Text(stringResource(R.string.add_note)) }
        }
    }
}

@Composable
fun NoteCard(
    note: NoteWithCategory,
    modifier: Modifier = Modifier,
    onEditNote: (NoteWithCategory)->Unit = {},
    onDeleteNote: (Note) -> Unit = {},
) {
    Surface(modifier = modifier.clickable(onClick = {
        onEditNote(note)
    })) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier.weight(1f).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(note.note.title, fontSize = 20.sp)
                Text(
                    note.category.name,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                HorizontalDivider(thickness = 1.dp)
                Text(note.note.text)
            }
            TextButton(onClick = { onDeleteNote(note) }, modifier = Modifier.padding(4.dp)) {
                Text("Удалить")
            }
        }
    }
}

@Preview
@Composable
fun NoteCardPreview() {
    NotesTheme(darkTheme = true) {
        NoteCard(NoteWithCategory(Note(0, "Заголовок", "Текст"), Category(1, "Учёба")))
    }
}