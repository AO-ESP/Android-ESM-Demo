package ru.esm.tspiot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.atol.os.tspiot.presentation.ui.navigation.AppRoute
import ru.atol.os.tspiot.presentation.ui.navigation.Route
import ru.esm.tspiot.ui.scanner.ScannerScreen
import ru.esm.tspiot.ui.screens.CodesCheckScreen
import ru.esm.tspiot.ui.screens.LmScreen
import ru.esm.tspiot.ui.screens.MainScreen
import ru.esm.tspiot.ui.screens.PermissionScreen

@Composable
fun AppNavHost(
    startDestination: Route,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController, startDestination.id, modifier) {
        initNavigation(navController)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun NavGraphBuilder.initNavigation(navController: NavHostController) {
    composable(AppRoute.id) { MainScreen(navController) }
    composable(AppRoute.CodesCheckScreenRoute.id) {
        CodesCheckScreen(navController)
    }
    composable(AppRoute.LmScreenRoute.id) {
        LmScreen(navController)
    }
    composable(AppRoute.ScannerScreenRoute.id) {
        ScannerScreen(navController)
    }
    composable(AppRoute.PermissionScreenRoute.id) {
        PermissionScreen(navController)
    }
}

private fun NavGraphBuilder.composable(
    route: Route,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route.idWithArg, arguments) {
        content(it)
    }
}
