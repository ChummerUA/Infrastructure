package com.chummer.compose_utils.utils

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

val LazyListLayoutInfo.itemsAfterStart
    get() = visibleItemsInfo.last().index + 1

val LazyListLayoutInfo.itemsBeforeEnd
    get() = totalItemsCount - (visibleItemsInfo.last().index + 1)

sealed interface PagingDirection {
    data object Forward : PagingDirection

    data object Backward : PagingDirection
}

@Composable
fun ConfigurePaging(
    itemsOffset: Int,
    listState: LazyListState,
    updatePages: OnPageLoad
) {
    val isScrollingUp by listState.isScrollingUpState()
    val isScrollingDown by listState.isScrollingDownState()

    val shouldLoad by remember {
        derivedStateOf {
            with(listState.layoutInfo) {
                val isCloseToEnd = totalItemsCount > 0 && itemsBeforeEnd <= itemsOffset
                val isCloseToStart = totalItemsCount > 0 && itemsAfterStart <= itemsOffset
                val new = isScrollingDown && isCloseToEnd
                val old = isScrollingUp && isCloseToStart
                new to old
            }
        }
    }
    val (shouldLoadNewPage, shouldLoadOldPage) = shouldLoad

    LaunchedEffect(shouldLoadNewPage, shouldLoadOldPage) {
        val direction = if (shouldLoadNewPage) PagingDirection.Forward
        else if (shouldLoadOldPage) PagingDirection.Backward
        else null

        direction?.let(updatePages)
    }
}

typealias OnPageLoad = ((PagingDirection) -> Unit)
