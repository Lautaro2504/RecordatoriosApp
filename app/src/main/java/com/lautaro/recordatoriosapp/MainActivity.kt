package com.lautaro.recordatoriosapp

import com.lautaro.recordatoriosapp.presentation.EstadoCargando
import com.lautaro.recordatoriosapp.presentation.EstadoVacio
import com.lautaro.recordatoriosapp.presentation.EstadoError
import androidx.compose.foundation.clickable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lautaro.recordatoriosapp.presentation.RecordatorioUiState
import com.lautaro.recordatoriosapp.presentation.RecordatorioViewModel
import com.lautaro.recordatoriosapp.ui.theme.RecordatoriosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecordatoriosAppTheme {
                AppNavigation()
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios(
    viewModel: RecordatorioViewModel,
    alNavegarACrear: () -> Unit,
    alSeleccionarRecordatorio: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var estadoSeleccionado by remember {
        mutableStateOf<Estado?>(null)
    }

    var prioridadSeleccionada by remember {
        mutableStateOf<Prioridad?>(null)
    }

    val estadosDisponibles = listOf(
        null,
        Estado.PENDIENTE,
        Estado.EN_PROGRESO,
        Estado.COMPLETADO
    )

    val prioridadesDisponibles = listOf(
        null,
        Prioridad.BAJA,
        Prioridad.MEDIA,
        Prioridad.ALTA
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = alNavegarACrear) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear recordatorio"
                )
            }
        }
    ) { padding ->


        when (val estado = uiState) {

            is RecordatorioUiState.Cargando -> {
                EstadoCargando(
                    modifier = Modifier.padding(padding)
                )
            }

            is RecordatorioUiState.Error -> {
                EstadoError(
                    mensaje = estado.mensaje,
                    modifier = Modifier.padding(padding),
                    alReintentar = {
                        viewModel.cargarRecordatorios()
                    }
                )
            }

            is RecordatorioUiState.Vacio -> {
                EstadoVacio(
                    modifier = Modifier.padding(padding),
                    alCrearRecordatorio = alNavegarACrear
                )
            }

            is RecordatorioUiState.ConDatos -> {
                val recordatoriosFiltrados =
                    estado.recordatorios.filter { recordatorio ->

                        val coincideEstado =
                            estadoSeleccionado == null ||
                                    recordatorio.estado == estadoSeleccionado

                        val coincidePrioridad =
                            prioridadSeleccionada == null ||
                                    recordatorio.prioridad == prioridadSeleccionada

                        coincideEstado && coincidePrioridad
                    }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Text(
                        text = "Organizá tus tareas del día",
                        modifier = Modifier.padding(16.dp)
                    )

                    // FILTRO POR ESTADO
                    Text(
                        text = "Filtrar por estado:",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(estadosDisponibles) { estadoFiltro ->
                            FilterChip(
                                selected = estadoSeleccionado == estadoFiltro,
                                onClick = {
                                    estadoSeleccionado = estadoFiltro
                                },
                                label = {
                                    Text(
                                        when (estadoFiltro) {
                                            null -> "Todos"
                                            Estado.PENDIENTE -> "Pendiente"
                                            Estado.EN_PROGRESO -> "En progreso"
                                            Estado.COMPLETADO -> "Completado"
                                        }
                                    )
                                }
                            )
                        }
                    }

                    // FILTRO POR PRIORIDAD
                    Text(
                        text = "Filtrar por prioridad:",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )

                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(prioridadesDisponibles) { prioridadFiltro ->
                            FilterChip(
                                selected = prioridadSeleccionada == prioridadFiltro,
                                onClick = {
                                    prioridadSeleccionada = prioridadFiltro
                                },
                                label = {
                                    Text(
                                        when (prioridadFiltro) {
                                            null -> "Todas"
                                            Prioridad.BAJA -> "Baja"
                                            Prioridad.MEDIA -> "Media"
                                            Prioridad.ALTA -> "Alta"
                                        }
                                    )
                                }
                            )
                        }
                    }

                    // CANTIDAD DE RESULTADOS
                    Text(
                        text = "Resultados: ${recordatoriosFiltrados.size}",
                        modifier = Modifier.padding(16.dp)
                    )

                    // LISTA FILTRADA
                    if (recordatoriosFiltrados.isEmpty()) {
                        Text(
                            text = "No hay recordatorios para este filtro.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = recordatoriosFiltrados,
                                key = { it.id }
                            ) { recordatorio ->
                                TarjetaRecordatorio(recordatorio) {
                                    alSeleccionarRecordatorio(recordatorio.id)
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}


@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    modifier: Modifier = Modifier,
    alHacerClic: () -> Unit = {}
) {
    val colorCategoria = try {
        Color(
            android.graphics.Color.parseColor(
                recordatorio.categoria.colorHex
            )
        )
    } catch (_: IllegalArgumentException) {
        Color.Gray
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { alHacerClic() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = recordatorio.categoria.nombre,
                    color = colorCategoria,
                    style = MaterialTheme.typography.labelLarge
                )

                Text(text = "•")

                Text(
                    text = when (recordatorio.prioridad) {
                        Prioridad.BAJA -> "Prioridad baja"
                        Prioridad.MEDIA -> "Prioridad media"
                        Prioridad.ALTA -> "Prioridad alta"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = etiquetasEstado[recordatorio.estado]
                        ?: "Sin estado",
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    ),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            if (recordatorio.descripcion != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recordatorio.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

