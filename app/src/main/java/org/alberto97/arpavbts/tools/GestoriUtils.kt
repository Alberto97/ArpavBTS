package org.alberto97.arpavbts.tools

import android.graphics.Color
import org.alberto97.arpavbts.repositories.IGestoreRepository
import javax.inject.Inject
import javax.inject.Singleton


interface IGestoriUtils {
    fun getColor(gestore: String): Int
}

@Singleton
class GestoriUtils @Inject constructor(private val repository: IGestoreRepository) : IGestoriUtils {

    override fun getColor(gestore: String): Int {
        return repository.getColor(gestore) ?: Color.parseColor("#64dd17")
    }
}