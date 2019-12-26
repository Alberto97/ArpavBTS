package org.alberto97.arpavbts

import org.alberto97.arpavbts.models.arpav.FeatureCollection
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ArpavService {

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: ArpavService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: buildService().also { instance = it }
            }


        private fun buildService(): ArpavService {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://alberto97.altervista.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ArpavService::class.java)
        }

    }

    @GET("http://alberto97.altervista.org/arpav/v1/impianti.json")
    suspend fun fetchData(): FeatureCollection
}