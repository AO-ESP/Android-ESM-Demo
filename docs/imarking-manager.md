# IMarkingManager

Устаревший интерфейс проверки марок. В корневом описании демо помечен `@Deprecated`. Для новой интеграции используйте [`IEsmService.codesCheck`](iesm-service.md): тот же сценарий «передать коды и получить результат по каждому», плюс EAN/GTIN с версии 4.

Подключение — в [connection.md](connection.md).

## Идентификация

| | flavor `atol` | flavor `esp` |
| --- | --- | --- |
| Action | `ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER` | `ru.esm.tspiot.action.ACTION_MARKING_MANAGER` |
| Пакет процесса | `ru.atol.os.tspiot` | `ru.esp.tspiot` |
| Интерфейс | `ru.atol.os.tspiot.api.IMarkingManager` | `ru.esm.tspiot.api.IMarkingManager` |
| Колбэк | `ru.atol.os.tspiot.api.IBundleResultCallback` | `ru.esm.tspiot.api.IBundleResultCallback` |
| Модели | `ru.atol.os.tspiot.api.model.*` | `ru.esm.tspiot.api.model.*` |

Поля моделей на обоих flavor совпадают. Пакет должен совпасть с flavor, иначе `Parcel` не разберётся.

Сигнатура одна:

```aidl
interface IMarkingManager {
    void requestCheck(in IBundleResultCallback callback, in MarkingVerifyRequest info);
}
```

```aidl
interface IBundleResultCallback {
    void onSuccess(in Bundle bundle);
    void onError(in int code, in String message);
}
```

Успех: `onSuccess` с `Bundle`, ключ `"key"`, значение `MarkingVerifyResponse`. Ошибка: `onError(code, message)`, `message` может быть `null`. Перед `getParcelable` выставьте `bundle.classLoader` на class loader `MarkingVerifyResponse`.

Демо объявляет `<queries>` на оба action, клиент `requestCheck` есть в flavor-исходниках. Экран демо этот вызов сейчас не выполняет: методы `requestCheck` во ViewModel только пишут лог.

## Запрос `MarkingVerifyRequest`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `codes` | `List<String>` | обязателен | `getCodes()` | Коды маркировки |
| `inn` | `String` | nullable | `getInn()` | ИНН |
| `fnNumber` | `String` | nullable | `getFnNumber()` | Номер фискального накопителя |
| `clientInfo` | `ClientInfo` | обязателен | `getClientInfo()` | Кассовое ПО |

Конструктор из двух аргументов `(codes, clientInfo)` записывает `inn = null` и `fnNumber = null`.

Порядок в `Parcel`: список строк, `inn`, `fnNumber`, затем `clientInfo` как Parcelable.

### `ClientInfo`

Пакет flavor (`ru.esm.tspiot.api.model` или `ru.atol.os.tspiot.api.model`). Это не `ru.esp.esm.api.model.ClientInfo` из `IEsmService`: набор полей тот же, кодирование `lastkey` другое.

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `name` | `String` | обязателен | `getClientName()` | Имя кассового ПО |
| `version` | `String` | обязателен | `getClientVersion()` | Версия |
| `id` | `String` | обязателен | `getClientId()` | Идентификатор клиента |
| `token` | `String` | обязателен | `getClientToken()` | Токен |
| `lastkey` | `String` | nullable | `getLastKey()` | Предыдущий ключ |

В Parcel этого класса `lastkey` пишется обычным `writeString` без байта-флага. `null` допустим как null-строка Parcel. У `ClientInfo` в `IEsmService` перед строкой есть байт `0`/`1`. Классы нельзя подменять.

## Ответ `MarkingVerifyResponse`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `codes` | `List<MarkingCodeInfo>` | обязателен | `getCodes()` | Результат по каждому коду |
| `reqTimestamp` | `long` | обязателен | `getReqTimestamp()` | Метка времени запроса |
| `reqId` | `String` | обязателен | `getReqId()` | Идентификатор запроса |
| `inst` | `String` | nullable | `getInst()` | Экземпляр |
| `version` | `String` | nullable | `getVersion()` | Версия источника |

Порядок в `Parcel`: `codes` (`writeTypedList`), `reqTimestamp`, `reqId`, `inst`, `version`. У `inst` и `version` отдельного флага нет, это `writeString` / `readString`.

В ответе нет полей `code`, `description`, `isCheckedOffline` и списка `eans`. Они есть у `CodesCheckResponse` в `IEsmService`.

### `MarkingCodeInfo`

Пакет flavor. Не путать с `ru.esp.esm.api.model.MarkingCodeInfo`:

