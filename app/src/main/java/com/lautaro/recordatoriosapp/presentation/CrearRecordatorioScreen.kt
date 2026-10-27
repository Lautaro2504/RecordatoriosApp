
package com.lautaro.recordatoriosapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lautaro.recordatoriosapp.Prioridad


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecordatorioScreen(
    viewModel: RecordatorioViewModel,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formulario by viewModel.formularioState.collectAsStateWithLifecycle()

    var categoriaExpandida by remember { mutableStateOf(false) }
    var prioridadExpandida by remember { mutableStateOf(false) }

    val categorias = listOf("Estudio", "Trabajo", "Personal")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Crear recordatorio")
                }
            )
        }
    ) { paddingInterno ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Campo: título
            OutlinedTextField(
                value = formulario.titulo,
                onValueChange = viewModel::actualizarTitulo,
                label = { Text("Título") },
                singleLine = true,
                isError = formulario.errores.containsKey("titulo"),
                supportingText = {
                    formulario.errores["titulo"]?.let { error ->
                        Text(error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: descripción
            OutlinedTextField(
                value = formulario.descripcion,
                onValueChange = viewModel::actualizarDescripcion,
                label = {
                    Text("Descripción (máximo 200 caracteres)")
                },
                isError = formulario.errores.containsKey("descripcion"),
                supportingText = {
                    formulario.errores["descripcion"]?.let { error ->
                        Text(error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector: categoría
            ExposedDropdownMenuBox(
                expanded = categoriaExpandida,
                onExpandedChange = {
                    categoriaExpandida = !categoriaExpandida
                }
            ) {
                OutlinedTextField(
                    value = formulario.categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = categoriaExpandida
                        )
                    },
                    isError = formulario.errores.containsKey("categoria"),
                    supportingText = {
                        formulario.errores["categoria"]?.let { error ->
                            Text(error)
                        }
                    },
                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable
                        )
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = categoriaExpandida,
                    onDismissRequest = {
                        categoriaExpandida = false
                    }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                viewModel.actualizarCategoria(categoria)
                                categoriaExpandida = false
                            }
                        )
                    }
                }
            }

            // Selector: prioridad
            ExposedDropdownMenuBox(
                expanded = prioridadExpandida,
                onExpandedChange = {
                    prioridadExpandida = !prioridadExpandida
                }
            ) {
                val textoPrioridad = when (formulario.prioridad) {
                    Prioridad.BAJA -> "Baja"
                    Prioridad.MEDIA -> "Media"
                    Prioridad.ALTA -> "Alta"
                    null -> ""
                }

                OutlinedTextField(
                    value = textoPrioridad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = prioridadExpandida
                        )
                    },
                    isError = formulario.errores.containsKey("prioridad"),
                    supportingText = {
                        formulario.errores["prioridad"]?.let { error ->
                            Text(error)
                        }
                    },
                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable
                        )
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = prioridadExpandida,
                    onDismissRequest = {
                        prioridadExpandida = false
                    }
                ) {
                    Prioridad.entries.forEach { prioridad ->
                        val etiqueta = when (prioridad) {
                            Prioridad.BAJA -> "Baja"
                            Prioridad.MEDIA -> "Media"
                            Prioridad.ALTA -> "Alta"
                        }

                        DropdownMenuItem(
                            text = { Text(etiqueta) },
                            onClick = {
                                viewModel.actualizarPrioridad(prioridad)
                                prioridadExpandida = false
                            }
                        )
                    }
                }
            }

            // Error general al intentar guardar
            formulario.errorGeneral?.let { mensaje ->
                Text(mensaje)
            }

            // Botón para guardar
            Button(
                onClick = viewModel::crearRecordatorio,
                enabled = formulario.formularioValido &&
                        !formulario.guardando &&
                        !formulario.guardadoExitoso,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (formulario.guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar recordatorio")
                }
            }

            // Confirmación después de guardar
            if (formulario.guardadoExitoso) {
                Text("¡Recordatorio guardado correctamente!")

                Button(
                    onClick = alVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a la lista")
                }
            }
        }
    }
}
