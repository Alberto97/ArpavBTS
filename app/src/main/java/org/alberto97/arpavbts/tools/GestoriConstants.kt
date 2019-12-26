package org.alberto97.arpavbts.tools

const val iliad = "iliad_id"
const val tim = "tim_id"
const val vodafone = "vodafone_id"
const val windTre = "windtre_id"
const val all = "all_id"

private const val timName = "TELECOM"
private const val vodafoneName = "VODAFONE"
private const val windTreName = "Wind Tre SpA"
private const val iliadName = "ILIAD ITALIA S.p.A."

val carrierNameById = mapOf(
    Pair(tim, timName),
    Pair(vodafone, vodafoneName),
    Pair(windTre, windTreName),
    Pair(iliad, iliadName)
)

val carrierIdByName = mapOf(
    Pair(timName, tim),
    Pair(vodafoneName, vodafone),
    Pair(windTreName, windTre),
    Pair(iliadName, iliad)
)

const val timColor ="#29B6F6"
const val vodafoneColor ="#ff3434"
const val windTreColor ="#ffa000"
const val iliadColor ="#d32f2f"

const val otherColor = "#64dd17"
const val allColor = "#EEEEEE"

val carrierColor = mapOf(
    Pair(tim, timColor),
    Pair(vodafone, vodafoneColor),
    Pair(windTre, windTreColor),
    Pair(iliad, iliadColor)
)
