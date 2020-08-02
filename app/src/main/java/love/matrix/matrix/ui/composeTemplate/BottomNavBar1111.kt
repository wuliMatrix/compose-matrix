package love.matrix.matrix.ui.composeTemplate

import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.*
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.foundation.Icon
import androidx.ui.graphics.Color
import androidx.ui.graphics.toArgb
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.*
import kotlin.math.absoluteValue


/**
 * 2020/7/14
 *
 * 由于compose-router提升到根视图开始使用（setContent{....}），BottomNavBar的实现方式
 * 也跟着变成compose-router的实现方式了，所以这个BottomNavBar+1111表示不再使用了。
 * 为何使用这种方式，将会在这里做比较详细的说明。
 *
 *
 * 感觉累了，不想大写特写了。
 *
 * 经过多次的整理后，发现这种做法最大的贡献就是在PlayVideo时右滑时，不会再出现侧边抽屉了
 * 其中实现的基本原理还是从根视图上让它们一分为二。PlayVideo和Video成为两个独立的视图后，
 * PlayVideo没设置有侧边抽屉，右滑时，当然，就不会再出现侧边抽屉了。
 *
 * 功能实现后，总是猛然发现事情是如此的简单，不值一提。
 *
 *
 *
 */


/**
 * 这个组件使用了环境变量【appStateAmbient】传值，使代码简洁了很多
 * 不使用环境变量【appStateAmbient】传值的话，appState就需要放在BottomNavBar(appState)里，
 * 由于BottomNavBar出现在很多地方（ Matrix,hop,Video,Chat,Mine），会造成appState出现在很多地方
 * 如何使用环境变量呢？这里提供一种方法，以供参考，本软件使用环境变量时就尽可能向这方法靠拢，以减少代码的复杂性
 * 1，分别定义变量值、状态类、枚举类
 *   1).  val XxxAmbient: ProvidableAmbient<XxxStatus> =
 *        ambientOf{ throw IllegalStateException("AppState is not initialized") }
 *   2). class XxxStatus {
 *        var currentXxxScreen by mutableStateOf(XxxScreen.AAAA)
 *       }
 *   3). enum class XxxScreen {aaa,bbb,ccc,ddd,,,}
 * 2,环境值初始化
 *   val appState = AppStatus()
 *   Providers(
 *       XxxAmbient provides XxxStatus
 *   ) {
 *     ...
 *   }
 * 3,在【@composable】中使用环境值
 *   @Composable
 *   fun BottomNavBar() {
 *      val XxxStatus = xxxAmbient.current
 *      ...
 *  }
 *
 */





/**
@Composable
fun BottomNavBar1111() {

    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)
    val appState = appStateAmbient.current//使用Ambient注入环境变量
   // val playState = playStateAmbient.current

    val aa: List<MenuItem> = if (appState.currentScreen == Screen.Video) {
            activity.window.statusBarColor = Color.Transparent.toArgb()
            items1//透明模式
    } else {
        activity.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
        items2//常规模式
    }

    BottomNavigation() {
        aa.forEachIndexed { index, it ->
            BottomNavigationItem(
                icon = { Icon(it.icon) },
                //selected = selectedIndex.value == index,
                selected = appState.currentScreen.ordinal == index,//由xxx.value变为xxx.ordinal
                onSelected = {
                    appState.currentScreen = it.screen
                }
            )
        }
    }
}

val appStateAmbient: ProvidableAmbient<AppStatus> =
    ambientOf { throw IllegalStateException("AppState is not initialized") }

class AppStatus {
    var currentScreen by mutableStateOf(Screen.Video)
}

enum class Screen {
    Matrix,
    Shop,
    Video,
    Chat,
    Mine,
    PlayVideo
}

data class MenuItem(val name: String, val icon: VectorAsset, val screen: Screen)

val items1 = listOf( //常规模式
    MenuItem(
        name = "Home",
        icon = Icons.Default.Home,
        screen = Screen.Matrix
    ),
    MenuItem(
        "Songs",
        Icons.Default.ShoppingBasket,
        Screen.Shop
    ),
    MenuItem(
        "Video",
        Icons.Default.Airplay,
        Screen.PlayVideo
    ),
    MenuItem(
        "Chat",
        Icons.Default.Chat,
        Screen.Chat
    ),
    MenuItem(
        "Mine",
        Icons.Default.Face,
        Screen.Mine
    )
)

val items2 = listOf(//透明模式
    MenuItem(
        name = "Home",
        icon = Icons.Default.Home,
        screen = Screen.Matrix
    ),
    MenuItem(
        "Songs",
        Icons.Default.ShoppingBasket,
        Screen.Shop
    ),
    MenuItem(
        "Video",
        Icons.Default.VideoCall,
        Screen.Video
    ),
    MenuItem(
        "Chat",
        Icons.Default.Chat,
        Screen.Chat
    ),
    MenuItem(
        "Mine",
        Icons.Default.Face,
        Screen.Mine
    )
)
*/
