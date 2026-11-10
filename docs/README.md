# Контракты ЕСМ и ТС ПИоТ

Этот репозиторий — клиент-демонстрация кассового приложения. Он подключается к уже установленному сервису ТС ПИоТ и вызывает его AIDL-интерфейсы. Сервис в этот репозиторий не входит: интегратор копирует AIDL и Parcelable-модели в своё приложение и биндится к установленному пакету.

Документация описывает интерфейсы, подключение, запросы и ответы так, как они заданы исходниками этого клиента.

## Сервисы

| Сервис | Назначение | Статус |
| --- | --- | --- |
| [`IEsmService`](iesm-service.md) | Проверка КИ и EAN, продажа и возврат в ЛМ ЧЗ, сведения о ЕСМ / ККТ / ЛМ | Актуальный |
| [`IPiotManager`](ipiot-manager.md) | События кассы: смена, чек, кассир, ККТ, ошибки, уведомления ИСМ, проверка EAN/GTIN | Актуальный |
| [`IMarkingManager`](imarking-manager.md) | Проверка марок | Устарел, заменяется `IEsmService.codesCheck` |

Общие правила `bindService`, `<queries>`, ключа Bundle и `classLoader` — в [connection.md](connection.md).

Пошаговые инструкции только по `IPiotManager.eanCheck`:

- [ean-check-atol.md](ean-check-atol.md) — терминалы Атол
- [ean-check-esp.md](ean-check-esp.md) — flavor `esp`, остальные терминалы

## Куда подключаться

Пакет сервиса зависит от терминала. `Intent.setPackage` обязателен: без него `bindService` на современных Android сервис не находит.

| Flavor сборки демо | Терминал | Пакет сервиса | minSdk демо |
| --- | --- | --- | --- |
| `atol` | Атол | `ru.atol.os.tspiot` | 24 |
| `esp` | остальные | `ru.esp.tspiot` | 23 |

| Сервис | Action | AIDL-интерфейс, flavor `atol` | AIDL-интерфейс, flavor `esp` |
| --- | --- | --- | --- |
| `IEsmService` | `ru.esp.esm.action.ACTION_ESM_SERVICE` | `ru.esp.esm.api.IEsmService` | `ru.esp.esm.api.IEsmService` |
| `IPiotManager` | `ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER` | `ru.atol.os.tspiot.driver.api.IPiotManager` | — |
| `IPiotManager` | `ru.esm.tspiot.action.ACTION_PIOT_MANAGER` | — | `ru.esm.tspiot.driver.api.IPiotManager` |
| `IMarkingManager` | `ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER` | `ru.atol.os.tspiot.api.IMarkingManager` | — |
| `IMarkingManager` | `ru.esm.tspiot.action.ACTION_MARKING_MANAGER` | — | `ru.esm.tspiot.api.IMarkingManager` |

У `IEsmService` action и пакет интерфейса одинаковы на обоих flavor. Меняется только пакет процесса, в котором сервис запущен (`setPackage`).

Успешный ответ с данными кладётся в `Bundle` по ключу `"key"`. Перед `getParcelable` нужно выставить `bundle.classLoader` на class loader модели ответа.

## Версии `IEsmService`

`getAidlVersion()` возвращает целое число версии интерфейса. Метод синхронный и колбэк не использует.

| Версия | Состав | Примечание |
| --- | --- | --- |
| 2 | `getAidlVersion`, `codesCheck`, `cisSell`, `cisReturn`, `cisSold` | Базовый контракт |
| 3 | плюс `getInfo` | ЕСМ v1.4.2 |
| 4 | плюс `eanCheck`; в `KktInfoExternal` поле `licenceTill`; в `CodesCheckResponse` список `eans`; в `codesCheck` можно передавать и КИ, и EAN/GTIN | ЕСМ v1.*, ещё не в релизе. Номер версии продукта нужно сверить с релизом ЕСМ |

Вызов метода более новой версии на старом сервисе завершается `RemoteException` / `UnsupportedOperationException` на стороне клиента. Перед вызовом `getInfo` и `eanCheck` проверяйте `getAidlVersion()`.

## Как перенести контракт в своё приложение

1. Скопируйте `.aidl` и Java-классы моделей. Имена пакетов должны совпасть байт в байт с пакетами сервиса. Смена пакета ломает разбор `Parcel`: получите `ClassNotFoundException` или неверные поля.
2. В `build.gradle` модуля включите AIDL:

```kotlin
android {
    buildFeatures {
        aidl = true
    }
}
```

3. Для Android 11+ объявите `<queries>` на action сервиса. Полный пример — в [connection.md](connection.md).
4. Модели помечены `@Keep`. Если включаете R8/ProGuard, не обфусцируйте эти классы и их `CREATOR`: имя класса пишется в `Parcel`.
5. Геттеры можно добавить, порядок и типы полей в `writeToParcel` / конструкторе из `Parcel` менять нельзя.

Исходники контрактов в этом репозитории:

| Контракт | AIDL | Java-модели |
| --- | --- | --- |
| `IEsmService` | `app/src/main/aidl/ru/esp/esm/api/` | `app/src/main/java/ru/esp/esm/api/` |
| `IPiotManager`, esp | `app/src/main/aidl/ru/esm/tspiot/driver/api/` | `app/src/main/java/ru/esm/tspiot/driver/api/` |
| `IPiotManager`, atol | `app/src/atol/aidl/ru/atol/os/tspiot/driver/api/` | `app/src/atol/java/ru/atol/os/tspiot/driver/api/` |
| `IMarkingManager`, esp | `app/src/esp/aidl/ru/esm/tspiot/api/` | `app/src/esp/java/ru/esm/tspiot/api/` |
| `IMarkingManager`, atol | `app/src/atol/aidl/ru/atol/os/tspiot/api/` | `app/src/atol/java/ru/atol/os/tspiot/api/` |

Модели `IPiotManager` в flavor `atol` и `esp` совпадают по полям. Отличается Java/AIDL-пакет, включая EAN: на Атол это `ru.atol.os.tspiot.driver.api.model.ean`, на остальных терминалах — `ru.esm.tspiot.driver.api.model.ean`. Классы из `ru.esm…` на Атол в Binder не передаются. Демо конвертирует их только внутри приложения, перед вызовом сервиса.
