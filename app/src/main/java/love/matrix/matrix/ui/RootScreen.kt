package love.matrix.matrix.ui

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.material.Scaffold
import com.github.zsoltk.compose.router.Router
import com.kuky.demo.wan.android.ui.home.HomeArticleViewModel
import love.matrix.matrix.AppContainer
import love.matrix.matrix.ui.chat.ChatScreen
import love.matrix.matrix.ui.composeTemplate.BottomNavBar
import love.matrix.matrix.ui.matrix.MatrixScreen
import love.matrix.matrix.ui.mine.MineScreen
import love.matrix.matrix.ui.shop.ShopScreen
import love.matrix.matrix.ui.VideoList.DrawerVideoScreen
import love.matrix.matrix.ui.PlayVideo.PlayVideoScreen
import love.matrix.matrix.ui.VideoList.VideoListScreen

/**
 * 2020/7/14
 *
 * 这里是这款软件视图的根。你可以把它看成一片树林，其中的每一个【xxxScreen】都是其中的一棵树
 *
 * 这里通过使用compose-router，让树与树之间也可以交换信息。目前只做了
 *
 * 从“VideoListScreen”取到视频ID（videoId)，然后放到“PlayVideoScreen”进行播放这件事
 *
 * 其中的实现方式大概是这样的：在Screen里放回调参数，让这个参数跟着相关视图层层推进,直到拿到videoID,
 *
 * 拿到videoId后，把videoId放到 onClick = {}里，触底反弹回到这里，
 *
 * 然后根据回调参数里设置的值更改currentRouting，完成视图的切换
 *
 */

interface RootScreen {
    sealed class Routing {
        object Matrix : Routing()
        object Mine : Routing()
        object Shop : Routing()
        object Chat : Routing()
        object VideoList : Routing()
        data class PlayVideo(
            val videoSource: DrawerVideoScreen,// PlayVideo 视频来源  <==> 侧边抽屉  VideoList
            val videoId: String
        ) : Routing()
    }

    companion object {
        @Composable
        fun Content(
            defaultRouting: Routing,
            appContainer: AppContainer,
            currentDrawerScreen: MutableState<DrawerVideoScreen>,
            viewModel: HomeArticleViewModel
        ) {
            Router(defaultRouting) { backStack ->
                val currentRouting = backStack.last()

                Scaffold(
                    bodyContent = {
                        //这里的每一个“is"项都相当于一棵树，它们组成了树林
                        when (currentRouting) {
                            is Routing.Matrix -> MatrixScreen(appContainer.postsRepository)
                            is Routing.Shop -> ShopScreen()
                            is Routing.Mine -> MineScreen()
                            is Routing.Chat -> ChatScreen(viewModel)
                            is Routing.VideoList -> VideoListScreen(
                                currentDrawerScreen = currentDrawerScreen,
                                appContainer = appContainer,
                                gotoPlayVideo = {
                                    //这里使用push，说明VideoList -> PlayVideo是进级的
                                    //按系统回退键，会从PlayVideo返回到VideoList
                                    backStack.push(
                                        Routing.PlayVideo(currentDrawerScreen.value, it)
                                    )
                                }
                            )
                            is Routing.PlayVideo -> PlayVideoScreen(
                                videoSource = currentDrawerScreen.value,
                                videoId = currentRouting.videoId,
                                backVideoList = {
                                    //这里使用newRoot，说明PlayVideo -> VideoList是平级的
                                    //按系统回退键，不会从VideoList返回到PlayVideo
                                    backStack.newRoot(
                                        Routing.VideoList
                                    )
                                }
                            )
                        }
                    },
                    bottomBar = {

                        //这里BottomNavBar的实现方式是比较绕的，转换来又转换去，，，，
                        BottomNavBar.Content(
                            state = currentRouting.toMenuState(),
                            onMenuItemClicked = { menuItem ->
                                //使用newRoot,众生平等
                                backStack.newRoot(menuItem.toRouting())
                            }
                        )
                    }
                )

            }
        }

        //Routing转MenuItem，页面打开后，选中按钮变成当前页面的Menu
        private fun Routing.toMenuState() =
            BottomNavBar.State(
                currentSelection = when (this) {
                    is Routing.Matrix -> BottomNavBar.MenuItem.Matrix
                    is Routing.Shop -> BottomNavBar.MenuItem.Shop
                    is Routing.VideoList -> BottomNavBar.MenuItem.VideoList
                    is Routing.PlayVideo -> BottomNavBar.MenuItem.PlayVideo
                    is Routing.Chat -> BottomNavBar.MenuItem.Chat
                    is Routing.Mine -> BottomNavBar.MenuItem.Mine
                }
            )

        //MenuItem转Routing，点击按钮后，页面发生相关跳转（跳转到当前页面）
        private fun BottomNavBar.MenuItem.toRouting(): Routing = when (this) {
            is BottomNavBar.MenuItem.Matrix -> Routing.Matrix
            is BottomNavBar.MenuItem.Shop -> Routing.Shop
            is BottomNavBar.MenuItem.VideoList -> Routing.VideoList

            // 问：这里还需要改进吧？？？
            // 答：嗯，，，，，，好像它使用的是MainActivity里初始化的值，
            // 这里设置的值始终不会起作用。虽然，个中逻辑关系还理不清楚，但是，从测试结果来看，
            // 其中的逻辑关系表现为“这里设置的值会被MainActivity里设置的值覆盖掉”，
            // 这种特点正好符合功能需求，所以，就不理会了它。
            is BottomNavBar.MenuItem.PlayVideo -> Routing.PlayVideo(
                videoSource = DrawerVideoScreen.Bilibili,
                videoId = "aaa"
            )
            is BottomNavBar.MenuItem.Chat -> Routing.Chat
            is BottomNavBar.MenuItem.Mine -> Routing.Mine
        }
    }
}

