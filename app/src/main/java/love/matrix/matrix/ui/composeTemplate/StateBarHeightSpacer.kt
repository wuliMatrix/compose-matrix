package love.matrix.matrix.ui.composeTemplate

import androidx.activity.ComponentActivity
import androidx.compose.Composable
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.graphics.toArgb
import androidx.ui.layout.Spacer
import androidx.ui.layout.preferredHeight
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp

/**
 * 2020/7/11
 *
 * 这是一个高度为状态栏高度的Spacer,目前用在有两种状态栏的MainActivity里
 *
 * MainActivity的状态栏有两种状态，视频播放页面是一种状态（透明），其他页面是另一种状态（背景色）
 * 为了在不同的页面上显示符合要求的状态栏，这里采用的实现的方法是这样的：
 * 1，创建全局环境变量currentScreenIsPlayVideo来跟踪PlayVideoScreen这个页面，
 *   使用这样的方式是因为在MainActivity里只有PlayVideoScreen（视频播放页面）是特别的，
 *   进来这个页面和出去这个页面，状态栏都需要改变状态
 *   val playStateAmbient: ProvidableAmbient<PlayStatus> =
 *    ambientOf { throw IllegalStateException("PlayStatus is not initialized") }
 *
 *  class PlayStatus {
 *      var isPlayVideoScreen by mutableStateOf(YesorNo.No)//开始并不是PlayVideoScreen页面
 *  }
 *
 *  enum class YesorNo {Yes,No,}
 *
 *  创建全局变量，需要在使用之前完成初始化，一般放在相对应的activity里
 *   Providers(
 *        playStateAmbient provides PlayStatus()
 *   ) {
 *     .....
 *   }
 *
 * 2，对于底部导航栏（matrix,shop,video,chat,mine)，需要在这些组件的开始之处，加上这么一句话：
 *    Column(modifier = Modifier.fillMaxSize()) {
 *          StateBarHeightBox()
 *          xxxxxScreen()
 *    }
 *    StateBarHeightBox()所添加的Box,可以让actionBar离开状态栏区域,使它刚好出现在它应该出现的地方
 *
 *    对于侧边抽屉栏 :bilibili,matrix,,,则需要在这些组件的开始之处，加上这么一句话：
 *    playStateAmbient.current.isPlayVideoScreen = YesorNo.No
 *
 *    对于PlayVideoScreen自身,则需要在它自身组件之内，加上这么一句话：
 *    playStateAmbient.current.isPlayVideoScreen = YesorNo.Yes
 *
 * 3，在Video的开始之处，需要做相关的【if-else】判断，将他们区分开来，分别处理
 *    需在开始之处做判断是因为在开始之处就要添加StateBarHeightBox()
 *
 *    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)
 *
 *    if (playStateAmbient.current.isPlayVideoScreen == YesorNo.No) {
 *
 *         //设置状态栏的背景颜色
 *         activity.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
 *         Column(modifier = Modifier.fillMaxSize()) {
 *
 *               //给视图顶部加一个高度为状态栏高度的Box,让actionBar刚好离开状态栏区域
 *              StateBarHeightBox()
 *              VideoBodyContent(currentScreen, appContainer)
 *         }
 *     } else {
 *         //这另外一种情况，等打开PlayVideoScreen页面时再放置StateBarHeightBox()
 *         activity.window.statusBarColor = Color.Transparent.toArgb()
 *         VideoBodyContent(currentScreen, appContainer)
 *     }
 *
 * 4,如果还需要将BottonNavBar做成透明的，就需要给它准备两套按钮，
 *   并在BottonNavBar组件里对它作相关的判断（在何种情况，用哪一套）：
 *      val aa: List<MenuItem> = if (appState.currentScreen == Screen.Video) {
 *       if (playState.isPlayVideoScreen == YesorNo.Yes) {
 *             // 在底部导航栏之间切换时，PlayVideoScreen所对应的Screen.Video也是在变化的，
 *             // 所以这里需要设置状态栏背景色，页面切回到PlayVideoScreen时显示正确的透明色
 *             activity.window.statusBarColor = Color.Transparent.toArgb()
 *             items1//透明模式
 *       } else {
 *              //VideoScreen的状态栏是什么样的需要具体情况具体分析，所以这里不作处理，交给下一步处理
 *             //activity.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
 *             items2//常规模式
 *       }
 *       } else {
 *             activity.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
 *             items2//常规模式
 *       }
 *
 * 5,总的来说，挺繁琐的。有时候觉得一个activity对应一套状态栏，就没这么多事情了，
 *   只是又觉得这软件就是集各家之所长，所以，应该要有各种热门软件的身影（并且最终又有自己的独到之处）
 *   现在它，就是将火山小视频和抖音短视频整合在一起了。
 * 6,目前这样做，发现还存在的问题是进入PlayVideoScreen后，
 *   通过侧边抽屉退出的话，按系统退出按钮需要按三下才能退出软件。
 *   这是不符合人们使用常识的，暂时没有好的办法解决。不过那样使用软件的话，本来就不符合常规的使用方式
 *   所以，暂时就不作处理了，毕竟还有很多事情要做，以后有时间，有心情再完善。。。
 *
 *
 * 2020/7/14
 *
 * 觉得软件最开始的地方，尽量完善能减少重构的次数和重构的复杂度，所以我又满世界找解决办法，最终
 * 在compose-router的组件里找到解决的办法，真是"踏破铁鞋无觅处 得来全不费工夫"。
 *
 * 整体的思路是将compose-router的使用提升到setContent{...}级别，也就是说从一开始就使用router,
 * 每一个xxxScreen就变成了一棵树，整体来看视图就变成了树林，使用router,树与树之间可以通过回调传递数据，
 * 也就是说，数据可以放到onClick={ }里，从一棵树传给另一棵树，这在[enum+screen]方式是做不到的，
 * 详细实现方式请转到BottmNavBar1111.kt里查看
 *
 *
*/


@Composable
fun StateBarHeightSpacer(){

    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)

    //获取状态栏的高度 知识来源：https://blog.csdn.net/a_running_wolf/article/details/50477965
    var statusBarHeight = -1
    val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
    }
    //px转dp
    val scale: Float = activity.resources.displayMetrics.density
    val aa = statusBarHeight / scale - 0.5f // - 0.5f是为了让结果四舍五入
    Spacer(Modifier.preferredHeight(aa.dp))
}