
package com.lautaro.recordatoriosapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lautaro.recordatoriosapp.Prioridad
import com.lautaro.recordatoriosapp.Recordatorio
import com.lautaro.recordatoriosapp.data.RecordatorioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordatorioViewModel(
    private val repository: RecordatorioRepository
) : ViewModel() {

    // Estado de la lista de recordatorios
    private val _uiState =
        MutableStateFlow<RecordatorioUiState>(
            RecordatorioUiState.Cargando
        )

    val uiState: StateFlow<RecordatorioUiState> =
        _uiState.asStateFlow()

    // Estado del formulario de creación
    private val _formularioState =
        MutableStateFlow(CrearRecordatorioUiState())

    val formularioState: StateFlow<CrearRecordatorioUiState> =
        _formularioState.asStateFlow()

    init {
        cargarRecordatorios()
    }

    // Limpiar el formulario de creación
    fun limpiarFormulario() {
        _formularioState.value = CrearRecordatorioUiState()
    }

    // Cargar la lista de recordatorios
    fun cargarRecordatorios() {
        viewModelScope.launch {
            _uiState.value = RecordatorioUiState.Cargando

            try {
                val lista = repository.obtenerRecordatorios()

                _uiState.value = if (lista.isEmpty()) {
                    RecordatorioUiState.Vacio
                } else {
                    RecordatorioUiState.ConDatos(lista)
                }
            } catch (e: Exception) {
                _uiState.value = RecordatorioUiState.Error(
                    e.message
                        ?: "No se pudieron cargar los recordatorios"
                )
            }
        }
    }

    // Guardar un recordatorio en el repositorio
    fun guardarRecordatorio(recordatorio: Recordatorio) {
        viewModelScope.launch {
            try {
                repository.guardarRecordatorio(recordatorio)
                cargarRecordatorios()
            } catch (e: Exception) {
                _uiState.value = RecordatorioUiState.Error(
                    e.message
                        ?: "No se pudo guardar el recordatorio"
                )
            }
        }
    }

    // Actualizar el título
    fun actualizarTitulo(titulo: String) {
        _formularioState.update {
            it.copy(
                titulo = titulo,
                errores = it.errores - "titulo",
                errorGeneral = null
            )
        }
    }

    // Actualizar la descripción
    fun actualizarDescripcion(descripcion: String) {
        _formularioState.update {
            it.copy(
                descripcion = descripcion,
                errores = it.errores - "descripcion",
                errorGeneral = null
            )
        }
    }

    // Actualizar la categoría
    fun actualizarCategoria(categoria: String) {
        _formularioState.update {
            it.copy(
                categoria = categoria,
                errores = it.errores - "categoria",
                errorGeneral = null
            )
        }
    }

    // Actualizar la prioridad
    fun actualizarPrioridad(prioridad: Prioridad) {
        _formularioState.update {
            it.copy(
                prioridad = prioridad,
                errores = it.errores - "prioridad",
                errorGeneral = null
            )
        }
    }

    // Validar los datos del formulario
    fun validarFormulario(): Boolean {
        val formulario = _formularioState.value

        val errores = validarDatosRecordatorio(
            titulo = formulario.titulo,
            descripcion = formulario.descripcion,
            categoria = formulario.categoria,
            prioridad = formulario.prioridad
        )

        _formularioState.update {
            it.copy(errores = errores)
        }

        return errores.isEmpty()
    }

    fun crearRecordatorio() {
        if (!validarFormulario()) {
            return
        }

        val formulario = _formularioState.value

        val categoria = com.lautaro.recordatoriosapp.categoriasPorNombre[
            formulario.categoria
        ]

        if (categoria == null) {
            _formularioState.update {
                it.copy(
                    errores = mapOf(
                        "categoria" to "Seleccioná una categoría válida"
                    )
                )
            }
            return
        }

        val nuevoRecordatorio = Recordatorio(
            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            titulo = formulario.titulo.trim(),
            descripcion = formulario.descripcion.trim().ifEmpty { null },
            categoria = categoria,
            prioridad = formulario.prioridad!!,
            estado = com.lautaro.recordatoriosapp.Estado.PENDIENTE
        )

        viewModelScope.launch {
            _formularioState.update {
                it.copy(
                    guardando = true,
                    errorGeneral = null
                )
            }

            try {
                repository.guardarRecordatorio(nuevoRecordatorio)

                _formularioState.update {
                    it.copy(
                        guardando = false,
                        guardadoExitoso = true
                    )
                }

                cargarRecordatorios()
            } catch (e: Exception) {
                _formularioState.update {
                    it.copy(
                        guardando = false,
                        errorGeneral = e.message
                            ?: "No se pudo guardar el recordatorio"
                    )
                }
            }
        }
    }

}
