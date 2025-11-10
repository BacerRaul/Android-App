package com.bacer.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.bacer.notesapp.ui.subjects.SubjectScreen
import com.bacer.notesapp.ui.theme.NotesAppTheme
import com.bacer.notesapp.data.DatabaseInstance
import com.bacer.notesapp.data.SubjectRepository
import com.bacer.notesapp.viewmodel.SubjectViewModel
import com.bacer.notesapp.viewmodel.SubjectViewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bacer.notesapp.ui.grades.GradeScreen

class MainActivity : ComponentActivity() {

    private val viewModel: SubjectViewModel by viewModels {
        SubjectViewModelFactory(
            SubjectRepository(
                DatabaseInstance.getDatabase(this).subjectDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.loadSubjects()

        setContent {
            NotesAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "subjects"
                ) {

                    composable("subjects") {
                        SubjectScreen(
                            subjects = viewModel.subjects,
                            onAddSubject = { viewModel.addSubject(it) },
                            onDeleteSubject = { viewModel.deleteSubject(it) },
                            nameError = viewModel.nameError,
                            onClearNameError = { viewModel.clearNameError() },
                            onGradesClick = { subjectId ->
                                navController.navigate("grades/$subjectId")
                            }
                        )
                    }

                    composable("grades/{subjectId}") { backStackEntry ->
                        val subjectId = backStackEntry.arguments?.getString("subjectId")!!.toInt()
                        GradeScreen(
                            subjectId = subjectId,
                            onBack = { navController.popBackStack() },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
