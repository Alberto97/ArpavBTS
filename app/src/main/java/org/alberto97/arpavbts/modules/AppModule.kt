package org.alberto97.arpavbts.modules

import org.alberto97.arpavbts.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<IBtsRepository> {
        BtsRepository(get(), get())
    }

    single<IGestoriUtils> { GestoriUtils() }

    // ViewModel for Search View
    viewModel { MapViewModel(get()) }

    // ViewModel for Result View
    viewModel { StartViewModel(get()) }
}