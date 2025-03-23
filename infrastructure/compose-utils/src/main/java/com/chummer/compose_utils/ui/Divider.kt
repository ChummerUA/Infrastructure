package com.chummer.compose_utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Divider(
    paddingValues: PaddingValues,
    color: Color,
    height: Dp = 1.dp
) = Row(
    Modifier
        .fillMaxWidth(1f)
        .padding(paddingValues)
        .height(height)
        .background(color)
) { }
