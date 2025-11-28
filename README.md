AIDL интерфейс IMarkingManager, у которого только 1 метод requestCheck (метод для проверки марок).
У метода requestCheck, есть 2 параметра IBundleResultCallback и MarkingVerifyRequest.
Интерфейс IBundleResultCallback имеет 2 метода onSuccess(принимает на вход Bundle) и onError(in int code, in String message).
В зависимости от результата ТС ПИоТ возвращает или 
callback.onError(code, msg) или
callback.onSuccess(Bundle().apply { putParcelable("key", MarkingVerifyResponse) })

Имя пакета для подключения будет зависить от вендора (Атол, MSPos, др.)
