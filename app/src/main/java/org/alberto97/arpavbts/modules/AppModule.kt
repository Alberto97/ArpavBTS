package org.alberto97.arpavbts.modules

import org.alberto97.arpavbts.tools.GestoriUtils
import org.alberto97.arpavbts.tools.IGestoriUtils
import org.alberto97.arpavbts.viewmodels.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<IGestoriUtils> { GestoriUtils() }

    viewModel { MapViewModel(get(), get(), get()) }
}