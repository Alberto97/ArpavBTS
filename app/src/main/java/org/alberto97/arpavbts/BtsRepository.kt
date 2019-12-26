package org.alberto97.arpavbts

import android.app.Application
import android.util.Log
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.protobuf.ProtoBuf
import org.alberto97.arpavbts.models.BTSData
import org.alberto97.arpavbts.models.BTSList
import java.io.File

interface IBtsRepository {
    suspend fun update()
    fun get(): List<BTSData>
    fun get(gestore: String): List<BTSData>
}

class BtsRepository(val app: Application, private val arpavApi: ArpavApi) : IBtsRepository {
    private val fileName = "impianti.pb"
    private var btsList: List<BTSData>

    init {
        val data = loadData()
        btsList = if (data != null) {
            val obj = ProtoBuf.load(BTSList.serializer(), data)
            obj.list
        } else {
            listOf()
        }
    }

    private fun loadData(): ByteArray? {
        val extData = File(app.getExternalFilesDir(null), fileName)
        val data = File(app.filesDir, fileName)

        return when {
            extData.exists() -> {
                Log.d("ArpavBTS", "Lettura da risorsa esterna")
                extData.readBytes()
            }
            data.exists() -> {
                Log.d("ArpavBTS", "Lettura da risorsa interna")
                data.readBytes()
            }
            else -> null
//                Log.d("ArpavBTS", "Lettura da risorsa bundled")
//                val bundled = app.resources.openRawResource(R.raw.impianti)
//                bundled.readBytes()
        }
    }

    override fun get(): List<BTSData> {
        return btsList
    }

    override fun get(gestore: String): List<BTSData> {
        return btsList.filter { it.gestore == gestore }
    }

    @UnstableDefault
    override suspend fun update() {

        val dir = app.filesDir
        val outFile = File(dir, fileName)

        // Fetch new data
        val data = arpavApi.fetchData()
        val list = data.features.map {
            val geometry = it.geometry
            val position = geometry.coordinates[0]
            val longitudine = position[0]
            val latitudine = position[1]

            val props = it.properties
            val address = props.indirizzo ?: ""

            with(props) {
                BTSData(idImpianto, codice, nome, gestore, address, comune, provincia,
                    latitudine, longitudine, quotaSlm, postazione, pontiRadio)
            }
        }

        // Update list
        btsList = list

        // Save
        val btsList = BTSList(list)
        val bytes = ProtoBuf.dump(BTSList.serializer(), btsList)
        outFile.writeBytes(bytes)
    }
}