package org.alberto97.arpavbts.models

import kotlinx.serialization.SerialId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BTSData (
    @SerialId(1)
    @SerialName("id_impianto")
    val idImpianto: Int,

    @SerialId(2)
    val codice: String,

    @SerialId(3)
    val nome: String,

    @SerialId(4)
    val gestore: String,

    @SerialId(5)
    val indirizzo: String,

    @SerialId(6)
    val comune: String,

    @SerialId(7)
    val provincia: String,

    @SerialId(8)
    var latitudine: Float,

    @SerialId(9)
    var longitudine: Float,

    @SerialId(10)
    @SerialName("quota_slm")
    val quotaSlm: Float,

    @SerialId(11)
    val postazione: String,

    @SerialId(12)
    @SerialName("ponti_radio")
    val pontiRadio: String
)