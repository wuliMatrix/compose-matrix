package love.matrix.matrix.repository.posts

import android.content.res.Resources
import android.os.Handler
import androidx.ui.graphics.imageFromResource
import love.matrix.matrix.data.Post
import love.matrix.matrix.fakeData.posts
import love.matrix.matrix.repository.Result
import java.util.concurrent.ExecutorService
import kotlin.random.Random


class PostsImpl(
    //ExecutorService 跟踪一个或多个异步任务的进度,并提供管理终止和终止的方法
    private val executorService: ExecutorService,
    private val resultThreadHandler: Handler,
    private val resources: Resources
) : PostsRepository {

    /**
     * Simulates preparing the data for each post.
     *
     * DISCLAIMER: Loading resources with the ApplicationContext isn't ideal as it isn't themed.
     * This should be done from the UI layer.
     * 模拟为每个帖子准备数据。
     * 免责声明：由于不是主题主题，因此使用ApplicationContext加载资源并不理想。这应该从UI层完成。
     */
    private val postsWithResources: List<Post> by lazy {
        posts.map {
            it.copy(
                image = imageFromResource(resources, it.imageId),
                imageThumb = imageFromResource(resources, it.imageThumbId)
            )
        }
    }

    override fun getPost(postId: String, callback: (Result<Post?>) -> Unit) {
        executeInBackground(callback) {
            resultThreadHandler.post {//更新UI
            callback(Result.Success(
                    //匹配的第一个元素，如果没有找到则返回null。
                    postsWithResources.find { it.id == postId }
                ))
            }
        }
    }

    override fun getPosts(callback: (Result<List<Post>>) -> Unit) {
        executeInBackground(callback) {


            simulateNetworkRequest()//模拟请求网络
            Thread.sleep(1000L)
            if (shouldRandomlyFail()) {
                throw IllegalStateException()
            }
            resultThreadHandler.post { callback(Result.Success(postsWithResources)) }
        }
    }

    /**
     * Executes a block of code in the past and returns an error in the [callback]
     * if [block] throws an exception.
     * 执行一段代码，如果[block]抛出异常，则在[回调]中将错误信息返回。
     */
    private fun executeInBackground(callback: (Result<Nothing>) -> Unit, block: () -> Unit) {
        executorService.execute {
            try {
                block()
            } catch (e: Exception) {
                resultThreadHandler.post { callback(Result.Error(e)) }//将错误信息添加到消息队列中。
            }
        }
    }

    /**
     * Simulates network request 模拟网络请求
     */
    private var networkRequestDone = false
    private fun simulateNetworkRequest() {
        if (!networkRequestDone) {
            Thread.sleep(800L)
            networkRequestDone = true
        }
    }

    /**
     * 1/3 requests should fail loading 1/3个请求应该加载失败
     */
    private fun shouldRandomlyFail(): Boolean = Random.nextFloat() < 0.0f
}
