package ru.esm.tspiot.domain

internal object TestEvents {
    fun getUserAuthEvent(): String =
        "{ " +
                "\"event_code\": \"user_auth\"," +
                "\"user_name\": \"Кассиров Кассир\"," +
                "\"user_inn\": \"9715169631\"," +
                "\"auth_method\": \"999\"," +
                "\"debug_info\": \"<отладочная информация>\" " +
                "}"

    fun getKmScanEvent(): String =
        "{" +
                " \"event_code\": \"km_scan\"," +
                " \"event_time_ms\": 1780296676000," +
                " \"event_status\": 0," +
                " \"event_msg\": \"0104670540176099215<pGKy\u001d93dGVz\"," +
                " \"scan_src\": 1," +
                " \"scan_count\": 34," +
                " \"debug_info\": \"<отладочная информация>\"" +
                " }"

    fun getKmPrsngEvent(): String =
        " {" +
                "\"event_code\": \"km_prsng\"," +
                "\"event_time_ms\": 1780296676000," +
                "\"event_status\": 0," +
                "\"event_msg\": \"<текстовое сообщение о событии>\"," +
                "\"scan_count\": 22," +
                "\"si\": true," +
                "\"km_type\": 1305," +
                "\"ki\": \"0104670540176099215<pGKy93dGVz\"," +
                "\"debug_info\": \"<отладочная информация>\" " +
                "}"

    fun getLocalChkEvent(): String =
        "{" +
                "\"event_code\": \"km_local_chk\"," +
                "\"event_time_ms\": 1780296676000," +
                "\"event_status\": 1," +
                "\"event_msg\": \"<текстовое сообщение о событии>\"," +
                "\"fn\": \"9999078902018940\"," +
                "\"ki\": \"0104670540176099215<pGKy93dGVz\"," +
                "\"debug_info\": \"<отладочная информация>\"}"

    fun getKmOfdChkEvent(): String =
        "{\"event_code\": \"km_ofd_chk\"," +
                "\"event_time_ms\": 1780296676000," +
                "\"event_status\": 0," +
                "\"event_msg\": \"<текстовое сообщение о событии>\"," +
                "\"fn\": \"9999078902018940\"," +
                "\"ki\": \"0104670540176099215<pGKy93dGVz\"," +
                "\"debug_info\": \"<отладочная информация>\"" +
                "}"

    fun getKmOfdRspEvent(): String =
        "{" +
                "\"event_code\": \"km_ofd_rsp\"," +
                "\"event_time_ms\": 1780296676000," +
                "\"event_status\": 0," +
                "\"event_result\": 2106," +
                "\"event_msg\": \"<текстовое сообщение о событии>\"," +
                "\"fn\": \"9999078902018940\"," +
                "\"ki\": \"0104670540176099215<pGKy93dGVz\"," +
                "\"debug_info\": \"<отладочная информация>\"" +
                "}"

    fun getFdIssuedEvent(): String =
        "{" +
                "\"event_code\": \"fd_issued\"," +
                "\"event_time_ms\": 1780296676000," +
                "\"event_status\": 0," +
                "\"event_msg\": \"<текстовое сообщение о событии>\"," +
                "\"fn\": \"9999078902018940\"," +
                "\"fd_nmb\": 12345," +
                "\"codes\": [" +
                "\"0104670540176099215<pGKy\u001d93dGVz\"," +
                "\"0104670540176099215'W9Um\"" +
                "]," +
                "\"debug_info\": \"<отладочная информация>" +
                "\"" +
                "}"
}