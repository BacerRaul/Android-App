package com.bacer.notesapp.ui.notes

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import java.io.File
import java.util.concurrent.Executors


@Composable
fun CameraScreen(
    navController: NavHostController,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProvider = remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val imageCapture = remember { ImageCapture.Builder().build() }

    val capturedUris = remember { mutableStateListOf<String>() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val provider = cameraProviderFuture.get()
                cameraProvider.value = provider

                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider.value?.unbindAll()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val photoFile = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        capturedUris.add(photoFile.absolutePath)
                    }

                    override fun onError(exc: ImageCaptureException) {
                        exc.printStackTrace()
                    }
                }
            )
        }) {
            Text("Capture")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (capturedUris.isEmpty()) return@Button

                cameraProvider.value?.unbindAll()

                val pathsToPass = ArrayList(capturedUris)
                val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle

                savedStateHandle?.set("captured_photos", pathsToPass)
                savedStateHandle?.set("should_open_dialog", true)

                navController.popBackStack()
            },
            enabled = capturedUris.isNotEmpty()
        ) {
            Text("Done (${capturedUris.size})")
        }


        Spacer(Modifier.height(8.dp))

        TextButton(onClick = {
            cameraProvider.value?.unbindAll()
            onCancel()
        }) {
            Text("Cancel")
        }
    }
}
