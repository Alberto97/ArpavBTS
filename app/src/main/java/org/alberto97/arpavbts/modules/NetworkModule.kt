package org.alberto97.arpavbts.modules

import org.alberto97.arpavbts.tools.ArpavApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideRetrofit() }
    single { provideBtsApi(get()) }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://alberto97.altervista.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideBtsApi(retrofit: Retrofit): ArpavApi =
    retrofit.create(ArpavApi::class.java)