package com.pocs.presentation.view.component.bottomsheet

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * [ModalBottomSheetLayout]로 원하는 구현을 할 수 없어서 복사한 컴포즈 함수이다.
 *
 * 추가된 기능
 * - [ModalBottomSheetValue.Hidden]일 때는 아예 안보이도록 하여 링크의 문제를 해결함
 *   https://github.com/hansung-pocs/blog-android/pull/170#issuecomment-1215451403
 * -
 *
 * 사라진 기능
 * - [ModalBottomSheetState.confirmStateChange] 함수 작동 안함
 * - [ModalBottomSheetValue.HalfExpanded] 작동 안함
 * - Swipe 동작 안함
 */
@Composable
@ExperimentalMaterialApi
fun PocsModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    onDismiss: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    BoxWithConstraints(modifier) {
        val fullHeight = constraints.maxHeight.toFloat()
        val sheetHeightState = remember { mutableStateOf<Float?>(null) }
        val anchorInited = remember { mutableStateOf(false) }
        val sheetAlpha by animateFloatAsState(
            targetValue = if (sheetState.targetValue != ModalBottomSheetValue.Hidden) 1f else 0f,
            animationSpec = TweenSpec(durationMillis = 10)
        )
        LaunchedEffect(Unit) {
            // anchor가 internal이기 때문에 비어있는지 확인 할 수 없어 임시로 100ms 후에 초기화 된 것 처럼 보이게 한다.
            delay(100)
            anchorInited.value = true
        }
        Box(Modifier.fillMaxSize()) {
            content()
            Scrim(
                color = scrimColor,
                onDismiss = {
                    onDismiss?.invoke()
                    scope.launch { sheetState.hide() }
                },
                visible = sheetState.targetValue != ModalBottomSheetValue.Hidden
            )
        }
        Surface(
            Modifier
                .fillMaxWidth()
                .alpha(sheetAlpha)
                .offset {
                    val y = if (!anchorInited.value) {
                        // if we don't know our anchors yet, render the sheet as hidden
                        fullHeight.roundToInt()
                    } else {
                        // if we do know our anchors, respect them
                        sheetState.offset.value.roundToInt()
                    }
                    IntOffset(0, y)
                }
                .bottomSheetSwipeable(sheetState, fullHeight, sheetHeightState)
                .onGloballyPositioned {
                    sheetHeightState.value = it.size.height.toFloat()
                }
                .semantics {
                    if (sheetState.isVisible) {
                        dismiss {
                            scope.launch { sheetState.hide() }
                            true
                        }
                    }
                },
            shape = sheetShape,
            elevation = sheetElevation,
            color = sheetBackgroundColor,
            contentColor = sheetContentColor
        ) {
            Column(content = sheetContent)
        }
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissModifier = if (visible) {
            Modifier.pointerInput(Unit) { detectTapGestures { onDismiss() } }
        } else {
            Modifier
        }
        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

@Suppress("ModifierInspectorInfo")
@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.bottomSheetSwipeable(
    sheetState: ModalBottomSheetState,
    fullHeight: Float,
    sheetHeightState: State<Float?>
): Modifier {
    val sheetHeight = sheetHeightState.value
    val modifier = if (sheetHeight != null) {
        val anchors = if (sheetHeight < fullHeight / 2) {
            mapOf(
                fullHeight to ModalBottomSheetValue.Hidden,
                fullHeight - sheetHeight to ModalBottomSheetValue.Expanded
            )
        } else {
            mapOf(
                fullHeight to ModalBottomSheetValue.Hidden,
                fullHeight / 2 to ModalBottomSheetValue.HalfExpanded,
                max(0f, fullHeight - sheetHeight) to ModalBottomSheetValue.Expanded
            )
        }
        Modifier.swipeable(
            state = sheetState,
            anchors = anchors,
            orientation = Orientation.Vertical,
            enabled = false,
            resistance = null
        )
    } else {
        Modifier
    }

    return this.then(modifier)
}
