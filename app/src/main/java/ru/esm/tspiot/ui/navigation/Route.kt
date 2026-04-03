package ru.esm.tspiot.ui.navigation

sealed class Route {
    abstract val id: String
    open val idWithArg: String get() = id
    abstract val title: String
}

data object AppRoute : Route() {
    override val id = "main"
    override val title = "ЕСП ПМСР"

    data object CodesCheckScreenRoute : Route() {
        override val id: String = "codes_check"
        override val title = "Проверка КИ"
    }

    data object LmScreenRoute : Route() {
        override val id: String = "lm"
        override val title = "ЛМ ЧЗ"
    }

    data object ScannerScreenRoute : Route() {
        override val id: String = "scanner"
        override val title = "Сканер"
    }

    data object PermissionScreenRoute : Route() {
        override val id: String = "permission"
        override val title = "Разрешения"
    }
}