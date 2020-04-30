package org.alberto97.arpavbts.db
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BtsDao {
    @Query("SELECT * FROM bts WHERE gestore = :gestore")
    fun getBtsByGestore(gestore: String): LiveData<List<Bts>>

    @Query("SELECT * FROM bts")
    fun getBts(): LiveData<List<Bts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bts: List<Bts>)

    @Query("DELETE FROM bts")
    suspend fun deleteAll()

    @Query("SELECT NOT EXISTS(SELECT 1 FROM bts LIMIT 1)")
    fun isEmpty(): Boolean

//    @Query("SELECT gestore, count(*) as towers FROM bts GROUP BY gestore ORDER BY towers DESC")
//    fun getGestori(): List<Pair<String, String>>

//    @Query("SELECT DISTINCT gestore FROM bts WHERE gestore LIKE :gestore || '%'")
//    fun searchGestore(gestore: String): List<String>
}
