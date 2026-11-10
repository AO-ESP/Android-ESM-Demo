package ru.esm.tspiot.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.atol.os.tspiot.driver.api.PiotAtolManagerClientImpl
import ru.esm.tspiot.data.PiotManagerClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PiotClientModule {
    @Provides
    @Singleton
    fun providePiotManagerClient(
        @ApplicationContext context: Context
    ): PiotManagerClient {
        return PiotAtolManagerClientImpl(context)
    }
}
