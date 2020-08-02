package love.matrix.matrix.ui.shop

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxSize
import androidx.ui.material.*
import love.matrix.matrix.ui.composeTemplate.StateBarHeightSpacer


@Composable
fun ShopScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        StateBarHeightSpacer()
        Scaffold(
            bodyContent = {
                Surface(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        gravity = ContentGravity.Center,
                        children = {
                            Text(text = "购物")
                        })
                }
            }
        )
    }
}