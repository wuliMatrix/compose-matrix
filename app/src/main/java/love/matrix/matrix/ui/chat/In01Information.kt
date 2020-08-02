package love.matrix.matrix.ui.chat

import android.content.Intent
import android.os.Bundle
import androidx.compose.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.lifecycle.switchMap
import androidx.ui.core.*
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.clickable
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.unit.PxBounds
import androidx.ui.unit.dp
import androidx.ui.unit.height
import androidx.ui.unit.width
import androidx.ui.util.fastForEach
import com.kuky.demo.wan.android.data.HomeArticleCacheDao
import com.kuky.demo.wan.android.data.db.HomeArticleDetail
import com.kuky.demo.wan.android.ui.home.HomeArticleViewModel
import kotlinx.coroutines.delay
import love.matrix.matrix.activity.AAA
import love.matrix.matrix.activity.ScopeEntity
import love.matrix.matrix.ui.compose.ViewPagerVerticalScroller
import love.matrix.matrix.ui.template.LazyColumnItemsScrollableComponent
import org.jetbrains.anko.collections.forEachByIndex
import org.jetbrains.anko.doAsyncResult
import org.koin.android.ext.android.getKoin
import org.koin.java.KoinJavaComponent.getKoin

// Models
data class Person(
    val name: String,
    val age: Int,
    val profilePictureUrl: String? = null
)

// Methods
fun getPersonList() = listOf(
    /* Person("Grace Hopper", 25),
     Person("Ada Lovelace", 29),
     Person("John Smith", 28),
     Person("Elon Musk", 41),
     Person("Will Smith", 31),*/
    // Person("Robert James", 42),
    //Person("Anthony Curry", 91),
    //Person("Kevin Jackson", 22),
    Person("Robert Curry", 1),
    Person("John Curry", 9),
    Person("Ada Jackson", 2),
    Person("Joe Defoe", 35),
    Person("John Curry", 9),
    Person("Ada Jackson", 2),
    Person("大规模枯叶夺在", 35)
)

val colors = listOf(
    Color(0xFFffd7d7.toInt()),
    Color(0xFFffe9d6.toInt()),
    Color(0xFFfffbd0.toInt()),
    Color(0xFFe3ffd9.toInt()),
    Color(0xFFd0fff8.toInt())
)


@Composable
fun In01Informations(viewModel: HomeArticleViewModel) {
    // LazyColumnItemsScrollableComponent(getPersonList())
    LazyColumnItemsScrollable(
        //getPersonList()
        viewModel
    )

}

@Composable
fun LazyColumnItemsScrollable(
    articleList: HomeArticleViewModel
) {
    //var textaaa by state{getKoin().getScope("scope1").get<ScopeEntity>().text}

    val rootView = (ViewAmbient.current as AndroidOwner).view
    val context = rootView.context
    val intent = Intent(context, AAA::class.java)//java 要转场到的Activity,格式：XxxActivity::class.java
    val globalBounds = remember { Ref<PxBounds>() }
    val options: Bundle? = globalBounds.value?.let {
        ActivityOptionsCompat.makeScaleUpAnimation(//转场动画
            rootView,
            it.left.toInt(),
            it.top.toInt(),
            it.width.toInt(),
            it.height.toInt()
        ).toBundle()
    }

    val cols = 1//在这里控制数据显示的列数

    val list by articleList.getArticles().observeAsState(initial = emptyList<HomeArticleDetail>())
    VerticalScroller {
        list?.forEachByIndex { data ->

            Row(
                verticalGravity = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {

                Text(text = data.link, modifier = Modifier.padding(start = 12.dp).clickable(
                    onClick = {
                        intent.putExtra("aaa", data.link)
                        startActivity(context, intent, options)
                    }
                ))
                Spacer(modifier = Modifier.fillMaxWidth())
            }
            Row(
                verticalGravity = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {

                Text(text = getKoin().getScope("scope1").get<ScopeEntity>().text,
                    modifier = Modifier.padding(start = 12.dp).clickable(
                        onClick = {
                            intent.putExtra("aaa", data.link)
                            startActivity(context, intent, options)
                        }
                    ))
                Spacer(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}



