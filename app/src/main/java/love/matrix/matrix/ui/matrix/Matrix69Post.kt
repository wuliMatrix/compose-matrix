package love.matrix.matrix.ui.matrix


import androidx.compose.*
import com.github.zsoltk.compose.router.Router
import love.matrix.matrix.repository.posts.PostsRepository

interface Matrix69Post {
    sealed class Routing() {
        object Post : Routing()
        data class Article(val postId: String) : Routing()
    }

    companion object {
        @Composable
        fun Content(
            postsRepository: PostsRepository,
            defaultRouting: Routing
        ) {
            Router(defaultRouting) { backStack ->
                when (val routing = backStack.last()) {
                    is Routing.Post -> Po00Post.Content(
                        postsRepository = postsRepository,
                        onPostSelected = {
                            backStack.push(Routing.Article(it))
                        })

                    is Routing.Article -> Po01Article.Content(
                        postId = routing.postId,
                        postsRepository = postsRepository,
                        onBack = {
                            backStack.pop()
                        }
                    )
                }
            }
        }
    }
}
