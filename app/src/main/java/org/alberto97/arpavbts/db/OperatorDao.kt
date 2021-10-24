package org.alberto97.arpavbts.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OperatorDao {
    @Query("SELECT * FROM operatorView ORDER BY towers DESC")
    fun getAll(): Flow<List<OperatorView>>

    @Query("SELECT * FROM operatorView WHERE name LIKE :value || '%' ORDER BY towers DESC")
    fun get(value: String): Flow<List<OperatorView>>
}
