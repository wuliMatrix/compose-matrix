package love.matrix.matrix.ui.composeTemplate

import androidx.activity.ComponentActivity
import androidx.compose.Composable
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
import androidx.ui.unit.dp
import love.matrix.matrix.ui.RootScreen


/**
 * 2020/7/14
 *
 * 这是软件打开就能看到的底部导航栏组件，目前它实现了三种显示模式，
 * 1，全屏模式 + PlayVideo中间按钮（透明状态栏、透明导航栏）
 * 2，普通模式 + PlayVideo中间按钮
 * 3，普通模式 + VideoList中间按钮
 *
 * 总的来说就是火山小视频和抖音短视频的合体：火山+导航栏; 抖音+列表视频
 *
 * 个组件和上个版本相比，做了很大的改动，详情请见 BottomNavBar1111.kt
 */


interface BottomNavBar {

    sealed class MenuItem {
        object Matrix : MenuItem()
        object Shop : MenuItem()
        object VideoList : MenuItem()
        object PlayVideo : MenuItem()
        object Chat : MenuItem()
        object Mine : MenuItem()
    }

    data class State(
        var currentSelection: MenuItem
    )

    companion object {
        var TagState: MenuItem =  MenuItem.VideoList//记住中间按钮是PlayVideo还是VideoList

        @Composable
        fun Content(state: State, onMenuItemClicked: (MenuItem) -> Unit) {

            val activity = (LifecycleOwnerAmbient.current as ComponentActivity)

            //通过全局变量【TagState】，记住中间按钮是PlayVideo还是VideoList
            if (state.currentSelection == MenuItem.PlayVideo) {
                TagState = MenuItem.PlayVideo
            }
            if (state.currentSelection == MenuItem.VideoList) {
                TagState = MenuItem.VideoList
            }

            //第一种情况：状态栏和导航栏都是透明的底色,中间按钮是PlayVideo
            if (state.currentSelection == MenuItem.PlayVideo) {
                activity.window.statusBarColor = Color.Transparent.toArgb()
                BottomNavigation(backgroundColor = Color.Transparent, elevation = 0.dp) {
                    items1.forEachIndexed { _, it ->
                        BottomNavigationItem(
                            icon = { Icon(it.icon) },
                            activeColor = Color.White,
                            selected = state.currentSelection == it.state,
                            onSelected = {
                                onMenuItemClicked(it.state)
                            }
                        )
                    }
                }
            } else {
                activity.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                //第二种情况：状态栏和导航栏是常规的底色，中间按钮是PlayVideo
                if (TagState == MenuItem.PlayVideo) {
                    BottomNavigation() {
                        items1.forEachIndexed { _, it ->
                            BottomNavigationItem(
                                icon = { Icon(it.icon) },
                                selected = state.currentSelection == it.state,
                                onSelected = {
                                    onMenuItemClicked(it.state)
                                }
                            )
                        }
                    }
                } else {//第三种情况：状态栏和导航栏是常规的底色，中间按钮是VideoList
                    BottomNavigation() {
                        items2.forEachIndexed { _, it ->
                            BottomNavigationItem(
                                icon = { Icon(it.icon) },
                                selected = state.currentSelection == it.state,
                                onSelected = {
                                    onMenuItemClicked(it.state)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class AAA(val name: String, val icon: VectorAsset, val state: BottomNavBar.MenuItem)

val items1 = listOf( //中间按钮是PlayVideo
    AAA(
        name = "Home",
        icon = Icons.Default.Home,
        state = BottomNavBar.MenuItem.Matrix
    ),
    AAA(
        "Songs",
        Icons.Default.ShoppingBasket,
        state = BottomNavBar.MenuItem.Shop
    ),
    AAA(
        "Video",
        Icons.Default.Airplay,
        state = BottomNavBar.MenuItem.PlayVideo
    ),
    AAA(
        "Chat",
        Icons.Default.Chat,
        state = BottomNavBar.MenuItem.Chat
    ),
    AAA(
        "Mine",
        Icons.Default.Face,
        state = BottomNavBar.MenuItem.Mine
    )
)

val items2 = listOf(//中间按钮是VideoList
    AAA(
        name = "Home",
        icon = Icons.Default.Home,
        state = BottomNavBar.MenuItem.Matrix
    ),
    AAA(
        "Songs",
        Icons.Default.ShoppingBasket,
        state = BottomNavBar.MenuItem.Shop
    ),
    AAA(
        "Video",
        Icons.Default.VideoLibrary,
        state = BottomNavBar.MenuItem.VideoList
    ),
    AAA(
        "Chat",
        Icons.Default.Chat,
        state = BottomNavBar.MenuItem.Chat
    ),
    AAA(
        "Mine",
        Icons.Default.Face,
        state = BottomNavBar.MenuItem.Mine
    )
)


