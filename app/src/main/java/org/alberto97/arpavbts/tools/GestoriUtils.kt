package org.alberto97.arpavbts.tools

import android.content.Context
import android.graphics.Color
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.models.GestoreConfigModel
import org.alberto97.arpavbts.models.GestoriConfigModel
import org.alberto97.arpavbts.tools.Extensions.readRawTextFile
import org.alberto97.arpavbts.tools.moshi.AndroidColorAdapter
import javax.inject.Inject
import javax.inject.Singleton


interface IGestoriUtils {
    fun getColor(gestore: String): Int
    fun getPreferred(): List<GestoreConfigModel>
}

@Singleton
class GestoriUtils @Inject constructor(@ApplicationContext val context: Context) : IGestoriUtils {

    private val gestoreList: List<GestoreConfigModel>
    private val gestoreMap: Map<String, GestoreConfigModel>

    init {
        val json = context.resources.readRawTextFile(R.raw.gestori_config)

        val moshi = Moshi.Builder()
            .add(AndroidColorAdapter())
            .build()
        val jsonAdapter = moshi.adapter(GestoriConfigModel::class.java)
        val jsonData = jsonAdapter.fromJson(json) ?: throw Exception("Cannot deserialize config.json")

        gestoreList = jsonData.gestori
        gestoreMap = gestoreList.map { gestore -> gestore.rawName to gestore }.toMap()
    }

    override fun getColor(gestore: String): Int {
        val data = gestoreMap[gestore] ?: return Color.parseColor("#64dd17")
        return data.color
    }

    override fun getPreferred(): List<GestoreConfigModel> {
        return gestoreList.filter { e -> e.preferred }
    }
}