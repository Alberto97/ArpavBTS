package org.alberto97.arpavbts

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.alberto97.arpavbts.db.AppDatabase
import org.alberto97.arpavbts.db.BtsRepository
import org.alberto97.arpavbts.db.IBtsRepository
import org.alberto97.arpavbts.repositories.GestoreRepository
import org.alberto97.arpavbts.repositories.IGestoreRepository
import org.alberto97.arpavbts.tools.ArpavApi
import org.alberto97.arpavbts.tools.GestoriUtils
import org.alberto97.arpavbts.tools.IGestoriUtils
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideGestoriUtils(utils: GestoriUtils): IGestoriUtils

    @Binds
    abstract fun provideGestoreRepository(repository: GestoreRepository): IGestoreRepository

    @Binds
    abstract fun provideBtsRepository(repository: BtsRepository): IBtsRepository
}

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
            .build()

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.btsDao()
}

@Module
@InstallIn(ApplicationComponent::class)
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