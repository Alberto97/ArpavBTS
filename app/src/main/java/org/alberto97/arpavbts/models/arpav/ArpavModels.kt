package org.alberto97.arpavbts.models.arpav

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Suppress("unused")
@Serializable
class FeatureCollection(
    val type: String,
    val totalFeatures: Int,
    val features: List<Feature>
)

@Suppress("unused")
@Serializable
class Feature(
    val type: String,
    val id: String,
    val geometry: FeatureGeometry,
    val properties: FeatureProperties
)

@Suppress("unused")
@Serializable
class FeatureGeometry(val type: String, val coordinates: List<List<Float>>)

@Serializable
class FeatureProperties(
    @SerialName("id_impianto")
    val idImpianto: Int,
    val codice: String,
    val nome: String,
    val gestore: String,
    val indirizzo: String?,
    val comune: String,
    val provincia: String,
    @SerialName("quota_slm")
    val quotaSlm: Float,
    val postazione: String
)