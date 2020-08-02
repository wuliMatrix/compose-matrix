package love.matrix.matrix.ui.PlayVideo

import LocalResourceImageComponent
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.AllInclusive
import androidx.ui.material.icons.filled.DriveEta
import androidx.ui.material.icons.filled.EmojiObjects
import androidx.ui.unit.dp
import love.matrix.matrix.R
import love.matrix.matrix.ui.VideoList.DrawerVideoScreen
import love.matrix.matrix.ui.compose.ViewPagerHorizontalScroller
import love.matrix.matrix.ui.compose.ViewPagerVerticalScroller
import love.matrix.matrix.ui.composeTemplate.*


@Composable
fun PlayVideoScreen(
    videoSource: DrawerVideoScreen,
    videoId: String,
    backVideoList: () -> Unit

) {
    Scaffold(
        bodyContent = {
            PlayVideoBody(videoSource,videoId, backVideoList)
        }
    )
}

@Composable
fun PlayVideoBody(
    videoSource: DrawerVideoScreen,
    videoId: String,
    backVideoList: () -> Unit
) {

    // Stack组件的特点是，它绘制完后下一个组件还是从它最开始的地方绘制，所以组件之间是独立的，重叠的,
    // 使用时大块的应该首先绘制，否则小块的很可能会被完全盖住
    Stack(modifier = Modifier.fillMaxWidth()) {
        ViewPagerVerticalScroller(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = Color.White
            ) {
                Text("Index: $index", Modifier.padding(8.dp))

                Row {
                    TextButton(
                        onClick = { },
                        modifier = Modifier.padding(8.dp),
                        backgroundColor = Color.Red
                    ) {
                        Text("Previous", color = Color.White)
                    }

                    TextButton(
                        onClick = { },
                        modifier = Modifier.padding(8.dp),
                        backgroundColor = Color.Red
                    ) {
                        Text(videoId, color = Color.White)
                    }
                }
                LocalResourceImageComponent(resId = R.drawable.lenna)
            }
        }
    }

    //放在Stack下面避免被它全部盖住而看不见
    Column(modifier = Modifier.fillMaxSize()) {

        StateBarHeightSpacer()
        TopAppBar(
            title = {},
            contentColor = Color.White,
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            navigationIcon = {
                IconButton(onClick = {
                    backVideoList()
                }) {
                    when(videoSource) {
                       DrawerVideoScreen.Bilibili -> Icon(Icons.Filled.AllInclusive)
                        DrawerVideoScreen.Bilibili1 -> Icon(Icons.Filled.DriveEta)
                        DrawerVideoScreen.Interests -> Icon(Icons.Filled.EmojiObjects)
                    }
                }
            }
        )
    }

}



