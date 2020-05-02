package org.alberto97.arpavbts.tools

import org.alberto97.arpavbts.models.arpav.FeatureCollection
import retrofit2.http.GET

interface ArpavApi {

    @GET("http://alberto97.altervista.org/arpav/v1/impianti.json")
    suspend fun fetchData(): FeatureCollection
}