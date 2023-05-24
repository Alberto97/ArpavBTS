package org.alberto97.arpavbts.tools

import android.content.Context
import android.graphics.Color
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.models.GestoreConfigModel
import org.alberto97.arpavbts.tools.Extensions.readRawTextFile
import javax.inject.Inject
import javax.inject.Singleton


interface IOperatorConfig {
    fun getColor(gestore: String): Int
    fun getName(gestore: String): String
    fun getPreferred(): List<GestoreConfigModel>
}

@Singleton
class OperatorConfig @Inject constructor(@ApplicationContext val context: Context) : IOperatorConfig {

    private val gestoreList: List<GestoreConfigModel>
    private val gestoreMap: Map<String, GestoreConfigModel>

    init {
        val json = context.resources.readRawTextFile(R.raw.gestori_config)
        gestoreList = Json.decodeFromString(json)
        gestoreMap = gestoreList.associateBy { gestore -> gestore.rawName }
    }

    override fun getColor(gestore: String): Int {
        val data = gestoreMap[gestore] ?: return Color.parseColor("#64dd17")
        return data.color
    }

    override fun getName(gestore: String): String {
        val data = gestoreMap[gestore] ?: return gestore
        return data.label
    }

    override fun getPreferred(): List<GestoreConfigModel> {
        return gestoreList.filter { e -> e.preferred }
    }
}