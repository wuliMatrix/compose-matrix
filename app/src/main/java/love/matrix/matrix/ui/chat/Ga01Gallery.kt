package love.matrix.matrix.router

import androidx.compose.Composable
import androidx.compose.MutableState
import com.github.zsoltk.compose.router.Router
import love.matrix.matrix.data.Album
import love.matrix.matrix.data.Photo


@Composable
fun AddressBook69Gallery(isOpen: MutableState<Boolean>) {

    Gallery.Content(isOpen,Gallery.Routing.AlbumList)
}


interface Gallery {

    sealed class Routing {
        object AlbumList : Routing()
        data class PhotosOfAlbum(val album: Album) : Routing()
        data class FullScreenPhoto(val photo: Photo) : Routing()
    }

    companion object {
        @Composable
        fun Content(
            isOpen: MutableState<Boolean>,
            defaultRouting: Routing
        ) {
            Router(defaultRouting) { backStack ->
                when (val routing = backStack.last()) {
                    is Routing.AlbumList -> AlbumList.Content(
                        isOpen = isOpen,
                        onAlbumSelected = {
                            backStack.push(Routing.PhotosOfAlbum(it))
                        })

                    is Routing.PhotosOfAlbum -> PhotosOfAlbum.Content(
                        isOpen = isOpen,
                        album = routing.album,
                        onPhotoSelected = {
                            backStack.push(Routing.FullScreenPhoto(it))
                        })

                    is Routing.FullScreenPhoto -> FullScreenPhoto.Content(
                        photo = routing.photo
                    )
                }
            }
        }
    }
}
