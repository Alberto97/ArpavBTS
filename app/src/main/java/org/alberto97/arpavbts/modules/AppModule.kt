package org.alberto97.arpavbts.modules

import org.alberto97.arpavbts.repositories.BtsRepository
import org.alberto97.arpavbts.repositories.IBtsRepository
import org.alberto97.arpavbts.tools.GestoriUtils
import org.alberto97.arpavbts.tools.IGestoriUtils
import org.alberto97.arpavbts.viewmodels.MapViewModel
import org.alberto97.arpavbts.viewmodels.StartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<IBtsRepository> {
        BtsRepository(get(), get())
    }

    single<IGestoriUtils> { GestoriUtils() }

    viewModel { MapViewModel(get(), get(), get()) }
    viewModel { StartViewModel() }
}