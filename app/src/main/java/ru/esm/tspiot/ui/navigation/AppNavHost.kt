package ru.esm.tspiot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.esm.tspiot.ui.scanner.ScannerScreen
import ru.esm.tspiot.ui.screens.CodesCheckScreen
import ru.esm.tspiot.ui.screens.LmScreen
import ru.esm.tspiot.ui.screens.MainScreen
import ru.esm.tspiot.ui.screens.PermissionScreen

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No NavController provided")
}

// Тут просмотра не будет из-за разных ViewModel в реализациях

@Composable
fun AppNavHost(
    startDestination: Route,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController, startDestination.id, modifier) {
            initNavigation()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun NavGraphBuilder.initNavigation() {
    composable(AppRoute.id) { MainScreen() }
    composable(AppRoute.CodesCheckScreenRoute.id) { CodesCheckScreen() }
    composable(AppRoute.LmScreenRoute.id) { LmScreen() }
    composable(AppRoute.ScannerScreenRoute.id) { ScannerScreen() }
    composable(AppRoute.PermissionScreenRoute.id) { PermissionScreen() }
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
