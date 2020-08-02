package love.matrix.matrix.ui.composeTemplate


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.animation.AnimationEndReason
import androidx.compose.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.ui.core.*
import androidx.ui.unit.PxBounds
import androidx.ui.unit.height
import androidx.ui.unit.width
import love.matrix.matrix.ui.compose.ViewPagerScope
import java.time.temporal.TemporalAdjusters.next
import kotlin.math.sign

/**
 * 2020/7/9
 *
 * 这个组件实现了从【@Composable】转场到【activity】的功能
 *
 * 不过，从【@Composable】转场到【Activity】基本发生在点击事件里，
 * 而这个【@Composable】组件放到onClick={}里使用是会报错的
 *
 * 目前没有好的解决办法，在onClick={}使用的时候需要将组件拆分成两部分，分别放到【@Composable】和onClick={}里
 *
 * 想了很久没找到解决的办法，就先这样用着吧，也许用着用着官方就有相应的功能了
 *
 * 本人觉得，优雅的解决方式应该是这样的：
 *
 * onClick = { ComposeToActivity(XxxActivity::class.java) }
 *
 */


@Composable
inline fun <reified T> ComposeToActivity(java: Class<T>) {

    //----------------------第一部分，复制到composable里------------------------
    val rootView = (ViewAmbient.current as AndroidOwner).view
    val context = rootView.context
    val intent = Intent(context, java)//java 要转场到的Activity,格式：XxxActivity::class.java
    val globalBounds = remember { Ref<PxBounds>() }
    val options: Bundle? = globalBounds.value?.let {
        ActivityOptionsCompat.makeScaleUpAnimation(//转场动画
            rootView,
            it.left.toInt(),
            it.top.toInt(),
            it.width.toInt(),
            it.height.toInt()
        ).toBundle()
    }

    //---------------------第二部分，复制到onClick={}里 ------------------------
    startActivity(context, intent, options)
}

