package com.exafinal.menuinteractivo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exafinal.menuinteractivo.data.MenuRepository
import com.exafinal.menuinteractivo.data.database.RestauranteDatabase
import com.exafinal.menuinteractivo.ui.screens.CarritoScreen
import com.exafinal.menuinteractivo.ui.screens.ConfirmacionPedidoScreen
import com.exafinal.menuinteractivo.ui.screens.MenuScreen
import com.exafinal.menuinteractivo.ui.screens.PlatoDetailScreen
import com.exafinal.menuinteractivo.ui.screens.QRScanScreen
import com.exafinal.menuinteractivo.ui.theme.MenuinteractivoTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manejo de permisos
        val hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if (!hasCameraPermission) {
            requestCameraPermission()
        } else {
            startApplication()
        }
    }

    private fun requestCameraPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startApplication()
            } else {
                // Si no se otorgan los permisos, muestra un mensaje o finaliza la app
                finish()
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startApplication() {
        val database = RestauranteDatabase.getDatabase(this) // Instancia de la base de datos
        val repository = MenuRepository(database) // Repositorio con DAO de Plato

        setContent {
            MenuinteractivoTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, repository = repository)
            }
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, repository: MenuRepository) {
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        composable("qr_scan") {
            QRScanScreen(navController = navController)
        }
        composable("menu_screen/{qrData}") { backStackEntry ->
            val qrData = backStackEntry.arguments?.getString("qrData") ?: "Unknown"
            MenuScreen(
                qrData = qrData,
                repository = repository,
                onPlatoClick = { platoId ->
                    navController.navigate("plato_detail/$platoId")
                },
                onVerCarrito = { pedidoId ->
                    navController.navigate("carrito_screen/$pedidoId")
                }
            )
        }

        composable("plato_detail/{platoId}") { backStackEntry ->
            val platoId = backStackEntry.arguments?.getString("platoId")?.toInt() ?: 0
            PlatoDetailScreen(platoId = platoId, repository = repository)
        }
        composable("carrito_screen/{pedidoId}") { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId")?.toInt() ?: 0
            CarritoScreen(
                pedidoId = pedidoId,
                repository = repository,
                onConfirmarPedido = {
                    navController.navigate("confirmacion_pedido_screen/$pedidoId")
                }
            )
        }
        composable("confirmacion_pedido_screen/{pedidoId}") { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId")?.toInt() ?: 0
            ConfirmacionPedidoScreen(
                pedidoId = pedidoId,
                repository = repository,
                onPedidoRegistrado = {
                    navController.navigate("menu_screen/mesa_1") // Navegar al menú después de registrar
                }
            )
        }
        composable("confirmacion_pedido_screen/{pedidoId}") { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId")?.toInt() ?: 0
            ConfirmacionPedidoScreen(
                pedidoId = pedidoId,
                repository = repository,
                onPedidoRegistrado = {
                    // Redirige al inicio después de registrar el pedido
                    navController.popBackStack("splash_screen", inclusive = false)
                }
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_principal),
            contentDescription = "Logo"
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("qr_scan")
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MenuinteractivoTheme {
        SplashScreen(navController = rememberNavController())
    }
}


