package love.matrix.matrix

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.state
import androidx.lifecycle.Observer
import androidx.ui.animation.Crossfade
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.layout.ColumnScope.weight
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.material.*
import androidx.ui.res.vectorResource
import androidx.ui.unit.dp
import com.github.zsoltk.compose.backpress.AmbientBackPressHandler
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.savedinstancestate.BundleScope
import com.github.zsoltk.compose.savedinstancestate.saveAmbient
import com.kuky.demo.wan.android.ui.home.HomeArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import love.matrix.matrix.activity.ScopeEntity
import love.matrix.matrix.ambients.informationRepository
import love.matrix.matrix.repository.information.InformationRepository
import love.matrix.matrix.repository.posts.PostsRepository
import love.matrix.matrix.ui.RootScreen
import love.matrix.matrix.ui.common.MatrixTheme
import love.matrix.matrix.ui.VideoList.*
import love.matrix.matrix.ui.matrix.MatrixScreen
import love.matrix.matrix.ui.matrix.Po00PostView
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.bindScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val backPressHandler = BackPressHandler()

    private val mViewModel by viewModel<HomeArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //创建 scope 需要指定 id 和 qualifier，getScope 需要id
        val scope1 = getKoin().createScope("scope1", named("scope"))
        //默认绑定onDestory
        bindScope(scope1)
        scope1.get<ScopeEntity>().text = "Scope1Activity"


        val appContainer = (application as MainApplication).container
        // viewModel.getUsers.observe(this, Observer {})

        //沉浸式状态栏  知识来源： https://blog.csdn.net/mldxs/article/details/87801266
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //window.statusBarColor = Color.TRANSPARENT//这里使用了两种状态栏，需要具体情况具体分析

        val videoId = "aaaaaaa"
        setContent {

            val currentDrawerScreen = state { DrawerVideoScreen.Bilibili }
            /*val currentDrawerScreen = state {
                if ((0..1).random() == 1) {
                    DrawerVideoScreen.Bilibili1
                } else {
                    DrawerVideoScreen.Bilibili
                }
            }*/
            MatrixTheme {
                //环境值初始化，具体使用方式详见composeTemplate包中的BottomNavBar1111组件
                Providers(
                    AmbientBackPressHandler provides backPressHandler,
                    informationRepository provides InformationRepository()
                ) {
                    Crossfade(savedInstanceState) {
                        Surface(color = MaterialTheme.colors.background) {

                            //comopose-router从根视图就开始使用
                            RootScreen.Content(
                                /*defaultRouting = if ((0..1).random() == 1) {
                                    RootScreen.Routing.VideoList
                                } else {
                                    RootScreen.Routing.PlayVideo(
                                        videoSource = currentDrawerScreen.value,
                                        videoId = videoId
                                    )
                                },*/
                                defaultRouting = RootScreen.Routing.Matrix,
                                appContainer = appContainer,
                                currentDrawerScreen = currentDrawerScreen,
                                viewModel = mViewModel
                            )
                        }
                    }

                }
            }
        }
    }


    private var backPressTime = 0L
    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (!backPressHandler.handle()) {
            if (now - backPressTime > 2000) {
                //Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show()
                backPressTime = now
            } else {
                super.onBackPressed()
            }
        }
    }

    //退出应用的时候，保存相关状态值，以供稍后打开时使用
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.saveAmbient()
    }
}


@Composable
fun aaa(
    postsRepository: PostsRepository
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Jetnews") }
            )
        },
        bodyContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = Color.Green,
                gravity = ContentGravity.Center,
                children = {
                    Text(text = "母体")
                })
        },
        bottomBar = { Spacer(Modifier.preferredHeight(176.dp).drawBackground(color = Color.Red)) }

    )


}