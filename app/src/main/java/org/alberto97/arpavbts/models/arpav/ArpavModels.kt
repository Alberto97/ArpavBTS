package org.alberto97.arpavbts.models.arpav

import com.google.gson.annotations.SerializedName

class FeatureCollection(
    val type: String,
    val totalFeatures: Int,
    val features: List<Feature>
)

class Feature(
    val type: String,
    val id: String,
    val geometry: FeatureGeometry,
    val properties: FeatureProperties
)

class FeatureGeometry(val type: String, val coordinates: List<List<Float>>)

class FeatureProperties(
    @SerializedName("id_impianto")
    val idImpianto: Int,
    val codice: String,
    val nome: String,
    val gestore: String,
    val indirizzo: String?,
    val comune: String,
    val provincia: String,
    @SerializedName("quota_slm")
    val quotaSlm: Float,
    val postazione: String,
    @SerializedName("ponti_radio")
    val pontiRadio: String
)