package org.alberto97.arpavbts.db

import androidx.lifecycle.LiveData
import org.alberto97.arpavbts.tools.ArpavApi

interface IBtsRepository {
    suspend fun saveBts(bts: List<Bts>)
    suspend fun clear()
    fun getBts(gestore: String? = null): LiveData<List<Bts>>
    suspend fun updateBts()
}

class BtsRepository(private val dao: BtsDao, private val arpavApi: ArpavApi) : IBtsRepository {

    override suspend fun saveBts(bts: List<Bts>) {
        dao.insert(bts)
    }

    override suspend fun clear() {
        dao.deleteAll()
    }

    override fun getBts(gestore: String?): LiveData<List<Bts>> {
        return if (gestore != null) {
            dao.getBtsByGestore(gestore)
        } else {
            dao.getBts()
        }
    }

    override suspend fun updateBts() {
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
                Bts(
                    idImpianto, codice, nome, gestore, address, comune, provincia,
                    latitudine, longitudine, quotaSlm, postazione, pontiRadio
                )
            }
        }

        // Clear DB
        clear()

        // Populate DB
        saveBts(list)
    }
}
