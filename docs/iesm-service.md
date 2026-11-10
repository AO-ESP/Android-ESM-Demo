# IEsmService

Актуальный интерфейс ЕСМ: проверка кодов маркировки и EAN/GTIN, регистрация продажи и возврата в локальном модуле «Честный ЗНАК», список проданных кодов, сведения о ККТ и ЛМ.

Исходники: `app/src/main/aidl/ru/esp/esm/api/IEsmService.aidl`, модели в `app/src/main/java/ru/esp/esm/api/model/`.

Подключение, `<queries>` и разбор Bundle — в [connection.md](connection.md). Карта версий — в [README.md](README.md).

## Идентификация

| | |
| --- | --- |
| Action | `ru.esp.esm.action.ACTION_ESM_SERVICE` |
| Пакет процесса на Атол | `ru.atol.os.tspiot` |
| Пакет процесса, flavor `esp` | `ru.esp.tspiot` |
| Интерфейс | `ru.esp.esm.api.IEsmService` |
| Колбэк | `ru.esp.esm.api.IBundleResultCallback` |
| Ключ ответа в Bundle | `"key"` |

Пакет интерфейса один на оба flavor. `setPackage` разный.

```aidl
package ru.esp.esm.api;

import ru.esp.esm.api.IBundleResultCallback;
import ru.esp.esm.api.model.CodesCheckRequest;

interface IEsmService {
    int getAidlVersion();
    void codesCheck(in IBundleResultCallback callback, in CodesCheckRequest codes);
    void cisSell(in IBundleResultCallback callback, in List<String> cisList);
    void cisReturn(in IBundleResultCallback callback, in List<String> cisList);
    void cisSold(in IBundleResultCallback callback, int skip, int limit);
    void getInfo(in IBundleResultCallback callback);
    void eanCheck(in IBundleResultCallback callback, in List<String> eanList);
}
```

```aidl
package ru.esp.esm.api;

import android.os.Bundle;

interface IBundleResultCallback {
    void onSuccess(in Bundle bundle);
    void onError(in int code, in String message);
}
```

`onError` не различает коды в этом клиенте: `code` и `message` приходят как есть. `message` может быть `null`.

## getAidlVersion

Синхронный метод, с версии 2. Колбэка нет.

```text
int getAidlVersion();
```

Возвращает номер версии интерфейса на запущенном сервисе. Демо считает сервис живым, если вызов завершился без исключения.

Ошибки на стороне клиента демо, не из сервиса:

| Результат | Смысл |
| --- | --- |
| успешный `int` | сервис ответил |
| код `-1` | `RemoteException` |
| код `-2` | любое другое исключение |
| стаб `null` | bind ещё не завершён или сервис отключён |

Метод можно вызывать как ping. Он не ходит в ГИС МТ и не меняет состояние ЛМ.

## codesCheck

Версия 2. С версии 4 в запрос можно класть и код идентификации, и EAN/GTIN. Ответ с версии 4 дополнительно содержит `eans`. Старый клиент, который не читает хвост `eans`, с новым сервером несовместим по этому полю: `eans` пишется в конец `Parcel` без маркера длины «было/не было». Копируйте модель той версии сервиса, с которой интегрируетесь.

```text
void codesCheck(in IBundleResultCallback callback, in CodesCheckRequest codes);
```

Назначение: проверка кодов в ГИС МТ и в ЛМ ЧЗ.

### Запрос `CodesCheckRequest`

Пакет `ru.esp.esm.api.model`. Оба поля обязательны, `null` в конструкторе не принимается.

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `cisList` | `CisList` | `getCisList()` | Список кодов и часовой пояс |
| `clientInfo` | `ClientInfo` | `getClientInfo()` | Идентификация кассового ПО |

Порядок в `Parcel`: сначала `cisList`, затем `clientInfo`, оба как `Parcelable`.

#### `CisList`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `codesList` | `List<Cis>` | обязателен | `getCodesList()` | Коды на проверку |
| `tz` | `Integer` | nullable | `getTz()` | Часовой пояс. В демо передаётся как `Int?` |

`tz == null` пишется байтом `0`. Иначе байт `1` и `int`.

#### `Cis`

