package org.alberto97.arpavbts.models.arpav

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@Suppress("unused")
@JsonClass(generateAdapter = true)
class FeatureCollection(
    val type: String,
    val totalFeatures: Int,
    val features: List<Feature>
)

@Suppress("unused")
@JsonClass(generateAdapter = true)
class Feature(
    val type: String,
    val id: String,
    val geometry: FeatureGeometry,
    val properties: FeatureProperties
)

@Suppress("unused")
@JsonClass(generateAdapter = true)
class FeatureGeometry(val type: String, val coordinates: List<List<Float>>)

@JsonClass(generateAdapter = true)
class FeatureProperties(
    @Json(name = "id_impianto")
    val idImpianto: Int,
    val codice: String,
    val nome: String,
    val gestore: String,
    val indirizzo: String?,
    val comune: String,
    val provincia: String,
    @Json(name = "quota_slm")
    val quotaSlm: Float,
    val postazione: String,
    @Json(name = "ponti_radio")
    val pontiRadio: String
)