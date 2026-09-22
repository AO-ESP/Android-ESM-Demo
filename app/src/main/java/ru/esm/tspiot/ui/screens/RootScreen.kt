package ru.esm.tspiot.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import ru.esm.tspiot.ui.navigation.AppNavHost
import ru.esm.tspiot.ui.navigation.AppRoute

val LocalHostController = staticCompositionLocalOf<NavHostController> {
    throw RuntimeException("LocalHostController must be initialized at this moment")
}

@Composable
fun RootScreen(
    controller: NavHostController
) {
    CompositionLocalProvider(
        LocalHostController provides controller
    ) {
        AppNavHost(
            AppRoute,
            controller,
        )
    }
}
