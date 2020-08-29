package org.alberto97.arpavbts.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.*

const val SHEET_SELECTED_PROVIDER = "Provider"
class GestoreBottomSheetDialog : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val list = arrayListOf(
            getItem(getString(R.string.provider_all), null),
            getItem(getString(R.string.provider_tim), timName),
            getItem(getString(R.string.provider_vodafone), vodafoneName),
            getItem(getString(R.string.provider_windtre), windTreName),
            getItem(getString(R.string.provider_iliad), iliadName)
        )

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Column {
                        Text(
                            text = stringResource(R.string.provider_pick),
                            modifier = Modifier.padding(15.dp)
                        )

                        LazyColumnFor(list) {
                            ListItemGestore(
                                id = it.id,
                                text = it.name,
                                tint = Color(it.color),
                                onClick = { id -> onGestoreClick(id) }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onGestoreClick(id: String?) {
        val intent = Intent()
        intent.putExtra(SHEET_SELECTED_PROVIDER, id)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

        // Delay dismiss to allow Compose properly ending its job
        lifecycleScope.launch {
            delay(500L)
            super.dismiss()
        }
    }

    private fun getItem(name: String, id: String?): GestoreAdapterItem {
        val colorStr = carrierColor[id] ?: allColor
        val color = android.graphics.Color.parseColor(colorStr)

        return GestoreAdapterItem(color, name, id)
    }
}