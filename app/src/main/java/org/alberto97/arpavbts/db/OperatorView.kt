package org.alberto97.arpavbts.db

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "operatorView",
    value ="SELECT gestore as name, count(*) as towers FROM bts GROUP BY gestore"
)
data class OperatorView(
    val name: String,
    val towers: Int
)