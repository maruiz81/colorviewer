package com.maruiz.colorviewer.ui.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.maruiz.colorviewer.R

@Composable
fun HexadecimalView() {
    var hexValue by remember { mutableStateOf("#") }
    val colorRendered by animateColorAsState(
        targetValue = hexValue.toColor(Color.Transparent),
        label = "color-picker-animation",
        finishedListener = {}
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp)
    ) {
        val textStyle =
            TextStyle(fontSize = 44.sp.nonScaledSp, color = MaterialTheme.colorScheme.onSurface)
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(24.dp))
                .background(colorRendered)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 24.dp)
        ) {
            val horizontalPadding = 24.dp
            Text(
                text = hexValue,
                style = textStyle,
                modifier = Modifier
                    .width(
                        measureTextWidth(
                            text = "#AAAAAAA",
                            style = textStyle
                        ) + horizontalPadding * 2
                    )
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(24.dp))
                    .padding(vertical = 12.dp, horizontal = horizontalPadding)
            )

            val haptic = LocalHapticFeedback.current

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .clickable(
                        onClick = {
                            hexValue =
                                if (hexValue.length > 1) hexValue.dropLast(1) else hexValue
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        indication = rememberRipple(bounded = true, radius = 40.dp),
                        interactionSource = remember { MutableInteractionSource() }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backspace), null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonKey("C", onclick = { hexValue = hexValue.addHexValueWithLimit("C") })
            ButtonKey("D", onclick = { hexValue = hexValue.addHexValueWithLimit("D") })
            ButtonKey("E", onclick = { hexValue = hexValue.addHexValueWithLimit("E") })
            ButtonKey("F", onclick = { hexValue = hexValue.addHexValueWithLimit("F") })
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonKey("8", onclick = { hexValue = hexValue.addHexValueWithLimit("8") })
            ButtonKey("9", onclick = { hexValue = hexValue.addHexValueWithLimit("9") })
            ButtonKey("A", onclick = { hexValue = hexValue.addHexValueWithLimit("A") })
            ButtonKey("B", onclick = { hexValue = hexValue.addHexValueWithLimit("B") })
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonKey("4", onclick = { hexValue = hexValue.addHexValueWithLimit("4") })
            ButtonKey("5", onclick = { hexValue = hexValue.addHexValueWithLimit("5") })
            ButtonKey("6", onclick = { hexValue = hexValue.addHexValueWithLimit("6") })
            ButtonKey("7", onclick = { hexValue = hexValue.addHexValueWithLimit("7") })
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonKey("0", onclick = { hexValue = hexValue.addHexValueWithLimit("0") })
            ButtonKey("1", onclick = { hexValue = hexValue.addHexValueWithLimit("1") })
            ButtonKey("2", onclick = { hexValue = hexValue.addHexValueWithLimit("2") })
            ButtonKey("3", onclick = { hexValue = hexValue.addHexValueWithLimit("3") })
        }
    }
}

fun String.toColorOrNull(): Color? = try {
    Color(toColorInt())
} catch (_: IllegalArgumentException) {
    null
}

fun String.toColor(fallback: Color) = toColorOrNull() ?: fallback

@Composable
fun RowScope.ButtonKey(text: String, onclick: () -> Unit = {}) {
    TextButton(onClick = onclick, modifier = Modifier.weight(1f)) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 32.sp.nonScaledSp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private fun String.addHexValueWithLimit(addValue: String): String =
    this.takeIf { it.length < 7 }
        ?.let { it + addValue }
        ?: this

val TextUnit.nonScaledSp
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp

@Composable
fun measureTextWidth(text: String, style: TextStyle = TextStyle.Default): Dp {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text, style).size.width
    return with(LocalDensity.current) { widthInPixels.toDp() }
}

@Composable
@Preview(showBackground = true)
fun HexadecimalViewPreview() {
    HexadecimalView()
}