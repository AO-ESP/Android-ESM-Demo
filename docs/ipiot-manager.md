# IPiotManager

Интерфейс ТС ПИоТ для кассовых событий: состояние смены, данные КМ, ошибки, уведомления ИСМ, сырое событие, закрытый чек, сведения о ККТ и кассире, проверка EAN/GTIN.

Методы на flavor `atol` и `esp` совпадают. Пакеты интерфейса, колбэков и моделей разные, включая EAN. На Атол запрос и ответ `eanCheck` — `ru.atol.os.tspiot.driver.api.model.ean`. Во flavor `esp` — `ru.esm.tspiot.driver.api.model.ean`. Поля одинаковые. Подмена пакета ломает разбор `Parcel`.

Подключение — в [connection.md](connection.md). Пошаговый вызов только `eanCheck`:

- [ean-check-atol.md](ean-check-atol.md)
- [ean-check-esp.md](ean-check-esp.md)

## Идентификация

| | flavor `atol` | flavor `esp` |
| --- | --- | --- |
| Action | `ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER` | `ru.esm.tspiot.action.ACTION_PIOT_MANAGER` |
| Пакет процесса | `ru.atol.os.tspiot` | `ru.esp.tspiot` |
| Интерфейс | `ru.atol.os.tspiot.driver.api.IPiotManager` | `ru.esm.tspiot.driver.api.IPiotManager` |
| Колбэки | `ru.atol.os.tspiot.driver.api.callback.*` | `ru.esm.tspiot.driver.api.callback.*` |
| Модели, включая EAN | `ru.atol.os.tspiot.driver.api.model.*` | `ru.esm.tspiot.driver.api.model.*` |

Ниже сигнатуры даны для flavor `esp`. На Атол замените `ru.esm.tspiot.driver.api` на `ru.atol.os.tspiot.driver.api`, в том числе у `model.ean`.

```aidl
interface IPiotManager {
    void setShiftState(in IBoolCallback callback, in boolean isClosed, in KktInfo kktInfo);
    void setImcData(in IResultCallback callback, in String imcData, in KktInfo kktInfo, in boolean isOnline);
    void setError(in IResultCallback callback, in ErrorRequest request, in KktInfo kktInfo);
    void setIsmNotice(in IResultCallback callback, in IsmNoticeInfo info, in KktInfo kktInfo);
    void setRawEvent(in IResultCallback callback, in String event);
    void setReceiptInfo(in IResultCallback callback, in ReceiptInfo info, in KktInfo kktInfo);
    void setKktInfo(in IResultCallback callback, in KktInfo kktInfo);
    void setCashier(in IResultCallback callback, in CashierInfo cashierInfo, in KktInfo kktInfo);
    void eanCheck(in IBundleResultCallback callback, in EanCheckRequest request);
}
```

Три колбэка:

```aidl
interface IBoolCallback {
    void onSuccess(boolean status);
    void onFailure(int code, in String message);
}

interface IResultCallback {
    void onSuccess();
    void onFailure(int code, in String message);
}

interface IBundleResultCallback {
    void onSuccess(in Bundle bundle);
    void onError(int code, in String message);
}
```

`onSuccess()` у `IResultCallback` без тела: сервис подтвердил приём. Данные назад не приходят. `onFailure.message` может быть `null`.

Коды лицензии объявлены только в исходниках flavor `atol`, класс `ru.atol.os.tspiot.driver.api.model.ErrorCodes`. Демо их не разбирает отдельно. Сервис может вернуть их в `onFailure`:

| Константа | Значение | Смысл по имени |
| --- | --- | --- |
| `LICENSE_NOT_FOUND` | `108` | Лицензия не найдена |
| `LICENSE_EXPIRED` | `109` | Лицензия истекла |
| `LICENSE_MALFORMED` | `110` | Лицензия повреждена |

## Общие модели

### `KktInfo`

Передаётся почти во все методы. Все поля — `String`, в Parcel пишутся подряд через `writeString` и могут быть `null`.

| Поле | Геттер | Смысл |
| --- | --- | --- |
| `fnSerial` | `getFnSerial()` | Номер фискального накопителя |
| `kktInn` | `getKktInn()` | ИНН |
| `kktSerial` | `getKktSerial()` | Заводской номер ККТ |
| `firmwareVersion` | `getFirmwareVersion()` | Версия прошивки ККТ |
| `fnVersion` | `getFnVersion()` | Версия ФН |

