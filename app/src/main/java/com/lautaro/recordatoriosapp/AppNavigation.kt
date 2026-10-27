package com.lautaro.recordatoriosapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lautaro.recordatoriosapp.data.InMemoryRecordatorioRepository
import com.lautaro.recordatoriosapp.presentation.CrearRecordatorioScreen
import com.lautaro.recordatoriosapp.presentation.DetalleRecordatorioScreen
import com.lautaro.recordatoriosapp.presentation.RecordatorioViewModel
import com.lautaro.recordatoriosapp.presentation.RecordatorioViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val repository = remember { InMemoryRecordatorioRepository() }

    val viewModel: RecordatorioViewModel = viewModel(
        factory = RecordatorioViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {
        composable("lista") {
            PantallaRecordatorios(
                viewModel = viewModel,
                alNavegarACrear = {
                    viewModel.limpiarFormulario()
                    navController.navigate("crear")
                },
                alSeleccionarRecordatorio = { id ->
                    navController.navigate("detalle/$id")
                }
            )
        }

        composable("crear") {
            CrearRecordatorioScreen(
                viewModel = viewModel,
                alVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                DetalleRecordatorioScreen(
                    recordatorioId = id,
                    viewModel = viewModel,
                    alVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
