package org.alberto97.arpavbts.modules

import android.content.Context
import androidx.room.Room
import org.alberto97.arpavbts.db.AppDatabase
import org.alberto97.arpavbts.db.BtsRepository
import org.alberto97.arpavbts.db.IBtsRepository
import org.koin.dsl.module

val dbModule = module {
    single { provideAppDb(get()) }
    single { get<AppDatabase>().btsDao() }
    single<IBtsRepository> { BtsRepository(get(), get()) }
}

fun provideAppDb(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
        .build()