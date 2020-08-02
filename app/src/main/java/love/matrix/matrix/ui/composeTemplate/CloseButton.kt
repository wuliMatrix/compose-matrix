package love.matrix.matrix.ui.composeTemplate

import androidx.activity.ComponentActivity
import androidx.compose.Composable
import androidx.ui.core.LifecycleOwnerAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.layout.size
import androidx.ui.material.IconButton
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Clear
import androidx.ui.unit.dp


/**
 * 2020/7/7
 *
 * 此组件实现了从@comppose组件关闭当前activity的功能
 *
 * as ComponentActivity 是因为@comppose组件的最开始的setContent就来源于ComponentActivity
 *
 * setContent点击进去(ctrl+鼠标）就是 ComponentActivity.setContent
 *
 *
 */

@Composable
fun CloseButton() {

    val activity = (LifecycleOwnerAmbient.current as ComponentActivity)

    IconButton(onClick = { activity.finish() }) {
        Icon(
            modifier = Modifier.size(50.dp),
            asset = Icons.Filled.Clear
        )
    }
}
