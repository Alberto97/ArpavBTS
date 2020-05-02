package org.alberto97.arpavbts.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bts")
class Bts(
    val idImpianto: Int,
    val codice: String,
    val nome: String,
    val gestore: String,
    val indirizzo: String,
    val comune: String,
    val provincia: String,
    var latitudine: Float,
    var longitudine: Float,
    val quotaSlm: Float,
    val postazione: String,
    val pontiRadio: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var deviceId: Long? = null
}