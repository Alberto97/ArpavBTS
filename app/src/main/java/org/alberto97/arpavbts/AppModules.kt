@file:Suppress("unused")

package org.alberto97.arpavbts

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.alberto97.arpavbts.db.AppDatabase
import org.alberto97.arpavbts.db.AppDbMigrations
import org.alberto97.arpavbts.db.BtsRepository
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.repositories.IOperatorRepository
import org.alberto97.arpavbts.repositories.OperatorRepository
import org.alberto97.arpavbts.tools.*
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideOperatorConfig(utils: OperatorConfig): IOperatorConfig

    @Binds
    abstract fun provideOperatorRepository(repository: OperatorRepository): IOperatorRepository

    @Binds
    abstract fun provideBtsRepository(repository: BtsRepository): IBtsRepository

    @Binds
    abstract fun provideMapStateStored(datastore: MapStateStored): IMapStateStored

    @Binds
    abstract fun provideMapStateManager(datastore: MapStateManagerImpl): MapStateManager
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
            .addMigrations(AppDbMigrations.MIGRATION2_3)
            .build()

    @Singleton
    @Provides
    fun provideBtsDao(db: AppDatabase) = db.btsDao()

    @Singleton
    @Provides
    fun provideOperatorDao(db: AppDatabase) = db.operatorDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://alberto97.altervista.org/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    fun provideBtsApi(retrofit: Retrofit): ArpavApi =
        retrofit.create(ArpavApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object WorkModule {
    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context,
        workerFactory: HiltWorkerFactory
    ): WorkManager {
        val workConfig = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, workConfig)
        return WorkManager.getInstance(context)
    }
}
