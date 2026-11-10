# eanCheck — интеграция для flavor **atol** (терминалы Атол)

Проверка EAN/GTIN через AIDL-сервис `IPiotManager` ТС ПИоТ на терминалах Атол.

Пакет сервиса: `ru.atol.os.tspiot`  
Action: `ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER`  
Интерфейс: `ru.atol.os.tspiot.driver.api.IPiotManager`

---

## 1. AIDL и модели

Скопируйте в своё приложение (пакеты должны совпасть **байт в байт**).

### `ru/atol/os/tspiot/driver/api/IPiotManager.aidl`

```aidl
package ru.atol.os.tspiot.driver.api;

import ru.atol.os.tspiot.driver.api.callback.IBundleResultCallback;
import ru.esm.tspiot.driver.api.model.ean.EanCheckRequest;

interface IPiotManager {
    void eanCheck(in IBundleResultCallback callback, in EanCheckRequest request);
}
```

Если `IPiotManager` уже подключён, достаточно добавить строку `eanCheck` и import запроса. Остальные методы менеджера можно оставить как есть.

### `ru/atol/os/tspiot/driver/api/callback/IBundleResultCallback.aidl`

```aidl
package ru.atol.os.tspiot.driver.api.callback;

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

Готовые файлы:

- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/EanCheckResponse.java`
- `app/src/main/java/ru/esm/tspiot/driver/api/model/ean/EanCheckResult.java`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/EanCheckResponse.aidl`
- `app/src/main/aidl/ru/esm/tspiot/driver/api/model/ean/EanCheckResult.aidl`

В `build.gradle` модуля включите AIDL: `buildFeatures { aidl = true }`.

---

## 2. AndroidManifest

Для Android 11+ объявите запрос к сервису:

```xml
<queries>
    <intent>
        <action android:name="ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER" />
    </intent>
</queries>
```

---

## 3. Подключение к сервису

```kotlin
private const val PIOT_ATOL_MANAGER_ACTION = "ru.atol.os.tspiot.action.ACTION_PIOT_MANAGER"
private const val ATOL_PACKAGE = "ru.atol.os.tspiot"

private var piotManager: ru.atol.os.tspiot.driver.api.IPiotManager? = null

private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        piotManager = ru.atol.os.tspiot.driver.api.IPiotManager.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        piotManager = null
    }
}

fun connect(context: Context) {
    val intent = Intent(PIOT_ATOL_MANAGER_ACTION).apply {
        setPackage(ATOL_PACKAGE)
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
import ru.atol.os.tspiot.driver.api.callback.IBundleResultCallback
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
| `results` | `List<EanCheckResult>` | Результаты по каждой позиции |
| `hasBlockedItems` | `boolean` | Есть хотя бы одна заблокированная позиция |

`EanCheckResult`:

| Поле | Тип | Смысл |
| --- | --- | --- |
| `itemId` | `String` | Идентификатор позиции из запроса |
| `gtin` | `String` | EAN/GTIN |
| `blocked` | `boolean` | Признак блокировки |
| `pg` | `Integer?` | Товарная группа (может отсутствовать) |

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
