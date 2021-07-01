package org.alberto97.arpavbts.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Bts::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun btsDao(): BtsDao
}