Код идентификации.

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `cis` | `String` | обязателен | `getCis()` | КИ или, с версии 4, EAN/GTIN |
| `pg` | `Integer` | nullable | `getPg()` | Товарная группа. `null`, если неизвестна |

`pg == null` — байт `0`. Иначе байт `1` и `int`.

#### `ClientInfo`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `name` | `String` | обязателен | `getClientName()` | Имя кассового ПО |
| `version` | `String` | обязателен | `getClientVersion()` | Версия кассового ПО |
| `id` | `String` | обязателен | `getClientId()` | Идентификатор клиента |
| `token` | `String` | обязателен | `getClientToken()` | Токен клиента |
| `lastkey` | `String` | nullable | `getLastKey()` | Предыдущий ключ. Конструктор из четырёх аргументов пишет `null` |

`lastkey == null` — байт `0` без строки. Иначе байт `1` и строка.

Демо подставляет имя `"ESM Test"`, версию `"1.0"` и один и тот же UUID в `id` и `token`, `lastkey = null`. Для продуктовой кассы нужны свои значения.

### Ответ `CodesCheckResponse`

Класс в Bundle: `ru.esp.esm.api.model.CodesCheckResponse`, ключ `"key"`.

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `code` | `int` | обязателен | `getCode()` | Код результата проверки |
| `description` | `String` | обязателен | `getDescription()` | Текст результата |
| `codes` | `List<MarkingCodeInfo>` | обязателен | `getCodes()` | Результат по каждому КИ |
| `reqId` | `String` | обязателен | `getReqId()` | Идентификатор запроса |
| `reqTimestamp` | `long` | обязателен | `getReqTimestamp()` | Метка времени запроса |
| `isCheckedOffline` | `boolean` | обязателен | `isCheckedOffline()` | Проверка выполнена офлайн |
| `inst` | `String` | nullable | `getInst()` | Идентификатор экземпляра |
| `version` | `String` | nullable | `getVersion()` | Версия источника ответа |
| `eans` | `List<EanCheckResult>` | обязателен, с версии 4 | `getEans()` | Результаты EAN. Старые конструкторы подставляют пустой список |

`inst` и `version`: байт `0` и отсутствие строки, либо байт `1` и строка. `isCheckedOffline`: байт `0` или `1`.

Элемент `eans` — `ru.esp.esm.api.model.EanCheckResult`. Это не модель `IPiotManager.eanCheck`. Поля другие. См. раздел `eanCheck` ниже.

### `MarkingCodeInfo` в ответе `codesCheck`

Пакет `ru.esp.esm.api.model`. Это не `MarkingCodeInfo` устаревшего `IMarkingManager`: там `groupId` имеет тип `List<String>` и есть поля `isFromOnlineServer`, `duration`. Здесь `groupIds` — `List<Integer>`, этих двух полей нет.

Все поля, кроме `cis`, nullable. Публичного конструктора со всеми полями нет: объект собирает сервис и клиент только читает его из `Parcel`.

| Поле | Тип | Геттер |
| --- | --- | --- |
| `cis` | `String`, обязателен | `getCis()` |
| `found` | `Boolean` | `getFound()` |
| `valid` | `Boolean` | `getValid()` |
| `printView` | `String` | `getPrintView()` |
| `gtin` | `String` | `getGtin()` |
| `isGreyGtin` | `Boolean` | `getGreyGtin()` |
| `groupIds` | `List<Integer>` | `getGroupIds()` |
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

Порядок полей в `Parcel` совпадает с таблицей сверху вниз. Кодирование nullable в этом классе:

| Тип | `null` | Значение |
| --- | --- | --- |
| `String` | байт `0` | байт `1` + строка |
| `Integer`, `Long` | байт `0` | байт `1` + `int` / `long` |
| `Boolean` | байт `2` | байт `1` = true, байт `0` = false |
| `List<String>` | байт `0` | байт `1` + `writeStringList` |
| `List<Integer>` | `int -1` | `int size`, затем на каждый элемент байт `0` (`null`) или байт `1` + `int` |
| `Map<String, String>` | байт `0` | байт `1`, `int size`, пары key/value строками |

Комментарий в исходнике помечает `isGreyGtin` как поле, которого нет в протоколе и которое нужно уточнить. Клиент его всё равно читает и пишет.

