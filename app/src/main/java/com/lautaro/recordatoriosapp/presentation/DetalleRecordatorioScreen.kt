
package com.lautaro.recordatoriosapp.presentation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lautaro.recordatoriosapp.Estado
import com.lautaro.recordatoriosapp.Prioridad
import com.lautaro.recordatoriosapp.Recordatorio
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecordatorioScreen(
    recordatorioId: Int,
    viewModel: RecordatorioViewModel,
    alVolver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val recordatorio = when (val estado = uiState) {
        is RecordatorioUiState.ConDatos ->
            estado.recordatorios.find { it.id == recordatorioId }

        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del recordatorio") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (recordatorio == null) {
                Text("No se encontró el recordatorio.")
            } else {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text("Descripción: ${recordatorio.descripcion ?: "Sin descripción"}")
                Text("Categoría: ${recordatorio.categoria.nombre}")

                Text(
                    text = "Prioridad: ${
                        when (recordatorio.prioridad) {
                            Prioridad.BAJA -> "Baja"
                            Prioridad.MEDIA -> "Media"
                            Prioridad.ALTA -> "Alta"
                        }
                    }"
                )

                Text(
                    text = "Estado: ${
                        when (recordatorio.estado) {
                            Estado.PENDIENTE -> "Pendiente"
                            Estado.EN_PROGRESO -> "En progreso"
                            Estado.COMPLETADO -> "Completado"
                        }
                    }"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = alVolver) {
                Text("Volver")
            }
        }
    }
}
