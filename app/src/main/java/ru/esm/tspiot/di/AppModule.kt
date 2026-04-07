package ru.esm.tspiot.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
//import ru.atol.os.tspiot.driver.api.PiotAtolManagerClientImpl
import ru.esm.tspiot.data.EsmServiceClient
import ru.esm.tspiot.data.PiotESMManagerClientImpl
import ru.esm.tspiot.data.PiotManagerClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideEsmServiceClient(@ApplicationContext context: Context): EsmServiceClient {
        return EsmServiceClient(context)
    }

    @Provides
    @Singleton
    fun provideEMSPiotManagerClient(
        @ApplicationContext context: Context
    ): PiotManagerClient {
        return PiotESMManagerClientImpl(context)
    }

//    @Provides
//    @Singleton
//    fun provideAtolPiotManagerClient(
//        @ApplicationContext context: Context
//    ): PiotManagerClient {
//        return PiotAtolManagerClientImpl(context)
//    }
}