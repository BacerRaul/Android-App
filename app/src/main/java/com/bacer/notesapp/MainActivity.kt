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
import com.bacer.notesapp.data.DatabaseInstance
import com.bacer.notesapp.data.SubjectRepository
import com.bacer.notesapp.data.GradeRepository
import com.bacer.notesapp.viewmodel.SubjectViewModel
import com.bacer.notesapp.viewmodel.SubjectViewModelFactory
import com.bacer.notesapp.viewmodel.GradeViewModel
import com.bacer.notesapp.viewmodel.GradeViewModelFactory

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
                            }
                        )
                    }
                    // ---------- Grades ----------
                    composable("grades/{subjectId}") { backStackEntry ->
                        val subjectId = backStackEntry.arguments?.getString("subjectId")!!.toInt()

                        // Load grades **only for the current subject**
                        LaunchedEffect(subjectId) {
                            gradeViewModel.loadGrades(subjectId)
                        }

                        // Get the subject name for the title (fallback to "Grades")
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
                }
            }
        }
    }
}