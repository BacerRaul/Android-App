package com.bacer.notesapp.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ShakeEventsBus {
    private val _shakeFlow = MutableSharedFlow<Unit>()
    val shakeFlow = _shakeFlow.asSharedFlow()

    suspend fun sendShakeEvent() {
        _shakeFlow.emit(Unit)
    }
}
