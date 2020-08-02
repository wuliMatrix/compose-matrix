package love.matrix.matrix.ui.ScreenVideo

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Menu
import love.matrix.matrix.AppContainer
import love.matrix.matrix.repository.posts.PostsRepository
import love.matrix.matrix.ui.VideoList.BiliVideoList

private enum class Bili1Sections(val title: String) {
    BiliHome("首页"),
    Topics("推荐"),

}

@Composable
fun BilibiliHome1(
    gotoPlayVideo: (String) -> Unit,
    appContainer: AppContainer,
    openDrawer: () -> Unit
) {
    val (currentSection, updateSection) = state { Bili1Sections.BiliHome }
    Scaffold(
       // scaffoldState = scaffoldState,//使用或或者不使用，看不出有什么区别，以后再琢磨
       topBar = {
           TopAppBar(
               title = { Text("bilibili") },
               navigationIcon = {
                   IconButton(onClick = openDrawer) {
                       Icon(asset = Icons.Filled.Menu)
                   }
               }
           )
       },
        bodyContent = {
            Bilibili1ViewBody(
                gotoPlayVideo = gotoPlayVideo,
                postsRepository = appContainer.postsRepository,
                currentSection = currentSection,
                updateSection = updateSection
            )
        }
    )
}

@Composable
private fun Bilibili1ViewBody(
    gotoPlayVideo: (String) -> Unit,
    postsRepository: PostsRepository,
    currentSection: Bili1Sections,         //当前选项，通过它，让当前选项卡比其他选项卡特别一些，突出一些
    updateSection: (Bili1Sections) -> Unit //选项卡之间的纽带，出现在onSelected里
) {
    val sectionTitles = Bili1Sections.values().map { it.title }

    Column {
        TabRow(
                items = sectionTitles,
                selectedIndex = currentSection.ordinal,// 返回此枚举常量的序数
                scrollable = true
        ) { index, title ->
            Tab(
                    text = { Text(title) },
                    selected = currentSection.ordinal == index,
                    onSelected = {
                        updateSection(Bili1Sections.values()[index])//更新选项卡状态
                    })
        }
        Box(modifier = Modifier.weight(1f)) {
            when (currentSection) {
                Bili1Sections.BiliHome -> {
                    BiliVideoList(gotoPlayVideo,postsRepository)//"首页"
                }
                Bili1Sections.Topics -> {
                    Surface(modifier = Modifier.weight(1f)) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                gravity = ContentGravity.Center,
                                children = {
                                    Text(text = "推荐")
                                })
                    }
                }

            }
        }
    }
}


