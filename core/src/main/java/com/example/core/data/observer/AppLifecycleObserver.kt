package com.example.core.data.observer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.core.domain.usecase.SyncLocationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLifecycleObserver @Inject constructor(
    private val syncLocationUseCase: SyncLocationUseCase
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            syncLocationUseCase()
        }
    }
}
