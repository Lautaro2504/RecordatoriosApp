
package com.lautaro.recordatoriosapp.presentation

import com.lautaro.recordatoriosapp.Recordatorio

sealed class RecordatorioUiState {
    data object Cargando : RecordatorioUiState()

    data object Vacio : RecordatorioUiState()

    data class ConDatos(
        val recordatorios: List<Recordatorio>
    ) : RecordatorioUiState()

    data class Error(
        val mensaje: String
    ) : RecordatorioUiState()
}
