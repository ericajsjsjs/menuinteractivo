package com.exafinal.menuinteractivo.ui.screens

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun QRScanScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val barcodeScanner = BarcodeScanning.getClient()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Texto en la parte superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Leer QR",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Contenido de la cámara
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // AndroidView para integrar CameraX PreviewView en Compose
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // Configuración de la cámara
                        val preview = Preview.Builder().build()
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        // Conectar PreviewView con CameraX
                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        // Configuración del análisis de imágenes
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        // Análisis de imágenes con ML Kit
                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
                                barcodeScanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            val scannedResult = barcode.rawValue ?: "No Data"
                                            Log.d("QRScan", "QR Code: $scannedResult")
                                            // Navega al menú con los datos escaneados
                                            navController.navigate("menu_screen/$scannedResult")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("QRScan", "Error: ${e.message}")
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            }
                        }

                        try {
                            // Vincular la cámara al ciclo de vida
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.e("QRScan", "Camera bind failed: ${e.message}")
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}