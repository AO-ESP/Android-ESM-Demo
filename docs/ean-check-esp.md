# eanCheck — интеграция для flavor **esp** (остальные терминалы)

Проверка EAN/GTIN через AIDL-сервис `IPiotManager` ТС ПИоТ на терминалах, отличных от Атол (пакет `ru.esp.tspiot`).

Полный контракт `IPiotManager` (остальные методы, `KktInfo`, чек, ошибки) — в [ipiot-manager.md](ipiot-manager.md). Общие правила bind и Bundle — в [connection.md](connection.md).

Этот `eanCheck` не совпадает с `IEsmService.eanCheck`: там на вход `List<String>`, а ответ — `ru.esp.esm.api.model.EanCheckResponse` (`gtin`, `EANblocked`, `message`). См. [iesm-service.md](iesm-service.md).

Пакет сервиса: `ru.esp.tspiot`  
Action: `ru.esm.tspiot.action.ACTION_PIOT_MANAGER`  
Интерфейс: `ru.esm.tspiot.driver.api.IPiotManager`

---

## 1. AIDL и модели

Скопируйте в своё приложение (пакеты должны совпасть **байт в байт**).

### `ru/esm/tspiot/driver/api/IPiotManager.aidl`

```aidl
package ru.esm.tspiot.driver.api;

import ru.esm.tspiot.driver.api.callback.IBundleResultCallback;
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest;

interface IPiotManager {
    void eanCheck(in IBundleResultCallback callback, in EanCheckRequest request);
}
```

Если `IPiotManager` уже подключён, достаточно добавить строку `eanCheck` и import запроса. Остальные методы менеджера можно оставить как есть.

### `ru/esm/tspiot/driver/api/callback/IBundleResultCallback.aidl`

```aidl
package ru.esm.tspiot.driver.api.callback;

import android.os.Bundle;

interface IBundleResultCallback {
    void onSuccess(in Bundle bundle);
    void onError(in int code, in String message);
}
```

### Запрос

Формат (логически):

```json
{
  "items": [
    { "itemId": "1", "gtin": "4690228020056" },
    { "itemId": "3", "gtin": "4600682003847" }
  ]
}
```

AIDL/Java-модели (пакет `ru.esm.tspiot.driver.api.model.ean`):

- `EanCheckRequest` — поле `items`
- `EanCheckItem` — поля `itemId`, `gtin`

Готовые файлы:

- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/EanCheckRequest.java`
- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/EanCheckItem.java`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/EanCheckRequest.aidl`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/EanCheckItem.aidl`

### Ответ в Bundle

Сервер кладёт в Bundle parcelable **`ru.esm.tspiot.driver.api.model.ean.EanCheckResponse`**.  
Нужны Java-классы и AIDL-заглушки **именно в этом пакете**, иначе при разборе будет `ClassNotFoundException`.

Готовые файлы:

- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/EanCheckResponse.java`
- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/EanCheckResult.java`
- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/GtinCheckError.java`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/EanCheckResponse.aidl`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/EanCheckResult.aidl`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/GtinCheckError.aidl`

В `build.gradle` модуля включите AIDL: `buildFeatures { aidl = true }`.

---

## 2. AndroidManifest

Для Android 11+ объявите запрос к сервису:

```xml
<queries>
    <intent>
        <action android:name="ru.esm.tspiot.action.ACTION_PIOT_MANAGER" />
    </intent>
</queries>
```

---

## 3. Подключение к сервису

```kotlin
private const val PIOT_ESM_MANAGER_ACTION = "ru.esm.tspiot.action.ACTION_PIOT_MANAGER"
private const val OTHER_PACKAGE = "ru.esp.tspiot"

private var piotManager: ru.esm.tspiot.driver.api.IPiotManager? = null

private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        piotManager = ru.esm.tspiot.driver.api.IPiotManager.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        piotManager = null
    }
}

fun connect(context: Context) {
    val intent = Intent(PIOT_ESM_MANAGER_ACTION).apply {
        setPackage(OTHER_PACKAGE)
    }
    context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
}

fun disconnect(context: Context) {
    piotManager?.let {
        context.unbindService(connection)
        piotManager = null
    }
}
```

`setPackage` обязателен: без него `bindService` на современных Android не найдёт ТС ПИоТ.

---

## 4. Вызов `eanCheck`

```kotlin
import android.os.Build
import android.os.Bundle
import ru.esm.tspiot.driver.api.callback.IBundleResultCallback
import ru.esm.tspiot.driver.api.model.ean.EanCheckItem
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest
import ru.esm.tspiot.driver.api.model.ean.EanCheckResponse

private const val BUNDLE_KEY = "key"

fun eanCheck(request: EanCheckRequest) {
    val manager = piotManager ?: return
    if (request.items.isEmpty()) return

    val callback = object : IBundleResultCallback.Stub() {
        override fun onSuccess(bundle: Bundle) {
            bundle.classLoader = EanCheckResponse::class.java.classLoader
            val response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(BUNDLE_KEY, EanCheckResponse::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(BUNDLE_KEY)
            }
            // response.results: List<EanCheckResult>
            // response.hasBlockedItems: Boolean
            // response.error: GtinCheckError? (code, message)
        }

        override fun onError(code: Int, message: String?) {
            // ошибка проверки на стороне ТС ПИоТ
        }
    }

    manager.eanCheck(callback, request)
}
```

`bundle.classLoader` выставляйте **до** `getParcelable`. Иначе класс из чужого процесса не разберётся.

---

## 5. Формат ответа

Ключ Bundle: `"key"`.

`EanCheckResponse`:

| Поле | Тип | Смысл |
| --- | --- | --- |
| `results` | `List<EanCheckResult>` | Результаты по каждой позиции. Список может отсутствовать |
| `hasBlockedItems` | `boolean` | Есть хотя бы одна заблокированная позиция. Геттер `getHasBlockedItems()` |
| `error` | `GtinCheckError?` | Ошибка проверки целиком. Геттер `getError()`. `null`, если ошибки нет |

`EanCheckResult`:

| Поле | Тип | Смысл |
| --- | --- | --- |
| `itemId` | `String` | Идентификатор позиции из запроса |
| `gtin` | `String` | EAN/GTIN |
| `blocked` | `boolean` | Признак блокировки. Геттер `isBlocked()` |
| `pg` | `Integer?` | Товарная группа. Геттер `getPg()`, может отсутствовать |

`GtinCheckError`:

| Поле | Тип | Смысл |
| --- | --- | --- |
| `code` | `int` | Код ошибки. Геттер `getCode()` |
| `message` | `String` | Текст. Геттер `getMessage()` |

В `Parcel` у `error` сначала пишется `int`: `0` — поля нет, `1` — следом parcelable.

Пустой `items`: сервер может **не вызвать** колбэк. Не передавайте пустой список.

---

## 6. Пример

```kotlin
eanCheck(
    EanCheckRequest(
        listOf(
            EanCheckItem("1", "4690228020056"),
            EanCheckItem("3", "4600682003847"),
        )
    )
)
```