Порядок в `Parcel`: `fnSerial`, `kktInn`, `kktSerial`, `firmwareVersion`, `fnVersion`.

Это не `KktInfoExternal` из `IEsmService.getInfo`. Другой пакет, другой набор полей, другое направление: здесь касса передаёт данные в ТС ПИоТ.

### `CashierInfo`

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `info` | `String` | `getInfo()` | ФИО или наименование кассира |
| `inn` | `String` | `getInn()` | ИНН кассира |

Оба поля nullable на уровне Parcel (`writeString`).

Вспомогательный разбор JSON, это не формат AIDL:

| Ключ JSON | Поле |
| --- | --- |
| `info` | `info` |
| `inn` | `inn` |

Отсутствующий ключ становится пустой строкой (`optString`).

### `ErrorRequest` и `ErrorInfo`

`ErrorRequest.errors` — `List<ErrorInfo>`, геттер `getErrors()`. В Parcel список пишется `writeTypedList`.

| Поле `ErrorInfo` | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `code` | `int` | `getCode()` | Код |
| `message` | `String` | `getMessage()` | Текст |
| `module` | `String` | `getModule()` | Модуль-источник |
| `type` | `String` | `getType()` | Уровень |

Константы `type` на классе `ErrorInfo`:

| Константа | Значение |
| --- | --- |
| `ERROR_TYPE` | `"error"` |
| `INFO_TYPE` | `"info"` |
| `WARNING_TYPE` | `"warning"` |
| `DEBUG_TYPE` | `"debug"` |

Порядок в `Parcel`: `code`, `message`, `module`, `type`.

### `IsmNoticeInfo`

Уведомление в ИСМ.

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `issueDate` | `String` | `getIssueDate()` | Дата формирования |
| `sendDate` | `String` | `getSendDate()` | Дата отправки |
| `noticeId` | `int` | `getNoticeId()` | Номер уведомления |
| `receiptId` | `String` | `getReceiptId()` | Идентификатор чека |

Порядок в `Parcel`: `issueDate`, `sendDate`, `noticeId`, `receiptId`.

Вспомогательный JSON (`IsmNoticeInfo.fromJson`), ключи не camelCase, кроме последнего:

| Ключ JSON | Поле | Если ключа нет |
| --- | --- | --- |
| `issue_date` | `issueDate` | `""` |
| `send_date` | `sendDate` | `""` |
| `notice_id` | `noticeId` | `-1` |
| `receiptId` | `receiptId` | `""` |

### `ReceiptInfo`

Закрытый чек.

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `receiptId` | `String` | `getReceiptId()` | Идентификатор чека |
| `imcData` | `List<ReceiptImcData>` | `getImcData()` | Позиции чека |

`receiptId` пишется строкой и может быть `null`. Список — `writeTypedList`.

### `ReceiptImcData`

Позиция чека.

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `ki` | `String` | nullable | `getKi()` | Код идентификации |
| `ofdStatus` | `String` | nullable | `getOfdStatus()` | Статус ОФД |
| `goodsInfo` | `GoodsInfo` | nullable | `getGoodsInfo()` | Реквизиты позиции, тег 1059 |

Маркированная позиция заполняет `ki`, `ofdStatus` и `goodsInfo`. Позиция без КМ передаёт только `goodsInfo`.

Совместимость Parcel. Конструктор `(ki, ofdStatus)` оставлен и пишет `goodsInfo = null`. Без `goodsInfo` в `Parcel` только две строки. Если `goodsInfo` задан, после строк пишется маркер `0x47494E46` (`"GINF"` в ASCII) и сам `Parcelable`. Чтение без маркера возвращает позицию `Parcel` назад: следующий `int` равен `0` или `1` и принадлежит флагу элемента списка либо началу `KktInfo`, а не имени класса. Старый клиент, который читает только две строки, новый хвост не потребляет, пока не встретит маркер. Меньше 4 байт после двух строк считается отсутствием `goodsInfo`.

Вспомогательный JSON одной позиции (`fromJson`) и массива (`fromJsonArray`):

| Ключ | Поле | Пустое или отсутствующее |
| --- | --- | --- |
| `ki` | `ki` | `null` |
| `ofd_status` | `ofdStatus` | `null` |
| `goodsInfo` | `goodsInfo` | `null`. Значение — объект или JSON-строка объекта |

### `GoodsInfo`

