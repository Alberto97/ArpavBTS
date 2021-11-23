package org.alberto97.arpavbts.tools

import android.app.Application
import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@JsonClass(generateAdapter = true)
data class MapState(
    val lat: Double,
    val lon: Double,
    val zoom: Float
)

@Suppress("BlockingMethodInNonBlockingContext")
class MapStateSerializer : Serializer<MapState?> {
    private val adapter = Moshi.Builder().build().adapter(MapState::class.java)

    override val defaultValue: MapState? = null

    override suspend fun readFrom(input: InputStream): MapState? = withContext(Dispatchers.IO) {
        val text = input.bufferedReader().use { it.readText() }
        adapter.fromJson(text)
    }

    override suspend fun writeTo(t: MapState?, output: OutputStream) = withContext(Dispatchers.IO) {
        val text = adapter.toJson(t)
        output.write(text.toByteArray())
    }

}

interface IMapStateStored {
    suspend fun getMapState(): MapState?
    suspend fun setMapState(lat: Double, lon: Double, zoom: Float)
}

@Singleton
class MapStateStored @Inject constructor(val app: Application) : IMapStateStored {
    private val Context.dataStore by dataStore("map_state.json", serializer = MapStateSerializer())

    override suspend fun getMapState(): MapState? {
        return app.dataStore.data.first()
    }

    override suspend fun setMapState(lat: Double, lon: Double, zoom: Float) {
        app.dataStore.updateData { MapState(lat, lon, zoom) }
    }
}