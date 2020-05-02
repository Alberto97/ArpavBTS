package org.alberto97.arpavbts.ui

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import org.alberto97.arpavbts.R

class PreferenceHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_header
        isSelectable = false
    }
}