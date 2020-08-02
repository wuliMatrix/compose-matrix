package love.matrix.matrix.ui.chat

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.TopAppBar
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import coil.request.GetRequestBuilder
import coil.transform.CircleCropTransformation
import dev.chrisbanes.accompanist.coil.CoilImage
import love.matrix.matrix.data.Contact
import love.matrix.matrix.ui.common.typography

interface Co02ContactDetail {

    companion object {

        @Composable
        fun Content(isOpen: MutableState<Boolean>,contact: Contact, onBack: () -> Unit) {
            isOpen.value = false
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                          //  Text(text = contact.name)
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onBack()
                            }) {
                                Icon(Icons.Filled.ArrowBack)
                            }
                        },
                        backgroundColor = MaterialTheme.colors.background,
                        elevation = 0.dp
                    )
                },
                bodyContent = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalGravity = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            //尝试使用[Coil]加载给定的[data]，然后在[Image]中显示结果。
                            /*CoilImage(
                                request = GetRequestBuilder(ContextAmbient.current)
                                    .data(contact.image)
                                    .transformations(CircleCropTransformation())
                                    .build(),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.preferredSize(100.dp)
                            )*/
                            Text(
                                text = contact.author,
                                style = typography.h4.plus(TextStyle(textAlign = TextAlign.Center))
                            )
                            Text(text = contact.link, color = Color.Gray)
                            Text(
                                text = contact.author,
                                style = typography.body1.plus(TextStyle(color = MaterialTheme.colors.secondary)),
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            )
        }



    }
}