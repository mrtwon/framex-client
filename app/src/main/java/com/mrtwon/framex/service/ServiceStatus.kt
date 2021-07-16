package com.mrtwon.framex.service

enum class ServiceStatus(val description: String) {
    CHECK("Поиск Обновлений ..."),
    NOT_UPDATE("Обновлений нет."),
    UPDATE("Обновление Базы Данных ..."),
    END_UPDATE("База данных обновленна"),
    STOP("Обновление Остановленно."),
    DISCONNECT("Проверьте доступ к сети"),
    STOP_SERVICE("Обновление прерванно.")
}