package ru.esm.tspiot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator

@Composable
fun createPreviewNavController(): NavHostController {
    val context = LocalContext.current
    return remember {
        // Не переопределяем методы, а создаем обертку
        NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
    }.also { controller ->
        // Сохраняем оригинальные методы, но они не будут вызываться
        // в Preview, так как navigate не будет вызван
    }
}