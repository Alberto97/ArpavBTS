package org.alberto97.arpavbts.db

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import org.alberto97.arpavbts.tools.ArpavApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

interface IBtsRepository {
    suspend fun saveBts(bts: List<Bts>)
    suspend fun clear()
    fun getBts(gestore: String? = null): Flow<List<Bts>>
    suspend fun updateBtsIfOldOrEmpty()
    suspend fun updateBts()
    fun getLastDbUpdate(): Long
}

object SharedPreferenceConstants {
    const val LAST_DB_UPDATE = "last_db_update"
}

@Singleton
class BtsRepository @Inject constructor(
    private val dao: BtsDao,
    private val arpavApi: ArpavApi,
    @ApplicationContext private val context: Context
) : IBtsRepository {

    override suspend fun saveBts(bts: List<Bts>) {
        dao.insert(bts)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        with (prefs.edit()) {
            putLong(SharedPreferenceConstants.LAST_DB_UPDATE, System.currentTimeMillis())
            commit()
        }
    }

    override suspend fun clear() = dao.deleteAll()

    override fun getBts(gestore: String?): Flow<List<Bts>> {
        return if (gestore != null) {
            dao.getBtsByGestore(gestore)
        } else {
            dao.getBts()
        }
    }

    override fun getLastDbUpdate(): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(SharedPreferenceConstants.LAST_DB_UPDATE, 0)
    }

    @ExperimentalTime
    override suspend fun updateBtsIfOldOrEmpty() {
        val isEmpty = dao.isEmpty()
        if (!isEmpty) {
            // Don't update until at least one day has passed since the last data fetch
            val lastUpdateMillis = getLastDbUpdate()
            val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
            if (duration.inWholeDays < 1) return
        }
        updateBts()
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
