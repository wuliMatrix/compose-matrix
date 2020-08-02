package love.matrix.matrix.ui.VideoList

import android.content.Intent
import android.os.Bundle
import androidx.compose.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.ui.core.*
import androidx.ui.foundation.Box
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.PhotoCamera
import androidx.ui.unit.PxBounds
import androidx.ui.unit.dp
import androidx.ui.unit.height
import androidx.ui.unit.width
import love.matrix.matrix.AppContainer
import love.matrix.matrix.ar.GltfActivity
import love.matrix.matrix.ui.ScreenVideo.BilibiliHome
import love.matrix.matrix.ui.ScreenVideo.BilibiliHome1
import love.matrix.matrix.ui.composeTemplate.BottomBarHeightSpacer
import love.matrix.matrix.ui.composeTemplate.StateBarHeightSpacer

enum class DrawerVideoScreen(val title: String) {
    Bilibili("Bilibili"),
    Bilibili1("Bilibili1"),
    Interests("Interets"),
}


@Composable
fun VideoListScreen(
    currentDrawerScreen: MutableState<DrawerVideoScreen>,
    appContainer: AppContainer,
    gotoPlayVideo: (String) -> Unit

) {

    val scaffoldState: ScaffoldState = remember { ScaffoldState() }

    Column(modifier = Modifier.fillMaxSize()) {
        StateBarHeightSpacer()
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                DrawerContentComponent(

                    //currentScreen不带value，说明接下来会有for循环，currentScreen带value放在里面
                    currentDrawerScreen = currentDrawerScreen,
                    closeDrawer = { scaffoldState.drawerState = DrawerState.Closed }
                )
            },
            bodyContent = { innerPadding ->
                val modifier = Modifier.padding(innerPadding)//让内容显示在bottomBar之上
                Box(modifier = modifier.fillMaxSize()) {
                    BodyContentComponent(
                        gotoPlayVideo = gotoPlayVideo,
                        //currentScreen带value，说明接下来有when判断,currentScreen不带value放在里面
                        currentDrawerScreen = currentDrawerScreen.value,
                        appContainer = appContainer,
                        openDrawer = {
                            scaffoldState.drawerState = DrawerState.Opened
                        }
                    )
                }
            },
            floatingActionButton = { VideoFloatingActionButton() },
            //添加一个空白体，让内容不在bottomBar内显示，需要innerPadding配合
            bottomBar = { BottomBarHeightSpacer() }
        )
    }
}

@Composable
private fun BodyContentComponent(
    gotoPlayVideo: (String) -> Unit,
    currentDrawerScreen: DrawerVideoScreen,
    appContainer: AppContainer,
    openDrawer: () -> Unit
) {
    when (currentDrawerScreen) {
        DrawerVideoScreen.Bilibili -> BilibiliHome(
            gotoPlayVideo = gotoPlayVideo,
            appContainer = appContainer,
            openDrawer = openDrawer
        )
        DrawerVideoScreen.Bilibili1 -> BilibiliHome1(
            gotoPlayVideo = gotoPlayVideo,
            appContainer = appContainer,
            openDrawer = openDrawer
        )
        DrawerVideoScreen.Interests -> Interests(
            interestsRepository = appContainer.interestsRepository,
            openDrawer = openDrawer
        )
    }
}

//这个【drawer】的实现方式结合了【Learn-Jetpack-Compose-By-Example】和【JetNews】两个项目
//本来想解决【drawer】快速切换会卡住的问题，结果实现后还是老样子，可能是【drawer】本身问题
//期待官方给出完善方案--2020/7/3
@Composable
fun DrawerContentComponent(
    currentDrawerScreen: MutableState<DrawerVideoScreen>,
    closeDrawer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.preferredHeight(150.dp))//空白分隔体
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))//分隔线
        for (index in DrawerVideoScreen.values().indices) {
            val screen = getVideoScreenBasedOnIndex(index)
            Box(Modifier.clickable(onClick = {

                //一个等号，实现了页面的切换（在onClick里使用，onClick是页面之间的纽带）
                currentDrawerScreen.value = screen
                closeDrawer()
            }), children = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),

                    //两个等号，实现了组件状态的切换（在if判断里使用，if判断是组件显示还是不显示的纽带）
                    color = if (currentDrawerScreen.value == screen) {
                        MaterialTheme.colors.secondary
                    } else {
                        MaterialTheme.colors.surface
                    }
                ) {
                    Text(text = screen.title, modifier = Modifier.padding(16.dp))
                }
            })
        }
    }
}

fun getVideoScreenBasedOnIndex(index: Int) = when (index) {//for循环里面的when,平凡里找特例，着重处理
    0 -> DrawerVideoScreen.Bilibili
    1 -> DrawerVideoScreen.Interests
    2 -> DrawerVideoScreen.Bilibili1
    else -> DrawerVideoScreen.Bilibili
}

@Composable
fun VideoFloatingActionButton() {
    val rootView = (ViewAmbient.current as AndroidOwner).view//取得当前视图[view]
    val context = rootView.context
    val intent = Intent(context, GltfActivity::class.java)
    val globalBounds = remember { Ref<PxBounds>() }
    val options: Bundle? = globalBounds.value?.let {
        //创建一个ActivityOptions，指定一个动画，在该动画中，新活动将从屏幕的一小块原始区域缩放到其最终的完整表示形式。
        ActivityOptionsCompat.makeScaleUpAnimation(
            rootView,
            it.left.toInt(),
            it.top.toInt(),
            it.width.toInt(),
            it.height.toInt()
        ).toBundle()
    }

    FloatingActionButton(
        onClick = { startActivity(context, intent, options) }, //从composable跳转到Activity
        backgroundColor = Color(0xFF08a0e9)
    ) {
        Image(
            Icons.Default.PhotoCamera,
            modifier = Modifier
                .preferredSize(25.dp)
        )
    }

}
