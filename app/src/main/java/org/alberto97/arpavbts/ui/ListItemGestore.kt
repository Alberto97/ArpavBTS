package org.alberto97.arpavbts.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.alberto97.arpavbts.ui.theme.AppTheme

@Composable
fun ListItemGestore(id: String?, text: String, tint: Color, onClick: (id: String?) -> Unit) {
    Row(
        Modifier.fillMaxWidth()
            .clickable(onClick = {
                onClick(id)
            })
            .padding(15.dp)
            .gravity(Alignment.CenterVertically)
    ) {
        Surface(
            color = tint,
            modifier = Modifier.size(30.dp).clip(CircleShape)
                .clickable(onClick = {
                    onClick(id)
                })
        ) {}
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            modifier = Modifier
                .gravity(Alignment.CenterVertically)
                .padding(start = 15.dp)
        )
    }
}

@Preview
@Composable
private fun ListItemPreview() {
    AppTheme(darkTheme = false) {
        Surface {
            ListItemGestore(
                id = "1",
                text = "Prova",
                tint = Color(0xff1faa00),
                onClick = {}
            )
        }
    }
}