Реквизиты товарной позиции. Пустая строка и `null` записываются как `"UNKNOWN"`. `NaN` и бесконечность у `double` записываются как `-1`. Целые при разборе JSON, если ключа нет или значение не число, тоже становятся `-1`.

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `goodsName` | `String` | `getGoodsName()` | Наименование |
| `price` | `double` | `getPrice()` | Цена |
| `scannedCode` | `String` | `getScannedCode()` | Считанный код |
| `quantity` | `double` | `getQuantity()` | Количество |
| `paymentType` | `int` | `getPaymentType()` | Признак способа расчёта |
| `itemType` | `int` | `getItemType()` | Признак предмета расчёта |
| `unitMeasurementType` | `int` | `getUnitMeasurementType()` | Мера количества |
| `goodsInfoCheckResult` | `int` | `getGoodsInfoCheckResult()` | Результат проверки сведений о товаре |
| `taxRate` | `int` | `getTaxRate()` | Ставка НДС |
| `amount` | `double` | `getAmount()` | Стоимость |

Ключи JSON в `GoodsInfo.fromJson` совпадают с именами полей, camelCase: `goodsName`, `price`, `scannedCode`, `quantity`, `paymentType`, `itemType`, `unitMeasurementType`, `goodsInfoCheckResult`, `taxRate`, `amount`.

Порядок в `Parcel`: `goodsName` (`String`), `price` (`double`), `scannedCode` (`String`), `quantity` (`double`), `paymentType`, `itemType`, `unitMeasurementType`, `goodsInfoCheckResult`, `taxRate` (`int`), `amount` (`double`).

## setShiftState

Состояние смены.

```text
void setShiftState(in IBoolCallback callback, in boolean isClosed, in KktInfo kktInfo);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `isClosed` | `boolean` | `true` — смена закрыта, `false` — открыта |
| `kktInfo` | `KktInfo` | ККТ, на которой сменилось состояние |

Ответ: `onSuccess(boolean status)` или `onFailure(int code, String message)`. Что означает `status`, сервис отдаёт как флаг; демо только логирует его. Отдельного Bundle нет.

```kotlin
manager.setShiftState(callback, isClosed = false, kktInfo)
```

## setImcData

Передача данных кода маркировки.

```text
void setImcData(in IResultCallback callback, in String imcData, in KktInfo kktInfo, in boolean isOnline);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `imcData` | `String` | JSON данных КМ |
| `kktInfo` | `KktInfo` | ККТ |
| `isOnline` | `boolean` | Признак онлайн-проверки |

AIDL принимает строку, не Parcelable. Демо кодирует свою модель `ImcData` через kotlinx.serialization. Сейчас у модели одно поле:

```json
{ "ki": "код идентификации" }
```

Имя ключа — `ki`. Успех — пустой `onSuccess()`. Ошибка — `onFailure`.

```kotlin
manager.setImcData(callback, """{"ki":"010460043993125621JgXJ5.T"}""", kktInfo, isOnline = true)
```

## setError

Пакет ошибок кассы или драйвера.

```text
void setError(in IResultCallback callback, in ErrorRequest request, in KktInfo kktInfo);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `request` | `ErrorRequest` | Список `ErrorInfo` |
| `kktInfo` | `KktInfo` | ККТ |

Ответ: `IResultCallback`.

## setIsmNotice

Уведомление ИСМ по чеку.

```text
void setIsmNotice(in IResultCallback callback, in IsmNoticeInfo info, in KktInfo kktInfo);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `info` | `IsmNoticeInfo` | Даты, номер уведомления, чек |
| `kktInfo` | `KktInfo` | ККТ |

Ответ: `IResultCallback`.

## setRawEvent

Сырое событие строкой. Структура строки контрактом не задана: сервис получает текст как есть.

```text
void setRawEvent(in IResultCallback callback, in String event);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `event` | `String` | Тело события |

`KktInfo` в этот метод не передаётся. Ответ: `IResultCallback`.

## setReceiptInfo

Закрытый чек.

```text
void setReceiptInfo(in IResultCallback callback, in ReceiptInfo info, in KktInfo kktInfo);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `info` | `ReceiptInfo` | Идентификатор чека и позиции |
| `kktInfo` | `KktInfo` | ККТ |

Ответ: `IResultCallback`.

