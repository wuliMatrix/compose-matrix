package love.matrix.matrix.ui.compose

import android.os.Handler
import android.os.Looper
import androidx.compose.Composable
import androidx.compose.onActive
import androidx.core.os.postDelayed
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.contentColor
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.layout.wrapContentSize
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Snackbar
import androidx.ui.material.TextButton
import androidx.ui.unit.dp
import love.matrix.matrix.ui.common.snackbarAction


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {//在页面中心
        CircularProgressIndicator()//圆形进度指示器
    }
}

@Composable
fun ErrorSnackbar(//错误信息提示条
    showError: Boolean,
    modifier: Modifier = Modifier,
    onErrorAction: () -> Unit = { },
    onDismiss: () -> Unit = { }
) {
    if (showError) {
        // Make Snackbar disappear after 5 seconds if the user hasn't interacted with it
        //如果用户未与Snackbar互动，则5秒钟后消失
        onActive {
            // With coroutines, this will be cancellable
            // 使用协程，可以取消
            Handler(Looper.getMainLooper()).postDelayed(5000L) {
                onDismiss()
            }
        }

        Snackbar(
            modifier = modifier.padding(60.dp),
            text = { Text("Can't update latest news") },
            action = {
                TextButton(
                    onClick = {
                        onErrorAction()
                        onDismiss()
                    },
                    //返回层次结构中呼叫站点位置处的首选内容颜色。
                    //此颜色应用于任何版式/图标，以确保这些颜色在背景颜色更改时进行调整。
                    //例如，在深色背景上，文字应为浅色，在浅色背景上，文字应为深色。
                    contentColor = contentColor()
                ) {
                    Text(
                        text = "RETRY",
                        color = MaterialTheme.colors.snackbarAction
                    )
                }
            }
        )
    }
}
