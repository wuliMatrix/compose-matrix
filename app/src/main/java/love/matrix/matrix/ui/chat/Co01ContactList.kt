package love.matrix.matrix.ui.chat

import androidx.compose.*
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Scaffold
import androidx.ui.unit.dp
import coil.request.GetRequestBuilder
import coil.transform.CircleCropTransformation
import dev.chrisbanes.accompanist.coil.CoilImage
import love.matrix.matrix.data.Contact
import love.matrix.matrix.data.ContactList
import love.matrix.matrix.repository.contact.getContacts


interface Co01ContactList {

    companion object {
        @Composable
        fun Content(isOpen: MutableState<Boolean>,onClick: (contact: Contact) -> Unit) {
            isOpen.value = true

            var list by state { ContactList(data = emptyList()) }

            launchInComposition {
                list = getContacts()
            }
            Scaffold(
                bodyContent = {
                    VerticalScroller {
                        list.data.map {
                            ContactItem(it, modifier = Modifier.clickable(onClick = {
                                onClick(it)
                            }))
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ContactItem(contact: Contact, modifier: Modifier = Modifier) {
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
        Text(text = contact.author, modifier = Modifier.padding(start = 12.dp))
        Spacer(modifier = Modifier.fillMaxWidth())
    }
}