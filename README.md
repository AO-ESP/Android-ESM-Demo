# Пример AIDL-сервисов для интеграции кассового софта с ТС ПИоТ

Демонстрационный клиент. Он биндится к установленному ТС ПИоТ и вызывает три AIDL-интерфейса: `IEsmService`, `IPiotManager` и устаревший `IMarkingManager`.

Контракты, параметры запросов и ответов, action и пакеты сервисов описаны в [docs/README.md](docs/README.md).

Кратко:

- `IEsmService` — проверка КИ и EAN, продажа и возврат в ЛМ ЧЗ, сведения о ККТ. Action `ru.esp.esm.action.ACTION_ESM_SERVICE`.
- `IPiotManager` — события кассы и проверка EAN/GTIN. Action зависит от терминала.
- `IMarkingManager` — устаревшая проверка марок. Заменяется `IEsmService.codesCheck`.

Пакет процесса: `ru.atol.os.tspiot` на терминалах Атол (flavor `atol`), `ru.esp.tspiot` на остальных (flavor `esp`). `Intent.setPackage` обязателен.

Сборка демо выбирает реализацию `PiotManagerClient` product flavor: `atol` подключает `PiotAtolManagerClientImpl`, `esp` — `PiotESMManagerClientImpl`. Исходники flavor лежат в `app/src/atol` и `app/src/esp`.
