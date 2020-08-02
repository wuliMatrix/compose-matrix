package love.matrix.matrix.ui.chat

import androidx.compose.Composable
import androidx.compose.MutableState
import com.github.zsoltk.compose.router.Router
import love.matrix.matrix.data.Album
import love.matrix.matrix.data.Contact
import love.matrix.matrix.data.Photo
import love.matrix.matrix.router.AlbumList
import love.matrix.matrix.router.FullScreenPhoto
import love.matrix.matrix.router.PhotosOfAlbum




interface Chat69Contacts {//联系人

    sealed class Routing {
        object ContactList : Routing()
        data class ContactDetail(val contact: Contact) : Routing()
    }

    companion object {
        @Composable
        fun Content(
            isOpen: MutableState<Boolean>,
            defaultRouting: Routing
        ) {
            Router(defaultRouting) { backStack ->
                when (val routing = backStack.last()) {
                        is Routing.ContactList -> Co01ContactList.Content(
                            isOpen = isOpen,
                            onClick = {
                                backStack.push(Routing.ContactDetail(it))
                            })
                        is Routing.ContactDetail -> Co02ContactDetail.Content(
                            isOpen = isOpen,
                            contact = routing.contact,
                            onBack = { backStack.pop() }
                        )
                }
            }
        }
    }
}
