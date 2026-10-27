
package com.lautaro.recordatoriosapp

import com.lautaro.recordatoriosapp.data.RecordatorioRepository
import com.lautaro.recordatoriosapp.presentation.RecordatorioUiState
import com.lautaro.recordatoriosapp.presentation.RecordatorioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecordatorioViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun configurar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarRecordatorios_listaVacia_muestraEstadoVacio() =
        runTest(dispatcher) {
            val repository = RepositorioDePrueba()
            val viewModel = RecordatorioViewModel(repository)

            advanceUntilIdle()

            assertEquals(
                RecordatorioUiState.Vacio,
                viewModel.uiState.value
            )
        }

    @Test
    fun cargarRecordatorios_siFalla_muestraEstadoError() =
        runTest(dispatcher) {
            val repository = RepositorioDePrueba(
                fallarAlCargar = true
            )
            val viewModel = RecordatorioViewModel(repository)

            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value is RecordatorioUiState.Error
            )
        }

    @Test
    fun guardarRecordatorio_siTieneExito_actualizaLaLista() =
        runTest(dispatcher) {
            val repository = RepositorioDePrueba()
            val viewModel = RecordatorioViewModel(repository)

            advanceUntilIdle()

            val recordatorio = crearRecordatorioDePrueba()
            viewModel.guardarRecordatorio(recordatorio)

            advanceUntilIdle()

            val estado = viewModel.uiState.value

            assertTrue(
                estado is RecordatorioUiState.ConDatos
            )

            assertEquals(
                recordatorio,
                (estado as RecordatorioUiState.ConDatos)
                    .recordatorios.first()
            )
        }

    @Test
    fun guardarRecordatorio_siFalla_muestraEstadoError() =
        runTest(dispatcher) {
            val repository = RepositorioDePrueba(
                fallarAlGuardar = true
            )
            val viewModel = RecordatorioViewModel(repository)

            advanceUntilIdle()

            viewModel.guardarRecordatorio(
                crearRecordatorioDePrueba()
            )

            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value is RecordatorioUiState.Error
            )
        }

    private fun crearRecordatorioDePrueba(): Recordatorio {
        val categoria = categoriasPorNombre.values.first()

        return Recordatorio(
            id = 1,
            titulo = "Estudiar Kotlin",
            descripcion = "Repasar las pruebas",
            categoria = categoria,
            prioridad = Prioridad.MEDIA,
            estado = Estado.PENDIENTE
        )
    }

    private class RepositorioDePrueba(
        private val fallarAlCargar: Boolean = false,
        private val fallarAlGuardar: Boolean = false
    ) : RecordatorioRepository {

        private val recordatorios = mutableListOf<Recordatorio>()

        override suspend fun obtenerRecordatorios(): List<Recordatorio> {
            if (fallarAlCargar) {
                throw Exception("Error de prueba al cargar")
            }

            return recordatorios.toList()
        }

        override suspend fun obtenerRecordatorioPorId(
            id: Int
        ): Recordatorio? {
            return recordatorios.find { it.id == id }
        }

        override suspend fun guardarRecordatorio(
            recordatorio: Recordatorio
        ): Recordatorio {
            if (fallarAlGuardar) {
                throw Exception("Error de prueba al guardar")
            }

            recordatorios.add(recordatorio)
            return recordatorio
        }
    }
}
