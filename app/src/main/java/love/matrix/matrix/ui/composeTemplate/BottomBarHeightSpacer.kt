package love.matrix.matrix.ui.composeTemplate

import androidx.activity.ComponentActivity
import androidx.compose.Composable
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.core.Modifier
import androidx.ui.layout.Spacer
import androidx.ui.layout.preferredHeight
import androidx.ui.unit.dp


/**
 * 2020/7/20
 *
 * 唉，，，想不到在MainActivity使用两种状态栏是这么麻烦的事，
 * innerPadding可以让内容不显示到BottomNavBar，可是由于有两种BottomNavBar
 * 它就不能和BottomNavBar放在一起，需要往后一步根据具体情况来设置，
 * 而在设置它的时候，由于BottomNavBar已经在前面设置好了，就需要用另建一个高度为BottomNavBar高度的载体
 * 来填补BottomNavBar的位置，如同状态栏的StateBarHeightSpacer()一样
 * 如何获取BottomNavBar高度，我是没耐心通过网络找答案了，就ctrl+鼠标点点点，点开BottomNavigation
 * 找到了 BottomNavigationHeight = 56.dp 就直接设置
 * Spacer(Modifier.preferredHeight(56.dp))
 *
 */
@Composable
fun BottomBarHeightSpacer(){

    Spacer(Modifier.preferredHeight(56.dp))
}