```kotlin
val line = ReceiptImcData(
    ki,
    ofdStatus,
    GoodsInfo(
        "Молоко",
        89.90,
        ki,
        1.0,
        paymentType,
        itemType,
        unitMeasurementType,
        checkResult,
        taxRate,
        89.90,
    ),
)
manager.setReceiptInfo(callback, ReceiptInfo(receiptId, listOf(line)), kktInfo)
```

Позиция без марки:

```kotlin
ReceiptImcData(null, null, goodsInfo)
```

Такая позиция пишет в Parcel только две null-строки, без маркера `goodsInfo`. Чтобы передать реквизиты, `goodsInfo` должен быть не `null`: иначе маркер не пишется и сервер новой версии тоже увидит пустые реквизиты.

## setKktInfo

Актуальные сведения о ККТ без другого события.

```text
void setKktInfo(in IResultCallback callback, in KktInfo kktInfo);
```

Ответ: `IResultCallback`.

## setCashier

Текущий кассир.

```text
void setCashier(in IResultCallback callback, in CashierInfo cashierInfo, in KktInfo kktInfo);
```

| Параметр | Тип | Смысл |
| --- | --- | --- |
| `cashierInfo` | `CashierInfo` | Имя и ИНН |
| `kktInfo` | `KktInfo` | ККТ |

Ответ: `IResultCallback`.

## eanCheck

Проверка EAN/GTIN через ТС ПИоТ. Это не `IEsmService.eanCheck`: другой action, другой запрос (`EanCheckRequest`, а не `List<String>`), другой ответ (есть `itemId`, `hasBlockedItems`, `error`).

```text
void eanCheck(in IBundleResultCallback callback, in EanCheckRequest request);
```

Ключ Bundle: `"key"`. Класс ответа: `EanCheckResponse` пакета flavor (`ru.atol.os.tspiot.driver.api.model.ean` или `ru.esm.tspiot.driver.api.model.ean`). Перед чтением выставьте `bundle.classLoader`.

Пустой `items` сервер может не подтвердить колбэком. Не передавайте пустой список и не ждите ответ без таймаута.

### Запрос

Логический вид:

```json
{
  "items": [
    { "itemId": "1", "gtin": "4690228020056" },
    { "itemId": "3", "gtin": "4600682003847" }
  ]
}
```

По AIDL это Parcelable, не JSON.

#### `EanCheckRequest`

| Поле | Тип | Обязательность | Геттер |
| --- | --- | --- | --- |
| `items` | `List<EanCheckItem>` | nullable | `getItems()` |

#### `EanCheckItem`

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `itemId` | `String` | `getItemId()` | Идентификатор позиции на стороне кассы. Вернётся в ответе |
| `gtin` | `String` | `getGtin()` | EAN/GTIN |

Порядок в `Parcel`: `itemId`, затем `gtin`.

### Ответ `EanCheckResponse`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `results` | `List<EanCheckResult>` | nullable | `getResults()` | Результат по каждой позиции |
| `hasBlockedItems` | `boolean` | обязателен | `getHasBlockedItems()` | Есть хотя бы одна заблокированная позиция. В Parcel — `int` `1` или `0` |
| `error` | `GtinCheckError` | nullable | `getError()` | Ошибка проверки целиком. `int 0` = `null`, `int 1` + Parcelable = значение |

#### `EanCheckResult`

| Поле | Тип | Обязательность | Геттер | Смысл |
| --- | --- | --- | --- | --- |
| `itemId` | `String` | обязателен | `getItemId()` | Идентификатор из запроса |
| `gtin` | `String` | обязателен | `getGtin()` | EAN/GTIN |
| `blocked` | `boolean` | обязателен | `isBlocked()` | Признак блокировки, в Parcel `int` `1` или `0` |
| `pg` | `Integer` | nullable | `getPg()` | Товарная группа. `int 0` = `null`, `int 1` + `int` = значение |

#### `GtinCheckError`

| Поле | Тип | Геттер | Смысл |
| --- | --- | --- | --- |
| `code` | `int` | `getCode()` | Код ошибки |
| `message` | `String` | `getMessage()` | Текст |

Порядок: `code`, затем `message`.

```kotlin
val request = EanCheckRequest(
    listOf(
        EanCheckItem("1", "4690228020056"),
        EanCheckItem("3", "4600682003847"),
    )
)
manager.eanCheck(callback, request)
```

Полный каркас bind и чтения Bundle — в [ean-check-atol.md](ean-check-atol.md) и [ean-check-esp.md](ean-check-esp.md).
