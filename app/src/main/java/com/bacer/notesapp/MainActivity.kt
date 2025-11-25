package com.bacer.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bacer.notesapp.ui.subjects.SubjectScreen
import com.bacer.notesapp.ui.grades.GradeScreen
import com.bacer.notesapp.ui.theme.NotesAppTheme
import com.bacer.notesapp.data.database.DatabaseInstance
import com.bacer.notesapp.data.subjects.SubjectRepository
import com.bacer.notesapp.data.grades.GradeRepository
import com.bacer.notesapp.ui.notes.NotesScreen
import com.bacer.notesapp.viewmodel.subjects.SubjectViewModel
import com.bacer.notesapp.viewmodel.subjects.SubjectViewModelFactory
import com.bacer.notesapp.viewmodel.grades.GradeViewModel
import com.bacer.notesapp.viewmodel.grades.GradeViewModelFactory
import com.bacer.notesapp.viewmodel.notes.NoteViewModelFactory
import com.bacer.notesapp.data.notes.NoteRepository
import com.bacer.notesapp.ui.notes.NoteContentScreen
import com.bacer.notesapp.viewmodel.notes.NoteViewModel
import com.bacer.notesapp.viewmodel.notes.NoteContentViewModel
import com.bacer.notesapp.viewmodel.notes.NoteContentViewModelFactory

class MainActivity : ComponentActivity() {

    // ----- ViewModels (singletons for the whole app) -----
    private val subjectViewModel: SubjectViewModel by viewModels {
        SubjectViewModelFactory(
            SubjectRepository(DatabaseInstance.getDatabase(this).subjectDao())
        )
    }

    private val gradeViewModel: GradeViewModel by viewModels {
        GradeViewModelFactory(
            GradeRepository(DatabaseInstance.getDatabase(this).gradeDao())
        )
    }

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(
            NoteRepository(DatabaseInstance.getDatabase(this).noteDao())
        )
    }

    private val noteContentViewModel: NoteContentViewModel by viewModels {
        NoteContentViewModelFactory(
            NoteRepository(DatabaseInstance.getDatabase(this).noteDao())
        )
    }
    // ----------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Load subjects once (the list is a StateFlow, UI will react automatically)
        subjectViewModel.loadSubjects()

        setContent {
            NotesAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "subjects"
                ) {
                    // ---------- Subjects ----------
                    composable("subjects") {
                        SubjectScreen(
                            subjects = subjectViewModel.subjects,
                            onAddSubject = { subjectViewModel.addSubject(it) },
                            onDeleteSubject = { subjectViewModel.deleteSubject(it) },
                            nameError = subjectViewModel.nameError,
                            onClearNameError = { subjectViewModel.clearNameError() },

                            onGradesClick = { subjectId ->
                                navController.navigate("grades/$subjectId")
                            },
                            onNotesClick = { subjectId ->
                                navController.navigate("notes/$subjectId")
                            }
                        )
                    }

                    // ---------- Grades ----------
                    composable("grades/{subjectId}") { backStackEntry ->
                        val subjectId = backStackEntry.arguments?.getString("subjectId")!!.toInt()

                        LaunchedEffect(subjectId) {
                            gradeViewModel.loadGrades(subjectId)
                        }

                        val subject by subjectViewModel.getSubjectById(subjectId)
                            .collectAsState(initial = null)

                        GradeScreen(
                            grades = gradeViewModel.grades,
                            subjectName = subject?.name ?: "Grades",
                            onBack = { navController.popBackStack() },
                            onAddGrade = { name, value ->
                                gradeViewModel.addGrade(subjectId, name, value)
                            },
                            onDeleteGrade = { grade ->
                                gradeViewModel.deleteGrade(grade, subjectId)
                            },

                            nameError = gradeViewModel.nameError,
                            onClearNameError = { gradeViewModel.clearNameError() }
                        )
                    }

                    // ---------- Notes ----------
                    composable("notes/{subjectId}") { backStackEntry ->
                        val subjectId = backStackEntry.arguments?.getString("subjectId")!!.toInt()

                        LaunchedEffect(subjectId) {
                            noteViewModel.loadNotes(subjectId)
                        }

                        val subject by subjectViewModel.getSubjectById(subjectId)
                            .collectAsState(initial = null)

                        NotesScreen(
                            notes = noteViewModel.notes,
                            subjectName = subject?.name ?: "Notes",
                            onBack = { navController.popBackStack() },
                            onAddNote = { name, images ->
                                noteViewModel.addNote(subjectId, name, images)
                            },
                            onDeleteNote = { note ->
                                noteViewModel.deleteNote(note, subjectId)
                            },

                            nameError = noteViewModel.nameError,
                            onClearNameError = { noteViewModel.clearNameError() },

                            imageError = noteViewModel.imageError,
                            onClearImageError = { noteViewModel.clearImageError() },

                            onContentClick = { noteId ->
                                navController.navigate("noteContent/$noteId")
                            },
                        )
                    }

                    // ---------- Note Content ----------
                    composable("noteContent/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")!!.toInt()

                        LaunchedEffect(noteId) {
                            noteContentViewModel.loadNote(noteId)
                        }

                        val note by noteContentViewModel.note.collectAsState()

                        NoteContentScreen(
                            noteName = note?.name ?: "Content",
                            imageUris = note?.imageUris ?: emptyList(),
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}