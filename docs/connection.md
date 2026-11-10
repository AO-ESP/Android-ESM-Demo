# Подключение к сервисам ТС ПИоТ

Клиент находит сервис по action и пакету, биндится через `Context.bindService` и получает AIDL-стаб из `IBinder`. Вызовы идут в другой процесс. Жизненный цикл bind/unbind держит приложение-клиент.

## Action и пакеты

| Сервис | Action | `setPackage`, flavor `atol` | `setPackage`, flavor `esp` |
| --- | --- | --- | --- |
| `IEsmService` | `ru.esp.esm.action.ACTION_ESM_SERVICE` | `ru.atol.os.tspiot` | `ru.esp.tspiot` |
| `IPiotManager` | `ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER` | `ru.atol.os.tspiot` | не используется |
| `IPiotManager` | `ru.esm.tspiot.action.ACTION_PIOT_MANAGER` | не используется | `ru.esp.tspiot` |
| `IMarkingManager` | `ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER` | `ru.atol.os.tspiot` | не используется |
| `IMarkingManager` | `ru.esm.tspiot.action.ACTION_MARKING_MANAGER` | не используется | `ru.esp.tspiot` |

`setPackage` обязателен. Intent только с action на Android 11+ не резолвится в неявный сервис другого приложения.

## AndroidManifest

На Android 11 (API 30) и выше пакеты, которые приложение не видит само, нужно объявить в `<queries>`. Иначе `bindService` вернёт `false`, и `onServiceConnected` не придёт.

Демо объявляет все action сразу, чтобы одна сборка могла говорить с обоими flavor. В продуктовом приложении достаточно action того терминала, на котором оно стоит.

```xml
<queries>
    <intent>
        <action android:name="ru.esp.esm.action.ACTION_ESM_SERVICE" />
    </intent>
    <intent>
        <action android:name="ru.esm.tspiot.action.ACTION_PIOT_MANAGER" />
    </intent>
    <intent>
        <action android:name="ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER" />
    </intent>
    <intent>
        <action android:name="ru.esm.tspiot.action.ACTION_MARKING_MANAGER" />
    </intent>
    <intent>
        <action android:name="ru.atol.os.tspiot.action.ACTION_MARKING_MANAGER" />
    </intent>
</queries>
```

Отдельное permission на bind этим сервисам в демо не запрашивается.

## Bind и unbind

Флаг — `Context.BIND_AUTO_CREATE`: процесс сервиса поднимается, если он ещё не запущен. Повторный `bindService` при уже живом стабе клиент демо игнорирует. `unbindService` вызывается только если соединение было установлено, иначе Android бросит `IllegalArgumentException`.

`onServiceDisconnected` приходит, когда процесс сервиса умер. Это не парный вызов к `unbindService`. После него стаб нужно обнулить и биндиться заново.

Пример для `IEsmService`. Для `IPiotManager` и `IMarkingManager` меняются action, пакет и класс стаба (`IPiotManager.Stub`, `IMarkingManager.Stub`).

```kotlin
private const val ESM_SERVICE_ACTION = "ru.esp.esm.action.ACTION_ESM_SERVICE"
private const val ATOL_PACKAGE = "ru.atol.os.tspiot"
private const val OTHER_PACKAGE = "ru.esp.tspiot"

private var esmService: ru.esp.esm.api.IEsmService? = null

private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        esmService = ru.esp.esm.api.IEsmService.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        esmService = null
    }
}

fun connect(context: Context, atol: Boolean) {
    if (esmService != null) return
    val intent = Intent(ESM_SERVICE_ACTION).apply {
        setPackage(if (atol) ATOL_PACKAGE else OTHER_PACKAGE)
    }
    val bound = context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    // bound == false: сервис не найден (нет приложения, неверный action/пакет, нет <queries>)
}

fun disconnect(context: Context) {
    if (esmService == null) return
    context.unbindService(connection)
    esmService = null
}
```

`bindService` возвращает `false`, если сервис не найден. Исключение при bind (например, `SecurityException`) демо логирует и не пробрасывает: соединение просто не устанавливается. Имеет смысл показать пользователю, что ТС ПИоТ не установлен или версия не совпадает.

Вызовы AIDL делайте после `onServiceConnected`. До этого стаб равен `null`. Демо в этом случае возвращает состояние «сервис недоступен» и метод не вызывает.

## Колбэки

Почти все методы асинхронные: клиент передаёт AIDL-callback, сервис отвечает в нём. Колбэк приходит на Binder-потоке, не на главном. Обновление UI переносите на main.

