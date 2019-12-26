package org.alberto97.arpavbts.models

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class BTSList (

    @SerialId(1)
    val list: List<BTSData>
)