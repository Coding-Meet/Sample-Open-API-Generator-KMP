package com.coding.meet.sampleopengenerator

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sample-Open-API-Generator-KMP",
    ) {
        App()
    }
}