| | `IMarkingManager` | `IEsmService.codesCheck` |
| --- | --- | --- |
| Пакет | `…api.model` flavor | `ru.esp.esm.api.model` |
| Товарные группы | `groupId: List<String>`, геттер `getGroupId()` | `groupIds: List<Integer>`, геттер `getGroupIds()` |
| Источник | `isFromOnlineServer: boolean`, геттеры `isFromOnlineServer()` и `isFromOfflineServer()` | нет |
| Длительность | `duration: Long`, геттер `getDuration()` | нет |

Остальные поля совпадают по смыслу. Кроме `cis` и `isFromOnlineServer`, все nullable. Публичный конструктор закрыт, объект собирается через `Builder` или приходит из `Parcel`. Для чтения ответа Builder не нужен.

| Поле | Тип | Геттер |
| --- | --- | --- |
| `cis` | `String`, обязателен | `getCis()` |
| `found` | `Boolean` | `getFound()` |
| `valid` | `Boolean` | `getValid()` |
| `printView` | `String` | `getPrintView()` |
| `gtin` | `String` | `getGtin()` |
| `isGreyGtin` | `Boolean` | `getGreyGtin()` |
| `groupId` | `List<String>` | `getGroupId()` |
| `verified` | `Boolean` | `getVerified()` |
| `realizable` | `Boolean` | `getRealizable()` |
| `utilised` | `Boolean` | `getUtilised()` |
| `expireDate` | `String` | `getExpireDate()` |
| `variableExpirations` | `Map<String, String>` | `getVariableExpirations()` |
| `productionDate` | `String` | `getProductionDate()` |
| `productWeight` | `Long` | `getProductWeight()` |
| `prVetDocument` | `String` | `getPrVetDocument()` |
| `isOwner` | `Boolean` | `getOwner()` |
| `isBlocked` | `Boolean` | `getBlocked()` |
| `ogvs` | `List<String>` | `getOgvs()` |
| `message` | `String` | `getMessage()` |
| `errorCode` | `Integer` | `getErrorCode()` |
| `isTracking` | `Boolean` | `getTracking()` |
| `sold` | `Boolean` | `getSold()` |
| `eliminationState` | `Integer` | `getEliminationState()` |
| `mrp` | `Integer` | `getMrp()` |
| `smp` | `Integer` | `getSmp()` |
| `grayZone` | `Boolean` | `getGrayZone()` |
| `innerUnitCount` | `Integer` | `getInnerUnitCount()` |
| `soldUnitCount` | `Integer` | `getSoldUnitCount()` |
| `packageType` | `String` | `getPackageType()` |
| `packageQuantity` | `Integer` | `getPackageQuantity()` |
| `parent` | `String` | `getParent()` |
| `producerInn` | `String` | `getProducerInn()` |
| `productionSerialNumber` | `String` | `getProductionSerialNumber()` |
| `productionBatchNumber` | `String` | `getProductionBatchNumber()` |
| `factorySerialNumber` | `String` | `getFactorySerialNumber()` |
| `isFromOnlineServer` | `boolean` | `isFromOnlineServer()`, `isFromOfflineServer()` = отрицание |
| `duration` | `Long` | `getDuration()` |

Порядок в `Parcel` совпадает с таблицей. Nullable кодируются так:

| Тип | `null` | Значение |
| --- | --- | --- |
| `String` | байт `0` | байт `1` + строка |
| `Integer`, `Long` | байт `0` | байт `1` + `int` / `long` |
| `Boolean` | байт `2` | байт `1` = true, байт `0` = false |
| `List<String>` | байт `0` | байт `1` + `writeStringList` |
| `Map<String, String>` | байт `0` | байт `1`, `int size`, пары строк key/value |

`isFromOnlineServer` — один байт `0` или `1`, без отдельного null.

`Builder.setFound` не принимает `null` (`Objects.requireNonNull`). Остальные сеттеры `Boolean` принимают `null`. Для интеграции, которая только читает ответ сервиса, Builder не вызывается.

## Пример

Пакеты в примере — flavor `esp`. На Атол замените `ru.esm.tspiot.api` на `ru.atol.os.tspiot.api`.

```kotlin
val request = MarkingVerifyRequest(
    listOf("010460043993125621JgXJ5.T"),
    inn,
    fnNumber,
    ClientInfo(name, version, id, token, null),
)

manager.requestCheck(object : IBundleResultCallback.Stub() {
    override fun onSuccess(bundle: Bundle) {
        bundle.classLoader = MarkingVerifyResponse::class.java.classLoader
        val response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable("key", MarkingVerifyResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable("key")
        }
        val first = response?.codes?.firstOrNull()
        val online = first?.isFromOnlineServer
    }

    override fun onError(code: Int, message: String?) {
    }
}, request)
```

Bind:

```kotlin
val action = if (atol) {
    "ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER"
} else {
    "ru.esm.tspiot.action.ACTION_MARKING_MANAGER"
}
val packageName = if (atol) "ru.atol.os.tspiot" else "ru.esp.tspiot"
val intent = Intent(action).apply { setPackage(packageName) }
context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
```
