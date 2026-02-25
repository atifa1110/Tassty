package com.example.core.di

import android.content.Context
import com.stripe.android.Stripe
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StripeModule {

    @Provides
    @Singleton
    fun provideStripe(@ApplicationContext context: Context): Stripe {
        return Stripe(context, "pk_test_luna_kamu_disini")
    }
}