### Пример

```kotlin
val request = CodesCheckRequest(
    CisList(
        listOf(
            Cis("010460043993125621JgXJ5.T", 1),
            Cis("04690604000001"),
        ),
        tz = null,
    ),
    ClientInfo(
        "CashApp",
        "2.3.1",
        clientId,
        clientToken,
        null,
    ),
)

esmService.codesCheck(callback, request)
```

Чтение ответа:

```kotlin
override fun onSuccess(bundle: Bundle) {
    bundle.classLoader = CodesCheckResponse::class.java.classLoader
    val response = bundle.getParcelable("key", CodesCheckResponse::class.java)
    val offline = response?.isCheckedOffline()
    val eans = response?.eans.orEmpty()
}
```

На API ниже 33 используйте `getParcelable(key)` без класса. `classLoader` нужен в обоих вариантах.

## cisSell

Версия 2. Регистрация продажи в ЛМ ЧЗ.

```text
void cisSell(in IBundleResultCallback callback, in List<String> cisList);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `callback` | `IBundleResultCallback` | Результат |
| `cisList` | `List<String>` | Коды идентификации проданных товаров |

Ответ в `"key"`: `ru.esp.esm.api.model.lm.LmRegisterResponse`.

### `LmRegisterResponse`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `results` | `List<Result>` | nullable, элементы тоже могут быть `null` | `getResults()` | Результат по каждому коду |

`results == null`: байт `0`. Иначе байт `1`, `int size`, затем на элемент байт `0` (`null`) или байт `1` и `Parcelable`.

#### `LmRegisterResponse.Result`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `cis` | `String` | nullable | `getCis()` | Код |
| `success` | `Boolean` | nullable | `getSuccess()` | Продажа зарегистрирована |

Оба поля: байт `0` = `null`. Для `cis` далее строка. Для `success` далее байт `1` или `0`.

```kotlin
esmService.cisSell(callback, listOf(cis))

// onSuccess:
bundle.classLoader = LmRegisterResponse::class.java.classLoader
val response = bundle.getParcelable("key", LmRegisterResponse::class.java)
val first = response?.results?.firstOrNull()
val ok = first?.success
```

## cisReturn

Версия 2. Регистрация возврата в ЛМ ЧЗ. Контракт запроса и ответа совпадает с `cisSell`.

```text
void cisReturn(in IBundleResultCallback callback, in List<String> cisList);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `cisList` | `List<String>` | Коды, которые возвращаются |

Ответ: `LmRegisterResponse` в ключе `"key"`. `Result.success == true` означает, что возврат по этому коду зарегистрирован.

## cisSold

Версия 2. Постраничный список проданных товаров из ЛМ ЧЗ.

