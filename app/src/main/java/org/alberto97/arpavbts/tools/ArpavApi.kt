package org.alberto97.arpavbts.tools

import org.alberto97.arpavbts.BuildConfig
import org.alberto97.arpavbts.models.arpav.FeatureCollection
import retrofit2.http.GET

interface ArpavApi {

    @GET(BuildConfig.ENDPOINT_URL)
    suspend fun fetchData(): FeatureCollection
}