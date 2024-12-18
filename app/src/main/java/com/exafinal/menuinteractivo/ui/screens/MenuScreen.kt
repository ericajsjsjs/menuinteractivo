package com.exafinal.menuinteractivo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.exafinal.menuinteractivo.R
import com.exafinal.menuinteractivo.data.MenuRepository
import com.exafinal.menuinteractivo.data.model.Plato
import com.exafinal.menuinteractivo.ui.theme.Orange200
import com.exafinal.menuinteractivo.ui.theme.Orange700
import androidx.compose.ui.platform.LocalContext

@Composable
fun MenuScreen(
    qrData: String,
    repository: MenuRepository,
    onPlatoClick: (Int) -> Unit,
    onVerCarrito: (Int) -> Unit // Callback para ir al carrito
) {
    var platos by remember { mutableStateOf<List<Plato>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar los platos desde la base de datos
    LaunchedEffect(qrData) {
        isLoading = true
        platos = repository.obtenerPlatos()
        isLoading = false
    }

    // Interfaz de usuario para mostrar el menú
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Menú basado en QR: $qrData",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            isLoading -> {
                Text(text = "Cargando menú...", style = MaterialTheme.typography.bodyMedium)
            }
            platos.isEmpty() -> {
                Text(text = "No hay datos disponibles.", style = MaterialTheme.typography.bodyMedium)
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(platos) { plato ->
                        PlatoItem(plato = plato, onClick = { onPlatoClick(plato.id_plato) })
                    }
                }
            }
        }

        // Botón para acceder al carrito
        Button(
            onClick = { onVerCarrito(1) }, // Envía un ID de pedido estático, por ejemplo, "1"
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver Carrito")
        }
    }
}


@Composable
fun PlatoItem(plato: Plato, onClick: () -> Unit) {
    val context = LocalContext.current

    // Obtener el ID del recurso de imagen desde el nombre almacenado en 'imagen'
    val imageId = remember {
        context.resources.getIdentifier(
            plato.imagen, "drawable", context.packageName
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }, // Llama al callback cuando se haga clic en la tarjeta
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Orange200),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mostrar la imagen del plato
            Image(
                painter = if (imageId != 0) painterResource(id = imageId)
                else painterResource(id = R.drawable.placeholder_image),
                contentDescription = "Imagen del plato",
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Información del plato
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = plato.nombre_plato,
                    style = MaterialTheme.typography.titleMedium,
                    color = Orange700
                )
                Text(
                    text = "Precio: \$${plato.precio_base}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = plato.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}


