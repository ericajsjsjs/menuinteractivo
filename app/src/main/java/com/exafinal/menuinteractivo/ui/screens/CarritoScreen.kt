package com.exafinal.menuinteractivo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exafinal.menuinteractivo.data.MenuRepository
import com.exafinal.menuinteractivo.data.model.DetalleConNombrePlato
import com.exafinal.menuinteractivo.data.model.DetallePedido
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    pedidoId: Int,
    repository: MenuRepository,
    onConfirmarPedido: () -> Unit
) {
    var detalles by remember { mutableStateOf<List<DetalleConNombrePlato>>(emptyList()) }
    var total by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()

    // Cargar los detalles con el nombre del plato
    LaunchedEffect(pedidoId) {
        detalles = repository.obtenerDetallesConNombrePlato(pedidoId)
        total = detalles.sumOf { it.subtotal }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito de Compras") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(detalles) { detalle ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Nombre Plato: ${detalle.nombre_plato}")
                            Text("Cantidad: ${detalle.cantidad}")
                            Text("Subtotal: \$${String.format("%.2f", detalle.subtotal)}")
                            // Mostrar ingredientes personalizados
                            if (!detalle.notas.isNullOrEmpty()) {
                                Text("Ingredientes: ${detalle.notas}")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total: \$${String.format("%.2f", total)}",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = { onConfirmarPedido() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Confirmar Pedido")
            }
        }
    }
}

@Composable
fun DetallePedidoItem(detalle: DetallePedido, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Plato ID: ${detalle.id_plato}")
                Text(text = "Cantidad: ${detalle.cantidad}")
                Text(text = "Subtotal: \$${detalle.subtotal}")
            }
            Button(onClick = { onEliminar() }) {
                Text(text = "Eliminar")
            }
        }
    }
}
