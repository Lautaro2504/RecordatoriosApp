
package com.lautaro.recordatoriosapp.presentation

import com.lautaro.recordatoriosapp.Prioridad

data class CrearRecordatorioUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val prioridad: Prioridad? = null,
    val errores: Map<String, String> = emptyMap(),
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val errorGeneral: String? = null
) {
    /**
     * Indica si los datos cumplen los requisitos
     * necesarios para crear un recordatorio.
     */
    val formularioValido: Boolean
        get() = titulo.trim().length >= 4 &&
                descripcion.length <= 200 &&
                categoria in listOf("Estudio", "Trabajo", "Personal") &&
                prioridad != null
}
