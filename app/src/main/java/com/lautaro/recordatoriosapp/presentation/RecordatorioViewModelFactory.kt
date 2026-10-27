
package com.lautaro.recordatoriosapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lautaro.recordatoriosapp.data.RecordatorioRepository

class RecordatorioViewModelFactory(
    private val repository: RecordatorioRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(RecordatorioViewModel::class.java)) {
            return RecordatorioViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
