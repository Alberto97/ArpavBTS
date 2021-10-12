package org.alberto97.arpavbts.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [Bts::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = AppDatabase.PontiRadioMigration::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun btsDao(): BtsDao

    @DeleteColumn(tableName = "bts", columnName = "pontiRadio")
    class PontiRadioMigration : AutoMigrationSpec
}