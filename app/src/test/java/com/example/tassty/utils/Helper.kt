package com.example.tassty.utils

suspend fun <T> app.cash.turbine.ReceiveTurbine<T>.awaitState(
    predicate: (T) -> Boolean
): T {
    while (true) {
        val item = awaitItem()
        if (predicate(item)) return item
    }
}