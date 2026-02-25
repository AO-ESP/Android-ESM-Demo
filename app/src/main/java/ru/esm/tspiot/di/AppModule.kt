package ru.esm.tspiot.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.esm.tspiot.data.EsmServiceClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideEsmServiceClient(@ApplicationContext context: Context): EsmServiceClient {
        return EsmServiceClient(context)
    }
}