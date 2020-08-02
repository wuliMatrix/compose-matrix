package love.matrix.matrix.ui.matrix

import androidx.compose.*
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.weight
import androidx.ui.material.*
import androidx.ui.unit.dp
import love.matrix.matrix.data.Post
import love.matrix.matrix.repository.*
import love.matrix.matrix.repository.posts.PostsRepository
import love.matrix.matrix.ui.ScreenVideo.PostCardTop
import love.matrix.matrix.ui.compose.ErrorSnackbar
import love.matrix.matrix.ui.compose.LoadingScreen
import love.matrix.matrix.ui.compose.SwipeToRefreshLayout
import love.matrix.matrix.ui.composeTemplate.BottomNavBar


interface Po00Post {

    companion object {
        @Composable
        fun Content( postsRepository: PostsRepository,onPostSelected: (String) -> Unit) {
           Po00PostView(postsRepository,onPostSelected)
        }
    }
}

@Composable
fun Po00PostView(
    postsRepository: PostsRepository,
    onPostSelected: (String) -> Unit
) {
   Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("母体") }
            )
        },
        bodyContent = {innerPadding ->
            val modifier = Modifier.padding(innerPadding)//让内容显示在bottomBar之上
            val (postsState, refreshPosts) = refreshableUiStateFrom(postsRepository::getPosts)
            if (postsState.loading && !postsState.refreshing) {//没有旧数据，加载数据
                LoadingScreen()
            } else {
                SwipeToRefreshLayout(
                    refreshingState = postsState.refreshing,
                    onRefresh = { refreshPosts() },
                    refreshIndicator = {//Indicator 指示符
                        Surface(elevation = 10.dp, shape = CircleShape) {//elevation 高程
                            //循环进度指示器
                            CircularProgressIndicator(
                                Modifier.preferredSize(50.dp).padding(4.dp)
                            )
                        }
                    }
                ) {
                    ScreenBodyWrapper(//屏幕主体包装
                        modifier=  modifier,
                        onPostSelected = onPostSelected,
                        state = postsState,
                        onErrorAction = {
                            refreshPosts()
                        }
                    )
                }
            }
        },
        bottomBar = {
           Spacer(Modifier.preferredHeight(56.dp))
        }

    )
}



@Composable
fun ScreenBodyWrapper(//Wrapper 包装
    modifier:Modifier,
   onPostSelected: (String) -> Unit,
    state: RefreshableUiState<List<Post>>,
    onErrorAction: () -> Unit
) {
    // State for showing the Snackbar error. This state will reset with the content of the lambda
    // inside stateFor each time the RefreshableUiState input parameter changes.
    // showSnackbarError is the value of the error state, use updateShowSnackbarError to update it.
    // 显示Snackbar错误的状态。每次RefreshableUiState输入参数更改时，此状态都会用lambda的内容重置。
    // showSnackbarError是错误状态的值，请使用updateShowSnackbarError对其进行更新
    //当state更改时，它将导致状态重置并重新运行[init]
    val (showSnackbarError, updateShowSnackbarError) = stateFor(state) {
        state is RefreshableUiState.Error
    }
    // 一个可组合对象，其子元素相对于其边缘定位。
    // 该组件对于绘制重叠的子项很有用。将始终按照在[Stack]主体中指定的顺序绘制子项。
    // 使用[StackScope.gravity]修饰符定义目标元素在[Stack]框中的位置。
    Stack(modifier = modifier.fillMaxSize()) {
        state.currentData?.let { posts ->
            MatrixScreenBody(
               onPostSelected,
                posts = posts
            )
        }
        ErrorSnackbar(//数据加载失败提示条
            showError = showSnackbarError,
            onErrorAction = onErrorAction,
            onDismiss = { updateShowSnackbarError(false) },
            modifier = Modifier.gravity(Alignment.BottomCenter)
        )
    }

}

@Composable
private fun MatrixScreenBody(
    onPostSelected: (String) -> Unit,
    posts: List<Post>,
    modifier: Modifier = Modifier
) {

    VerticalScroller(modifier = modifier) {
        posts.forEach { post ->
            PostCardTop(
                post = post,
                modifier = Modifier.clickable(onClick = {

                   onPostSelected(post.id)
                })
            )
            VideoScreenDivider()
        }
    }
}


@Composable
private fun VideoScreenDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        //copy 复制现有颜色，仅更改提供的值
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}

