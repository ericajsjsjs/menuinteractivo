package com.exafinal.menuinteractivo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exafinal.menuinteractivo.data.MenuRepository
import com.exafinal.menuinteractivo.data.model.Pedido
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionPedidoScreen(
    pedidoId: Int,
    repository: MenuRepository,
    onPedidoRegistrado: () -> Unit
) {
    var totalPedido by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()

    // Calcular el total del pedido
    LaunchedEffect(pedidoId) {
        val detalles = repository.obtenerDetallesPorPedido(pedidoId)
        totalPedido = detalles.sumOf { it.subtotal }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Confirmación de Pedido") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total a Pagar: \$${String.format("%.2f", totalPedido)}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    scope.launch {
                        // Lógica para limpiar el carrito y registrar el pedido
                        repository.limpiarCarrito()
                        onPedidoRegistrado() // Callback para redirigir a la pantalla inicial
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Pedido")
            }
        }
    }
}

