package com.globo.admoblibray.model

sealed class AdEvent {
    data object Loading : AdEvent()
    data object Loaded : AdEvent()
    data class Failed(val message: String) : AdEvent()
}