| Интерфейс | Где используется | Успех | Ошибка |
| --- | --- | --- | --- |
| `ru.esp.esm.api.IBundleResultCallback` | все методы `IEsmService`, кроме `getAidlVersion` | `onSuccess(Bundle)` | `onError(int code, String message)` |
| `ru.esm.tspiot.driver.api.callback.IBundleResultCallback` или `ru.atol.os.tspiot.driver.api.callback.IBundleResultCallback` | `IPiotManager.eanCheck` | `onSuccess(Bundle)` | `onError(int code, String message)` |
| `IResultCallback` того же пакета, что `IPiotManager` | `setImcData`, `setError`, `setIsmNotice`, `setRawEvent`, `setReceiptInfo`, `setKktInfo`, `setCashier` | `onSuccess()` | `onFailure(int code, String message)` |
| `IBoolCallback` того же пакета | `setShiftState` | `onSuccess(boolean status)` | `onFailure(int code, String message)` |
| `IBundleResultCallback` пакета `IMarkingManager` | `requestCheck` | `onSuccess(Bundle)` | `onError(int code, String message)` |

`code` и `message` задаёт сервис. Клиент демо дополнительно использует свои коды, если вызов не дошёл до сервиса:

| Код на стороне демо | Когда |
| --- | --- |
| `-1` | `RemoteException` или другое исключение при вызове |
| `-2` | исключение `getAidlVersion()`, которое не является `RemoteException` |

Эти два кода не приходят из `onError` сервиса. Сервис в `onError` / `onFailure` передаёт свой `code`.

`message` может быть `null`.

Пустой список в некоторых методах (`IPiotManager.eanCheck` с пустым `items`) сервер может не подтвердить колбэком вообще. Не ждите ответ бесконечно без своего таймаута.

## Разбор Bundle

Ключ parcelable-ответа во всех методах демо — строка `"key"`.

```kotlin
private const val BUNDLE_KEY = "key"

override fun onSuccess(bundle: Bundle) {
    bundle.classLoader = CodesCheckResponse::class.java.classLoader
    val response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelable(BUNDLE_KEY, CodesCheckResponse::class.java)
    } else {
        @Suppress("DEPRECATION")
        bundle.getParcelable<CodesCheckResponse>(BUNDLE_KEY)
    }
}
```

`classLoader` выставляется до `getParcelable`. Иначе Android ищет класс ответа в class loader чужого процесса и не находит класс клиента.

Класс в `getParcelable` — тот, который сервис положил в Bundle. Подмена пакета или пропуск вложенного parcelable даёт `BadParcelableException` / `ClassNotFoundException`.

| Метод | Класс в `"key"` |
| --- | --- |
| `IEsmService.codesCheck` | `ru.esp.esm.api.model.CodesCheckResponse` |
| `IEsmService.eanCheck` | `ru.esp.esm.api.model.EanCheckResponse` |
| `IEsmService.cisSell`, `cisReturn` | `ru.esp.esm.api.model.lm.LmRegisterResponse` |
| `IEsmService.cisSold` | `ru.esp.esm.api.model.lm.LmSoldCommodities` |
| `IEsmService.getInfo` | `ru.esp.esm.api.model.KktInfoExternal` |
| `IPiotManager.eanCheck` | `EanCheckResponse` пакета flavor: `ru.atol.os.tspiot.driver.api.model.ean` или `ru.esm.tspiot.driver.api.model.ean` |
| `IMarkingManager.requestCheck` | `MarkingVerifyResponse` пакета flavor (`ru.esm.tspiot.api.model` или `ru.atol.os.tspiot.api.model`) |

`getAidlVersion`, `setShiftState` и методы с `IResultCallback` Bundle не возвращают.

## Ошибки вызова

- `RemoteException` — процесс сервиса умер во время транзакции или метод отсутствует в старой версии AIDL (transaction mismatch).
- `bindService == false` — неверный action/пакет, нет `<queries>`, ТС ПИоТ не установлен.
- `ClassNotFoundException` при чтении Bundle — пакет модели на клиенте не совпал с пакетом, которым писал сервис, или не выставлен `classLoader`.
- Стаб `null` — `connect` ещё не завершился или уже был `disconnect` / `onServiceDisconnected`.

Один `ServiceConnection` нельзя одновременно передать в два `bindService` без парного `unbind`. Демо держит отдельное соединение на `IEsmService` и отдельное на `IPiotManager`.
