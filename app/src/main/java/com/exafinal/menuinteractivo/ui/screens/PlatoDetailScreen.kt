package com.exafinal.menuinteractivo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exafinal.menuinteractivo.data.MenuRepository
import com.exafinal.menuinteractivo.data.model.DetallePedido
import com.exafinal.menuinteractivo.data.model.Ingrediente
import com.exafinal.menuinteractivo.data.model.Plato
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatoDetailScreen(platoId: Int, repository: MenuRepository) {
    var plato by remember { mutableStateOf<Plato?>(null) }
    var ingredientes by remember { mutableStateOf<List<Ingrediente>>(emptyList()) }
    var selectedIngredientes by remember { mutableStateOf<List<Ingrediente>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar plato e ingredientes al iniciar la pantalla
    LaunchedEffect(platoId) {
        plato = repository.obtenerPlatoPorId(platoId)
        ingredientes = repository.obtenerIngredientesPorPlato(platoId)
        selectedIngredientes = ingredientes // Por defecto, todos seleccionados
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(plato?.nombre_plato ?: "Detalle del Plato") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            plato?.let { p ->
                // Información general del plato
                Text(
                    text = p.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Personaliza tus ingredientes:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Lista de ingredientes con checkboxes
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ingredientes) { ingrediente ->
                        var isSelected by remember { mutableStateOf(true) }

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
                                Text(text = ingrediente.nombre_ingrediente)
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        isSelected = checked
                                        selectedIngredientes = if (checked) {
                                            selectedIngredientes + ingrediente
                                        } else {
                                            selectedIngredientes - ingrediente
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para agregar el pedido
                Button(
                    onClick = {
                        scope.launch {
                            val precioIngredientes = selectedIngredientes.sumOf { it.precio_adicional }
                            val subtotal = p.precio_base + precioIngredientes

                            // Crear el detalle del pedido
                            val detallePedido = DetallePedido(
                                id_pedido = 1, // ID de pedido estático, ajustar en lógica real
                                id_plato = p.id_plato,
                                cantidad = 1,
                                subtotal = subtotal,
                                notas = selectedIngredientes.joinToString(", ") { it.nombre_ingrediente }
                            )

                            // Guardar en la base de datos
                            repository.agregarDetallePedido(detallePedido)

                            // Mostrar mensaje de confirmación
                            snackbarHostState.showSnackbar("Se ha agregado el pedido")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Agregar al Pedido")
                }
            } ?: run {
                Text(text = "Cargando detalles del plato...")
            }
        }
    }
}


