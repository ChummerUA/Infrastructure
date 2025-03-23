package com.chummer.compose_utils.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LazyListState.isScrollingUpState(): State<Boolean> {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    var height by remember(this) { mutableIntStateOf(layoutInfo.viewportSize.height) }

    return remember {
        derivedStateOf {
            val heightChanged = height != layoutInfo.viewportSize.height
            val offsetChanged = when {
                previousIndex != firstVisibleItemIndex -> previousIndex > firstVisibleItemIndex
                else -> previousScrollOffset > firstVisibleItemScrollOffset
            }
            (offsetChanged && !heightChanged).also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
                height = layoutInfo.viewportSize.height
            }
        }
    }
}

@Composable
fun LazyListState.isScrollingDownState(): State<Boolean> {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    var height by remember(this) { mutableIntStateOf(layoutInfo.viewportSize.height) }

    return remember {
        derivedStateOf {
            val heightChanged = height != layoutInfo.viewportSize.height
            val offsetChanged = when {
                previousIndex != firstVisibleItemIndex -> previousIndex < firstVisibleItemIndex
                else -> previousScrollOffset < firstVisibleItemScrollOffset
            }
            (offsetChanged && !heightChanged).also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
                height = layoutInfo.viewportSize.height
            }
        }
    }
}
