package com.globo.admoblibray.model

sealed class AdEvent {
    object Loading : AdEvent()
    object Loaded : AdEvent()
    data class Failed(val message: String) : AdEvent()
    object Opened : AdEvent()
    object Closed : AdEvent()
}