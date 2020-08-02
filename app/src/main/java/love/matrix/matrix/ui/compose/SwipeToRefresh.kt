/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package love.matrix.matrix.ui.compose

import androidx.animation.AnimatedFloat
import androidx.animation.AnimationBuilder
import androidx.animation.AnimationEndReason
import androidx.animation.TweenBuilder
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.animation.animatedFloat
import androidx.ui.core.Alignment
import androidx.ui.core.DensityAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.PassThroughLayout
import androidx.ui.foundation.Box
import androidx.ui.foundation.animation.AnchorsFlingConfig
import androidx.ui.foundation.animation.fling
import androidx.ui.foundation.gestures.DragDirection
import androidx.ui.foundation.gestures.draggable
import androidx.ui.layout.Stack
import androidx.ui.layout.offset
import androidx.ui.unit.dp

/**
 * 组件来源google示例项目<jetNews>
 * 1,下拉刷新组件
 * 2,开关组件
 */

private val SWIPE_DISTANCE_SIZE = 100.dp
private const val SWIPE_DOWN_OFFSET = 1.2f

@Composable
fun SwipeToRefreshLayout(//页面下拉刷新动画
    refreshingState: Boolean,//刷新状态
    onRefresh: () -> Unit,
    refreshIndicator: @Composable() () -> Unit,//Indicator 指示符
    content: @Composable() () -> Unit
) {
    val size = with(DensityAmbient.current) { SWIPE_DISTANCE_SIZE.value }
    // min is below negative to hide
    val min = -size
    val max = size * SWIPE_DOWN_OFFSET
    StateDraggable(
        state = refreshingState,
        onStateChange = { shouldRefresh -> if (shouldRefresh) onRefresh() },
        anchorsToState = listOf(min to false, max to true),
        animationBuilder = TweenBuilder(),
        dragDirection = DragDirection.Vertical,
        minValue = min,
        maxValue = max
    ) { dragPosition ->
        val dpOffset = with(DensityAmbient.current) {//密度 Density
            (dragPosition.value * 0.5).dp
        }
        //Stack 相对于其边缘放置子项的可组合对象。该组件对于绘制重叠的子项很有用。
        // 将始终按照在[Stack]主体中指定的顺序绘制子项。
        // 使用[StackScope.gravity]修饰符定义目标元素在[Stack]框中的位置。
        Stack {
            content()
            Box(Modifier.gravity(Alignment.TopCenter).offset(0.dp, dpOffset)) {//Alignment校准
                if (dragPosition.value != min) {
                    refreshIndicator()//Indicator 指示符
                }
            }
        }
    }
}

// Copied from ui/ui-material/src/main/java/androidx/ui/material/internal/StateDraggable.kt

/**
 * Higher-level component that allows dragging around anchored positions binded to different states
 *
 * Example might be a Switch which you can drag between two states (true or false).
 *允许在绑定到不同状态的锚定位置周围拖动的高级组件
 *示例可能是一个开关，您可以在两个状态（真或假）之间拖动。
 *
 * Additional features compared to regular [draggable] modifier:
 * 1. The AnimatedFloat hosted inside and its value will be in sync with call site state
 * 2. When the anchor is reached, [onStateChange] will be called with state mapped to this anchor
 * 3. When the anchor is reached and [onStateChange] with corresponding state is called, but
 * call site didn't update state to the reached one for some reason,
 * this component performs rollback to the previous (correct) state.
 * 4. When new [state] is provided, component will be animated to state's anchor
 8 与常规[可拖动]修饰符相比的其他功能：
 * 1.内部托管的AnimatedFloat及其值将与呼叫站点状态同步
 * 2.到达锚点后，将调用[onStateChange]，并将状态映射到该锚点
 * 3.到达锚点并调用具有相应状态的[onStateChange]时，但是呼叫站点由于某种原因未将状态更新为已到达状态，
 *   此组件执行回滚到先前（正确）状态。
 * 4.当提供新的[state]时，组件将被动画化为状态的锚点
 *
 * children of this composable will receive [AnimatedFloat] class from which
 * they can read current value when they need or manually animate.
 * 此composable的子级将接收[AnimatedFloat]类，他们可以在需要时或从中对其进行动画处理时从中读取当前值。
 *
 * @param T type with which state is represented
 * @param state current state to represent Float value with
 * @param onStateChange callback to update call site's state
 * @param anchorsToState pairs of anchors to states to map anchors to state and vise versa
 * @param animationBuilder animation which will be used for animations
 * @param dragDirection direction in which drag should be happening.
 * Either [DragDirection.Vertical] or [DragDirection.Horizontal]
 * @param minValue lower bound for draggable value in this component
 * @param maxValue upper bound for draggable value in this component
 * @param enabled whether or not this Draggable is enabled and should consume events
 */
// TODO(malkov/tianliu) (figure our how to make it better and make public)
@Composable
internal fun <T> StateDraggable(//开关
    state: T,
    onStateChange: (T) -> Unit,
    anchorsToState: List<Pair<Float, T>>,
    animationBuilder: AnimationBuilder<Float>,
    dragDirection: DragDirection,
    enabled: Boolean = true,
    minValue: Float = Float.MIN_VALUE,
    maxValue: Float = Float.MAX_VALUE,
    content: @Composable() (AnimatedFloat) -> Unit
) {
    val forceAnimationCheck = state { true }

    val anchors = remember(anchorsToState) { anchorsToState.map { it.first } }
    val currentValue = anchorsToState.firstOrNull { it.second == state }!!.first
    val flingConfig =
        AnchorsFlingConfig(anchors, animationBuilder, onAnimationEnd = { reason, finalValue, _ ->
            if (reason != AnimationEndReason.Interrupted) {
                val newState = anchorsToState.firstOrNull { it.first == finalValue }?.second
                if (newState != null && newState != state) {
                    onStateChange(newState)
                    forceAnimationCheck.value = !forceAnimationCheck.value
                }
            }
        })
    val position = animatedFloat(currentValue)
    position.setBounds(minValue, maxValue)

    // This state is to force this component to be recomposed and trigger onCommit below
    // This is needed to stay in sync with drag state that caller side holds
    //此状态将强制重组此组件并在下面触发onCommit
    //需要与呼叫方保持的拖动状态保持同步
    onCommit(currentValue, forceAnimationCheck.value) {
        position.animateTo(currentValue, animationBuilder)
    }
    val draggable = Modifier.draggable(
        dragDirection = dragDirection,
        onDragDeltaConsumptionRequested = { delta ->
            val old = position.value
            position.snapTo(position.value + delta)
            position.value - old
        },
        onDragStopped = { position.fling(flingConfig, it) },
        enabled = enabled,
        startDragImmediately = position.isRunning
    )
    // TODO(b/150706555): This layout is temporary and should be removed once Semantics
    //  is implemented with modifiers.
    // 此布局是临时的，一旦使用修饰符实现了语义，就应将其删除。
    @Suppress("DEPRECATION")
    PassThroughLayout(draggable) {
        content(position)
    }
}
