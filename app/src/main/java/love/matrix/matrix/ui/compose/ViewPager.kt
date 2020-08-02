package love.matrix.matrix.ui.compose


import androidx.animation.AnimatedFloat
import androidx.animation.AnimationEndReason
import androidx.animation.PhysicsBuilder
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.animation.animatedFloat
import androidx.ui.core.*
import androidx.ui.foundation.Box
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.animation.AnchorsFlingConfig
import androidx.ui.foundation.animation.fling
import androidx.ui.foundation.gestures.DragDirection
import androidx.ui.foundation.gestures.draggable
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sign

/**
 * 2020/7/9
 *
 * 组件来源  https://github.com/vanpra/ComposeViewPager
 *
 * 自己做了一些改动，使它可以上下，左右滑动
 */


interface ViewPagerScope {
    val index: Int
    fun next()
    fun previous()//上一个
}

data class ViewPagerImpl(override val index: Int, val increment: (Int) -> Unit) :
    ViewPagerScope {
    override fun next() {
        increment(1)//increment 增量
    }

    override fun previous() {
        increment(-1)
    }
}

data class PageState(
    val alpha: Float = 1f,
    val scaleX: Float = 1f, //比例因子
    val scaleY: Float = 1f,
    val translationX: Float = 0f,//相对父容器左上角的偏移量
    val translationY: Float = 0f
)

private const val MIN_SCALE = 0.75f

private const val MIN_SCALE_ZOOM = 0.9f
private const val MIN_ALPHA = 0.7f


interface ViewPagerTransition {//这部分是给页面切换加特效的，(暂时没适配上下滑动 2020/7/9)

    fun transformPage(constraints: Constraints, position: Float): PageState

    companion object {
        val NONE = object : ViewPagerTransition {
            override fun transformPage(constraints: Constraints, position: Float): PageState {
                return PageState()
            }
        }

        val DEPTH_TRANSFORM = object : ViewPagerTransition {
            override fun transformPage(constraints: Constraints, position: Float): PageState {
                return when {
                    position <= 0 -> PageState()
                    position <= 1 -> {
                        //scaleFactor 比例因子
                        //abs 返回给定值[x]的绝对值。
                        val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
                        PageState(
                            1 - position, scaleFactor, scaleFactor,
                            constraints.maxWidth * -position
                        )
                    }
                    else -> PageState(0f, 0f, 0f)
                }
            }
        }

        val ZOOM_OUT = object : ViewPagerTransition {
            override fun transformPage(constraints: Constraints, position: Float): PageState {
                return when {
                    position <= 1 && position >= -1 -> {

                        //coerceAtLeast 确保该值不小于指定的[minimumValue]。
                        //@如果大于或等于[minimumValue]，则返回此值。
                        val scaleFactor = MIN_SCALE_ZOOM.coerceAtLeast(1 - abs(position))
                        val vertMargin = constraints.maxHeight * (1 - scaleFactor) / 2
                        val horzMargin = constraints.maxWidth * (1 - scaleFactor) / 2
                        val translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        val alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE_ZOOM) / (1 - MIN_SCALE_ZOOM)) * (1 - MIN_ALPHA)))
                        PageState(
                            alpha,
                            scaleFactor,
                            scaleFactor,
                            translationX
                        )
                    }
                    else -> PageState(0f, 0f, 0f)
                }
            }
        }
    }
}