```text
void cisSold(in IBundleResultCallback callback, int skip, int limit);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `skip` | `int` | Сколько записей пропустить |
| `limit` | `int` | Размер страницы |

Ответ в `"key"`: `ru.esp.esm.api.model.lm.LmSoldCommodities`.

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `totalCount` | `Integer` | nullable | `getTotalCount()` | Всего проданных кодов |
| `cisList` | `List<String>` | nullable, элементы могут быть `null` | `getCisList()` | Страница кодов |

Оба поля: байт `1` и значение, либо байт `0` и `null`. У списка после флага идёт `size`, затем на элемент байт `1` + строка или байт `0`.

```kotlin
esmService.cisSold(callback, skip = 0, limit = 50)
```

## getInfo

Версия 3, ЕСМ v1.4.2. Сведений о запросе нет.

```text
void getInfo(in IBundleResultCallback callback);
```

Назначение: информация о ЕСМ, ККТ и ЛМ ЧЗ.

Ответ в `"key"`: `ru.esp.esm.api.model.KktInfoExternal`.

На сервисе старше версии 3 вызов падает. Демо ловит `UnsupportedOperationException` и исключение транзакции и показывает ошибку с кодом `-1`. Перед вызовом читайте `getAidlVersion()`.

### `KktInfoExternal`

| Поле | Тип в Parcel | Обязательность | Публичный доступ | Смысл |
| --- | --- | --- | --- | --- |
| `tsPiotId` | `String` | nullable | только `toString()` | Идентификатор ТС ПИоТ |
| `kktSerial` | `String` | nullable | только `toString()` | Заводской номер ККТ |
| `fnSerial` | `String` | nullable | только `toString()` | Номер фискального накопителя |
| `kktInn` | `String` | nullable | только `toString()` | ИНН владельца ККТ |
| `codesCheckTimeout` | `long` | пишется примитивом | только `toString()` | Таймаут проверки кодов. В модели объявлен как `Long`, в Parcel пишется `writeLong` |
| `lm` | `LmInfoExternal` | nullable | только `toString()` | Состояние локального модуля |
| `licenceTill` | `String` | nullable, с версии 4 | `getLicenceTill()` | Срок лицензии |

Публичный геттер есть только у `licenceTill`. Остальные поля приватные. Демо печатает объект через `toString()`. Чтобы читать поля из кода, добавьте геттеры в копию класса. Порядок `writeToParcel` при этом не меняйте.

`licenceTill` дописан в конец. Чтение: если после `lm` в `Parcel` ещё есть байты (`dataAvail() > 0`), читается строка, иначе поле остаётся `null`. Старый клиент останавливается на `lm` и хвост не читает. Новый клиент на старом сервере получает `licenceTill == null`.

Порядок записи: `tsPiotId`, `kktSerial`, `fnSerial`, `kktInn`, `codesCheckTimeout`, `lm`, `licenceTill`.

### `LmInfoExternal`

Вложен в `KktInfoExternal.lm`. Публичных геттеров нет, поля видны в `toString()`.

| Поле | Тип | Смысл |
| --- | --- | --- |
| `version` | `String` | Версия ЛМ |
| `status` | `String` | Статус ЛМ |
| `lastSync` | `long` | Время последней синхронизации |
| `token` | `String` | Токен ЛМ |
| `expDate` | `String` | Срок действия |
| `ip` | `String` | Адрес ЛМ |
| `port` | `int` | Порт ЛМ |
| `login` | `String` | Логин |
| `pass` | `String` | Пароль |

Порядок в `Parcel` совпадает с таблицей. Отдельных флагов nullable нет: строки читаются как обычный `readString()` и могут быть `null`, если так записал сервис.

`token`, `login` и `pass` — учётные данные ЛМ. Не пишите их в логи продуктовой кассы. `toString()` демо их содержит.

```kotlin
override fun onSuccess(bundle: Bundle) {
    bundle.classLoader = KktInfoExternal::class.java.classLoader
    val info = bundle.getParcelable("key", KktInfoExternal::class.java)
    val licenceTill = info?.licenceTill
}
```

## eanCheck

Версия 4, ЕСМ v1.*, ещё не в релизе. Проверка EAN в ЛМ ЧЗ.

Это отдельный контракт от `IPiotManager.eanCheck`. Здесь на вход список строк, на выход другая модель с другими полями. Интеграция через ТС ПИоТ описана в [ipiot-manager.md](ipiot-manager.md) и в файлах `ean-check-*.md`.

```text
void eanCheck(in IBundleResultCallback callback, in List<String> eanList);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `eanList` | `List<String>` | Значения EAN/GTIN |

Ответ в `"key"`: `ru.esp.esm.api.model.EanCheckResponse`.

### `EanCheckResponse`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `results` | `List<EanCheckResult>` | обязателен | `getResults()` | Результат по каждому EAN |

### `EanCheckResult` (`ru.esp.esm.api.model`)

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `gtin` | `String` | обязателен | `getGtin()` | Проверенный EAN/GTIN |
| `EANblocked` | `boolean` | обязателен | `getEANblocked()` | Признак блокировки. В Parcel пишется `int` `1` или `0` |
| `message` | `String` | nullable | `getMessage()` | Текст. `int 0` = `null`, `int 1` + строка = значение |

Поля `itemId`, `hasBlockedItems` и `error` у этого ответа нет. Они есть только у `IPiotManager.eanCheck`.

Тот же `EanCheckResult` вложен в `CodesCheckResponse.eans`, когда `codesCheck` получает EAN/GTIN.

```kotlin
esmService.eanCheck(callback, listOf("4690228020056", "4600682003847"))
```
