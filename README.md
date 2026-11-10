# Пример AIDL сервисов для интеграции кассового софта с ТС ПИоТ

## IMarkingManager `@Deprecated`

### Описание методов

1. `requestCheck(in IBundleResultCallback callback, in MarkingVerifyRequest info)` Метод для
   проверки марок.

У метода requestCheck, есть 2 параметра IBundleResultCallback и MarkingVerifyRequest.

Интерфейс IBundleResultCallback имеет 2 метода onSuccess(принимает на вход Bundle) и onError(in int code, in String message).

В зависимости от результата ТС ПИоТ возвращает или
`callback.onError(code, msg)` или
`callback.onSuccess(Bundle().apply { putParcelable("key", MarkingVerifyResponse) })`

*Примечание: Имя пакета для подключения зависит от вендора (Атол, MSPos, др.)*

## IPiotManager.setReceiptInfo

`setReceiptInfo(in IResultCallback callback, in ReceiptInfo info, in KktInfo kktInfo)` передаёт
закрытый чек. `ReceiptInfo` содержит `receiptId` и список `ReceiptImcData`.

У `ReceiptImcData` три поля:

- `ki` — код идентификации, nullable
- `ofdStatus` — статус ОФД, nullable
- `goodsInfo` — реквизиты позиции, тег 1059, nullable

Маркированная позиция заполняет `ki`, `ofdStatus` и `goodsInfo`. Позиция без КМ передаёт только
`goodsInfo`.

Поля `GoodsInfo` (ключи JSON в camelCase): `goodsName`, `price`, `scannedCode`, `quantity`,
`paymentType`, `itemType`, `unitMeasurementType`, `goodsInfoCheckResult`, `taxRate`, `amount`.
Пустая строка записывается как `"UNKNOWN"`, отсутствующее или нечисловое значение — как `-1`.

Пакет моделей зависит от flavor:

- mspos: `ru.esm.tspiot.driver.api.model`
- atol: `ru.atol.os.tspiot.driver.api.model`

Конструктор `(ki, ofdStatus)` оставлен. Без `goodsInfo` в `Parcel` пишутся только эти две строки,
как у клиента до ESMA-463. Если `goodsInfo` задан, перед ним пишется маркер `0x47494E46`, затем
сам parcelable. Чтение без маркера возвращает позицию `Parcel` назад: следующий `int` равен `0`
или `1` и принадлежит флагу элемента списка либо `KktInfo`.

## IPiotManager.eanCheck

Проверка EAN/GTIN. Отдельные инструкции по flavor:

- [flavor atol (терминалы Атол)](docs/ean-check-atol.md)
- [flavor mspos (остальные терминалы)](docs/ean-check-mspos.md)

## IEsmService

### Описание методов

`getAidlVersion()` - версия интерфейса

`codesCheck(in IBundleResultCallback callback, in CodesCheckRequest codes)` - проверка КИ в ГИС МТ и
ЛМ ЧЗ

`cisSell(in IBundleResultCallback callback, in List<String> cisList)` - регистрация продажи товаров
в ЛМ ЧЗ

`cisReturn(in IBundleResultCallback callback, in List<String> cisList)` - регистрация возврата
товаров в ЛМ ЧЗ

`cisSold(in IBundleResultCallback callback, int skip, int limit)` - получение списка проданных
товаров из ЛМ ЧЗ

### Подключение к сервису

```
val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"

val PACKAGE = "ru.atol.os.tspiot" // для терминалов Атол
val PACKAGE = "ru.esp.tspiot" // для остальных терминалов

val intent = Intent(ESM_SERVICE_ACTION).apply {setPackage(PACKAGE)}

context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
```

## Особенность сборки
Для сборки ATOL/Все остальные нужно переключить имплементацию интерфейса PiotManagerClient в AppModule