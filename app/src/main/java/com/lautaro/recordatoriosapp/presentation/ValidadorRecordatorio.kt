
package com.lautaro.recordatoriosapp.presentation

import com.lautaro.recordatoriosapp.Prioridad

fun validarDatosRecordatorio(
    titulo: String,
    descripcion: String,
    categoria: String,
    prioridad: Prioridad?
): Map<String, String> {

    val errores = mutableMapOf<String, String>()

    if (titulo.trim().length < 4) {
        errores["titulo"] = "El título debe tener al menos 4 caracteres"
    }

    if (descripcion.length > 200) {
        errores["descripcion"] =
            "La descripción no puede superar los 200 caracteres"
    }

    val categoriasValidas = setOf(
        "Estudio",
        "Trabajo",
        "Personal"
    )

    if (categoria !in categoriasValidas) {
        errores["categoria"] =
            "Seleccioná una categoría válida"
    }

    if (prioridad == null) {
        errores["prioridad"] =
            "Seleccioná una prioridad"
    }

    return errores
}
