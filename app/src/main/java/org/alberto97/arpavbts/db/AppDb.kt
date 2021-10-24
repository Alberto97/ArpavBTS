package org.alberto97.arpavbts.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Bts::class],
    views = [OperatorView::class],
    version = 3,
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
    abstract fun operatorDao(): OperatorDao

    @DeleteColumn(tableName = "bts", columnName = "pontiRadio")
    class PontiRadioMigration : AutoMigrationSpec
}

object AppDbMigrations {
    val MIGRATION2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE VIEW `operatorView` AS SELECT gestore as name, count(*) as towers FROM bts GROUP BY gestore")
        }
    }
}
