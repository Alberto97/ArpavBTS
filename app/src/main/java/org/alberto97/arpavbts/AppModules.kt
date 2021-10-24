package org.alberto97.arpavbts

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.alberto97.arpavbts.db.AppDatabase
import org.alberto97.arpavbts.db.AppDbMigrations
import org.alberto97.arpavbts.db.BtsRepository
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.repositories.IOperatorRepository
import org.alberto97.arpavbts.repositories.OperatorRepository
import org.alberto97.arpavbts.tools.ArpavApi
import org.alberto97.arpavbts.tools.OperatorConfig
import org.alberto97.arpavbts.tools.IOperatorConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://alberto97.altervista.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    fun provideBtsApi(retrofit: Retrofit): ArpavApi =
        retrofit.create(ArpavApi::class.java)
}