@Composable
fun ViewPagerHorizontalScroller( //左右滑动
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    range: IntRange? = null,
    startPage: Int = 0,
    enabled: Boolean = true,
    transition: ViewPagerTransition = ViewPagerTransition.NONE,
    screenItem: @Composable() ViewPagerScope.() -> Unit
) {

    if (range != null && !range.contains(startPage)) {
        //The start page supplied was not in the given range 提供的起始页不在给定范围内
        throw IllegalArgumentException("The start page supplied was not in the given range")
    }

    Box(backgroundColor = Color.Transparent) {
        WithConstraints {
            val index = state { startPage }
            val width = constraints.maxWidth.toFloat()

            // animationValue效果创建一个[AnimatedFloat]并在位置上对其进行记忆。
            // 当[AnimatedFloat]对象的值更新时，依赖于该值的组件将自动重新组成。
            // @param initVal将[AnimatedFloat]设置为的初始值。
            val offset = animatedFloat(width)

            if (range == null) {

                //  setBounds  设置动画应限制的范围。动画到达边界时，即使有剩余速度，它也会立即停止。
                // 设置范围将立即将当前值钳制到新范围。
                // 因此，建议不要以在动画过程中立即更改当前值的方式更改边界，因为这会导致动画不连续。
                // 第一个参数为 动画值的下限
                // 第二个参数为 动画值的上限
                offset.setBounds(0f, 2 * width)
            } else {
                when (index.value) {
                    //第一页
                    range.first -> offset.setBounds(width, 2 * width)
                    //最后一页
                    range.last -> offset.setBounds(0f, width)
                    else -> offset.setBounds(0f, 2 * width)
                }
            }
            //anchors 锚点
            val anchors = listOf(0f, width, 2 * width)

            //使用锚点创建fling配置将确保拖动结束后，该值将被动画化到预定义列表中的一个点。
            // 它考虑了速度，尽管考虑到速度，值将被动画化到所提供列表中最接近的点。
            // anchors 一组要制作动画的锚点
            // animationBuilder 用于动画的动画
            // onAnimationEnd 当动画值达到所需的锚点或挥动被手势输入中断时将调用的回调。
            //flingConfig 文件配置
            val flingConfig = AnchorsFlingConfig(anchors,

                //PhysicsBuilder 将弹簧的配置作为其构造函数参数。
                //dampingRatio 弹簧的阻尼比。
                //stiffness 刚度
                animationBuilder = PhysicsBuilder(dampingRatio = 0.8f, stiffness = 1000f),
                onAnimationEnd = { reason, end, _ ->

                    //确保此值在指定的范围内[minimumValue] .. [maximumValue]。
                    offset.snapTo(width)

                    if (reason != AnimationEndReason.Interrupted) {//动画被其他动画打断了
                        if (end == width * 2) {
                            index.value += 1
                            onNext()
                        } else if (end == 0f) {
                            index.value -= 1
                            onPrevious()
                        }
                    }
                })
            //increment 增量
            val increment = { increment: Int ->

                //animateTo 设置目标值，有效地启动动画以将值从[value]更改为目标值。
                // 如果已经有动画在播放，则此方法将中断正在进行的动画，
                // 调用与该动画关联的[onEnd]，并从当前值到新的目标值开始一个新的动画。
                // targetValue 赋予动画的新值
                // onEnd 一个可选的回调，当动画由于任何原因结束时将被调用。
                offset.animateTo(
                    //sign  返回给定值[x]的符号：
                    //      *-如果值为负，则为`-1.0`
                    //      *-如果值为零，则为零，
                    //      *-如果值为正，则为“ 1.0”
                    targetValue = width * sign(increment.toDouble()).toFloat() + width,
                    onEnd = { animationEndReason, _ ->
                        if (animationEndReason != AnimationEndReason.Interrupted) {
                            index.value += increment

                            offset.snapTo(width)//重来
                        }
                    })
            }

            //draggable 可拖动的
            // 在单个[DragDirection]中为UI元素配置触摸拖动。
            // 拖动距离以单个[Float]值（以像素为单位）报告给[onDragDeltaConsumptionRequested]。
            // 此组件的常见用例是当您需要能够在屏幕上拖动组件内部的某些内容并通过一个float值表示此状态时。
            // 您需要控制整个拖动流程，请考虑改用[dragGestureFilter]。
            // 如果要实现滚动/滚动行为，请考虑使用[scrollable]。
            // dragDirection  发生拖曳的方向
            // onDragDeltaConsumptionRequested  发生拖动时要调用的回调。
            // 用户必须在此lambda中更新其状态，并返回消耗的增量数量
            // onDragStopped  拖动停止时将调用的回调，提供速度
            // enabled  是否启用拖动
            val draggable = modifier.draggable(
                dragDirection = DragDirection.Horizontal,//Horizontal 横向
                onDragDeltaConsumptionRequested = {
                    val old = offset.value
                    offset.snapTo(offset.value - (it * 0.5f))
                    offset.value - old
                },
                //fling 配置fling动画，并指定开始速度。
                onDragStopped = { offset.fling(flingConfig, -(it * 0.6f)) },
                enabled = enabled
            )
            //isScrollable 参数来启用或禁用触摸输入滚动，默认为true,这里用true的话会有冲突，所以禁用
            HorizontalScroller(isScrollable = false) {

                // Stack 一个可组合对象，其子元素相对于其边缘定位。
                // 该组件对于绘制重叠的子项很有用。
                // 将始终按照在[堆栈]主体中指定的顺序绘制子级。
                // 使用[StackScope.gravity]修饰符定义目标元素在[Stack]框中的位置。

                //preferredWidth 声明内容的首选宽度为[width] dp。
                // 传入的度量值[约束]可能会覆盖此值，从而迫使内容变小或变大。

                //
                Stack(draggable.preferredWidth(maxWidth * 3).offset(-offset.toDp())) {
                    for (x in -1..1) {

                        val temp = 1f - ((offset.toDp() - maxWidth * x) / maxWidth)

                        val page = transition.transformPage(constraints, temp)

                        Column(
                            // 声明内容的首选大小为[width] dp x [height] dp。
                            // 传入的度量值[约束]可能会覆盖此值，从而迫使内容变小或变大。
                            // 有关设置内容大小而不管传入限制的修饰符，请参见[Modifier.size]。
                            // 请参阅[preferredWidth]或[preferredHeight]单独设置宽度或高度。
                            // 请参阅[preferredWidthIn]，[preferredHeightIn]
                            // 或[preferredSizeIn]设置首选尺寸范围。
                            Modifier.preferredSize(maxWidth, maxHeight)

                                .offset(
                                    x = maxWidth * (x + 1) + page.translationX.toDp(),
                                    y = page.translationY.toDp()
                                )
                        ) {
                            if ((offset.value < width && x == -1) || x == 0 || (offset.value > width && x == 1)) {
                                val viewPagerImpl =
                                    ViewPagerImpl(
                                        index.value + x,

                                        increment//启动动画
                                    )
                                Box(
                                    modifier = Modifier
                                        .weight(0.5f)
                                        // 允许内容以所需大小进行测量，而无需考虑传入的测量值[最小宽度]
                                        // [Constraints.minWidth]或[最小高度]
                                        // [Constraints.minHeight]约束。如果内容的测量大小小于最小大小约束，
                                        // 则[在最小尺寸的空间内对齐。
                                        .wrapContentSize(Alignment.Center)//Alignment 对齐
                                        .preferredSize(
                                            maxWidth * page.scaleX,
                                            maxHeight * page.scaleY
                                        )
                                        .drawOpacity(page.alpha)//绘制具有不透明度（alpha）小于1的内容。
                                ) {
                                    screenItem(viewPagerImpl)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ViewPagerVerticalScroller( //上下滑动
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    range: IntRange? = null,
    startPage: Int = 0,
    enabled: Boolean = true,
    transition: ViewPagerTransition = ViewPagerTransition.NONE,
    screenItem: @Composable() ViewPagerScope.() -> Unit
) {

    if (range != null && !range.contains(startPage)) {
        //The start page supplied was not in the given range 提供的起始页不在给定范围内
        throw IllegalArgumentException("The start page supplied was not in the given range")
    }

    Box(backgroundColor = Color.Transparent) {
        WithConstraints {
            val index = state { startPage }
            val height = constraints.maxHeight.toFloat()

            // animationValue效果创建一个[AnimatedFloat]并在位置上对其进行记忆。
            // 当[AnimatedFloat]对象的值更新时，依赖于该值的组件将自动重新组成。
            // @param initVal将[AnimatedFloat]设置为的初始值。
            val offset = animatedFloat(height)

            if (range == null) {

                //  setBounds  设置动画应限制的范围。动画到达边界时，即使有剩余速度，它也会立即停止。
                // 设置范围将立即将当前值钳制到新范围。
                // 因此，建议不要以在动画过程中立即更改当前值的方式更改边界，因为这会导致动画不连续。
                // 第一个参数为 动画值的下限
                // 第二个参数为 动画值的上限
                offset.setBounds(0f, 2 * height)
            } else {
                when (index.value) {
                    //第一页
                    range.first -> offset.setBounds(height, 2 * height)
                    //最后一页
                    range.last -> offset.setBounds(0f, height)
                    else -> offset.setBounds(0f, 2 * height)
                }
            }
            //anchors 锚点
            val anchors = listOf(0f, height, 2 * height)

            //使用锚点创建fling配置将确保拖动结束后，该值将被动画化到预定义列表中的一个点。
            // 它考虑了速度，尽管考虑到速度，值将被动画化到所提供列表中最接近的点。
            // anchors 一组要制作动画的锚点
            // animationBuilder 用于动画的动画
            // onAnimationEnd 当动画值达到所需的锚点或挥动被手势输入中断时将调用的回调。
            //flingConfig 文件配置
            val flingConfig = AnchorsFlingConfig(anchors,

                //PhysicsBuilder 将弹簧的配置作为其构造函数参数。
                //dampingRatio 弹簧的阻尼比。
                //stiffness 刚度
                animationBuilder = PhysicsBuilder(dampingRatio = 0.8f, stiffness = 1000f),
                onAnimationEnd = { reason, end, _ ->

                    //确保此值在指定的范围内[minimumValue] .. [maximumValue]。
                    offset.snapTo(height)

                    if (reason != AnimationEndReason.Interrupted) {//动画被其他动画打断了
                        if (end == height * 2) {
                            index.value += 1
                            onNext()
                        } else if (end == 0f) {
                            index.value -= 1
                            onPrevious()
                        }
                    }
                })
            //increment 增量
            val increment = { increment: Int ->

                //animateTo 设置目标值，有效地启动动画以将值从[value]更改为目标值。
                // 如果已经有动画在播放，则此方法将中断正在进行的动画，
                // 调用与该动画关联的[onEnd]，并从当前值到新的目标值开始一个新的动画。
                // targetValue 赋予动画的新值
                // onEnd 一个可选的回调，当动画由于任何原因结束时将被调用。
                offset.animateTo(
                    //sign  返回给定值[x]的符号：
                    //      *-如果值为负，则为`-1.0`
                    //      *-如果值为零，则为零，
                    //      *-如果值为正，则为“ 1.0”
                    targetValue = height * sign(increment.toDouble()).toFloat() + height,
                    onEnd = { animationEndReason, _ ->
                        if (animationEndReason != AnimationEndReason.Interrupted) {
                            index.value += increment

                            offset.snapTo(height)//重来
                        }
                    })
            }

            //draggable 可拖动的
            // 在单个[DragDirection]中为UI元素配置触摸拖动。
            // 拖动距离以单个[Float]值（以像素为单位）报告给[onDragDeltaConsumptionRequested]。
            // 此组件的常见用例是当您需要能够在屏幕上拖动组件内部的某些内容并通过一个float值表示此状态时。
            // 您需要控制整个拖动流程，请考虑改用[dragGestureFilter]。
            // 如果要实现滚动/滚动行为，请考虑使用[scrollable]。
            // dragDirection  发生拖曳的方向
            // onDragDeltaConsumptionRequested  发生拖动时要调用的回调。
            // 用户必须在此lambda中更新其状态，并返回消耗的增量数量
            // onDragStopped  拖动停止时将调用的回调，提供速度
            // enabled  是否启用拖动
            val draggable = modifier.draggable(
                //dragDirection = DragDirection.Horizontal,//Horizontal 横向
                dragDirection = DragDirection.Vertical,
                onDragDeltaConsumptionRequested = {
                    val old = offset.value
                    offset.snapTo(offset.value - (it * 0.5f))
                    offset.value - old
                },
                //fling 配置fling动画，并指定开始速度。
                onDragStopped = { offset.fling(flingConfig, -(it * 0.6f)) },
                enabled = enabled
            )
            //isScrollable 参数来启用或禁用触摸输入滚动，默认为true,这里用true的话会有冲突，所以禁用
           // HorizontalScroller(isScrollable = false) {
              VerticalScroller(isScrollable = false) {

                // Stack 一个可组合对象，其子元素相对于其边缘定位。
                // 该组件对于绘制重叠的子项很有用。
                // 将始终按照在[堆栈]主体中指定的顺序绘制子级。
                // 使用[StackScope.gravity]修饰符定义目标元素在[Stack]框中的位置。

                //preferredWidth 声明内容的首选宽度为[width] dp。
                // 传入的度量值[约束]可能会覆盖此值，从而迫使内容变小或变大。


                //Stack(draggable.preferredWidth(maxWidth * 3).offset(-offset.toDp())) {
                  Stack(draggable.preferredHeight(maxHeight * 3).offset(0.dp,-offset.toDp())) {
                    for (y in -1..1) {
                       // val temp = 1f - ((offset.toDp() - maxWidth * x) / maxWidth)
                        val temp = 1f - ((offset.toDp() - maxHeight * y) / maxHeight)

                        val page = transition.transformPage(constraints, temp)

                        Column(
                            // 声明内容的首选大小为[width] dp x [height] dp。
                            // 传入的度量值[约束]可能会覆盖此值，从而迫使内容变小或变大。
                            // 有关设置内容大小而不管传入限制的修饰符，请参见[Modifier.size]。
                            // 请参阅[preferredWidth]或[preferredHeight]单独设置宽度或高度。
                            // 请参阅[preferredWidthIn]，[preferredHeightIn]
                            // 或[preferredSizeIn]设置首选尺寸范围。
                            Modifier.preferredSize(maxWidth, maxHeight)
                                /*.offset(
                                    x = maxWidth * (y + 1) + page.translationX.toDp(),
                                    y = page.translationY.toDp()
                                )*/
                                .offset(
                                    x = page.translationX.toDp(),
                                    y = maxHeight * (y + 1) + page.translationY.toDp()

                                )
                        ) {
                            if ((offset.value < height && y == -1) || y == 0 || (offset.value > height && y == 1)) {
                                val viewPagerImpl =
                                    ViewPagerImpl(
                                        index.value + y,

                                        increment//启动动画
                                    )
                                Box(
                                    modifier = Modifier
                                        .weight(0.5f)
                                        // 允许内容以所需大小进行测量，而无需考虑传入的测量值[最小宽度]
                                        // [Constraints.minWidth]或[最小高度]
                                        // [Constraints.minHeight]约束。如果内容的测量大小小于最小大小约束，
                                        // 则[在最小尺寸的空间内对齐。
                                        .wrapContentSize(Alignment.Center)//Alignment 对齐
                                        .preferredSize(
                                            maxWidth * page.scaleX,
                                            maxHeight * page.scaleY
                                        )
                                        .drawOpacity(page.alpha)//绘制具有不透明度（alpha）小于1的内容。
                                ) {
                                    screenItem(viewPagerImpl)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun AnimatedFloat.toDp(): Dp {
    return with(DensityAmbient.current) { this@toDp.value.toDp() }
}

@Composable
fun Float.toDp(): Dp {
    return with(DensityAmbient.current) { this@toDp.toDp() }
}