package com.example.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://orhgedegvwjyxyzagbza.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9yaGdlZGVndndqeXh5emFnYnphIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQxMDAxODksImV4cCI6MjA2OTY3NjE4OX0.pNF55O0S0nBIpCHz5uY061GN1SzeB61jebsls9FGKRc"
        ) {
            httpEngine = io.ktor.client.engine.okhttp.OkHttp.create()
            install(Realtime)
            install(Postgrest)
        }
    }
}