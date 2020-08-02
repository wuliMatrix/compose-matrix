package love.matrix.matrix.activity

import com.android.example.paging.pagingwithnetwork.reddit.ui.SubRedditViewModel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.clickable
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Scaffold
import androidx.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent


class AAA : AppCompatActivity() {

   var linkVideo :String? = null
    var aa = 2
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        aa++
        getKoin().getScope("scope1").get<ScopeEntity>().text="saaaaaaaaa$aa"

       linkVideo  = intent.getStringExtra("aaa")

       setContent { ContactItem11(linkVideo) }


    }


}

@Composable
fun ContactItem11(linkVideo : String?,modifier: Modifier = Modifier) {
    VerticalScroller {

       // contact.map {


            Row(
                verticalGravity = Alignment.CenterVertically,
                modifier = modifier.plus(Modifier.padding(12.dp))
            ) {
                /*CoilImage(
                    request = GetRequestBuilder(ContextAmbient.current)
                        .data(contact.image)
                        .transformations(CircleCropTransformation())//它使用居中的圆圈作为蒙版裁剪图像。
                        .build(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.preferredSize(40.dp)
                )*/
                Text(
                    text = linkVideo.orEmpty(),
                    modifier = Modifier.padding(start = 12.dp)
                )
                Spacer(modifier = Modifier.fillMaxWidth())
            }

        Row(
            verticalGravity = Alignment.CenterVertically,
            modifier = modifier.plus(Modifier.padding(12.dp))
        ) {
            /*CoilImage(
                request = GetRequestBuilder(ContextAmbient.current)
                    .data(contact.image)
                    .transformations(CircleCropTransformation())//它使用居中的圆圈作为蒙版裁剪图像。
                    .build(),
                contentScale = ContentScale.Crop,
                modifier = Modifier.preferredSize(40.dp)
            )*/
            Text(
                text = "aaaaa",
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth())
        }
//
        //}
    }

}