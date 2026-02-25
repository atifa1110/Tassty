package com.example.core.domain.usecase

import androidx.activity.ComponentActivity
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.UserRepository
import com.stripe.android.model.ConfirmSetupIntentParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddCardToStripeUseCase @Inject constructor(
    private val repository: UserRepository
) {
    // Ambil Secret
    fun getSecret(): Flow<TasstyResponse<String>> = repository.getSetupIntentSecret()

    // Jalankan Stripe (SDK)
    fun launchStripe(activity: ComponentActivity, params: ConfirmSetupIntentParams) {
        repository.confirmStripeSetup(activity, params)
    }

    // Simpan Final ke Backend
    fun finalize(id: String, color: String): Flow<TasstyResponse<String>> =
        repository.saveCardInfo(id, color)
}