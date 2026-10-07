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

## IEsmService

### Описание методов (версия AIDL интерфейса 2)

`getAidlVersion()` - версия интерфейса

`codesCheck(in IBundleResultCallback callback, in CodesCheckRequest codes)` - проверка КИ в ГИС МТ и
ЛМ ЧЗ + Проверка EAN/GTIN

`cisSell(in IBundleResultCallback callback, in List<String> cisList)` - регистрация продажи товаров
в ЛМ ЧЗ

`cisReturn(in IBundleResultCallback callback, in List<String> cisList)` - регистрация возврата
товаров в ЛМ ЧЗ

`cisSold(in IBundleResultCallback callback, int skip, int limit)` - получение списка проданных
товаров из ЛМ ЧЗ

#### Изменения (версия AIDL интерфейса 3, ЕСМ v1.4.2)
Добавлен метод `getInfo(in IBundleResultCallback callback)` - получение информации о ЕСМ, ККТ, ЛМ ЧЗ

#### Изменения (версия AIDL интерфейса 4, ЕСМ v1.* - еще не в релизе) // TODO Актуализировать версию
Добавлен метод `eanCheck(in IBundleResultCallback callback, in List<String> eanList)` - проверка EAN в ЛМ ЧЗ

В ответ метода `getInfo(...)` в KktInfoExternal добавлен параметр `private final String licenceTill`

В параметр метода `codesCheck(...)` CodesCheckRequest можно передавать как КИ, так и EAN/GTIN

В ответ метода `codesCheck(...)` в CodesCheckResponse добавлен параметр `private final List<EanCheckResult> eans`

### Подключение к сервису

```
val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"

val PACKAGE = "ru.atol.os.tspiot" // для терминалов Атол
val PACKAGE = "ru.esp.tspiot" // для остальных терминалов

val intent = Intent(ESM_SERVICE_ACTION).apply {setPackage(PACKAGE)}

context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
```
