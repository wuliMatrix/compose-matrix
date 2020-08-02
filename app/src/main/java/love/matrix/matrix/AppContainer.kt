package love.matrix.matrix

import android.content.Context
import android.os.Handler
import android.os.Looper
import love.matrix.matrix.repository.bilibili.BilibiliImpl
import love.matrix.matrix.repository.bilibili.BilibiliRepository
import love.matrix.matrix.repository.interests.InterestsRepository
import love.matrix.matrix.repository.interests.impl.FakeInterestsRepository
import love.matrix.matrix.repository.posts.PostsImpl
import love.matrix.matrix.repository.posts.PostsRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Dependency Injection container at the application level.
 * 在应用程序级别的依赖注入容器。
 */
interface AppContainer {
    val postsRepository: PostsRepository//文章贴子
    val bilibiliRepository: BilibiliRepository//文章贴子
    val interestsRepository: InterestsRepository
}
/**
 * Implementation for the Dependency Injection container at the application level.
 * Variables are initialized lazily and the same instance is shared across the whole app.
 * app容器实现类。
 * 变量被延迟初始化，并且在整个应用程序中共享同一实例。
 */
class AppContainerImpl(private val applicationContext: Context) :
    AppContainer {

    private val executorService: ExecutorService by lazy {

        Executors.newFixedThreadPool(4)//创建一个重用固定数量线程的线程池
    }

    private val mainThreadHandler: Handler by lazy {
        //返回应用程序的主循环程序，该循环程序位于应用程序的主线程中。
        Handler(Looper.getMainLooper())
    }

    override val postsRepository: PostsRepository by lazy {
        PostsImpl(//模拟数据
            executorService = executorService,//线程池
            resultThreadHandler = mainThreadHandler,//运行在主线程
            resources = applicationContext.resources//数据
        )
    }

    override val bilibiliRepository: BilibiliRepository by lazy {
        BilibiliImpl()
    }

    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }

}
