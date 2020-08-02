package love.matrix.matrix.ui.matrix

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.layout.*
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import androidx.ui.unit.dp
import love.matrix.matrix.repository.posts.PostsRepository
import love.matrix.matrix.ui.composeTemplate.StateBarHeightSpacer


@Composable
fun MatrixScreen(
    postsRepository: PostsRepository
) {

    Column(modifier = Modifier.fillMaxSize()) {
       StateBarHeightSpacer()
        Scaffold(
            bodyContent = {

                Matrix69Post(postsRepository)
            }
        )
    }
}


@Composable
fun Matrix69Post(postsRepository: PostsRepository) {
    Matrix69Post.Content(postsRepository, Matrix69Post.Routing.Post)
}
