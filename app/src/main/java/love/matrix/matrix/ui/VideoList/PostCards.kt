/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package love.matrix.matrix.ui.ScreenVideo

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.*
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.layout.preferredSize
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.IconToggleButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.Surface
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Bookmark
import androidx.ui.material.icons.filled.BookmarkBorder
import androidx.ui.material.icons.filled.MoreVert
import androidx.ui.material.ripple.ripple
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import love.matrix.matrix.R
import love.matrix.matrix.data.Post
import love.matrix.matrix.fakeData.post3
import love.matrix.matrix.ui.common.ThemedPreview

@Composable
fun AuthorAndReadTime(//作者和阅读时间
        post: Post,
        modifier: Modifier = Modifier
) {
    Row(modifier) {
        ProvideEmphasis(EmphasisAmbient.current.medium) {//中等强调
            val textStyle = MaterialTheme.typography.body2
            Text(
                text = post.metadata.author.name,//作者
                style = textStyle
            )
            Text(
                text = " - ${post.metadata.readTimeMinutes} min read",//阅读时间
                style = textStyle
            )
        }
    }
}

@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    //封面图片，没有的话用默认图片
    val image = post.imageThumb ?: imageResource(R.drawable.placeholder_1_1)
    Image(
        asset = image,
        modifier = modifier
            .preferredSize(40.dp, 40.dp)
                //这是[Button]或[Snackbar]等小型组件使用的形状。
                //但是像[FloatingActionButton]这样的组件使用此形状，会覆盖边角尺寸的50％。
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun PostTitle(post: Post) {
    //高度强调
    ProvideEmphasis(EmphasisAmbient.current.high) {
        Text(post.title, style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun PostCardSimple(post: Post) {//简易明信片
    Row(
        modifier = Modifier.clickable(onClick = { })
            .padding(16.dp)
    ) {
            PostImage(post, Modifier.padding(end = 16.dp))
            Column(modifier = Modifier.weight(1f)) {
                PostTitle(post)
                AuthorAndReadTime(post)
            }

        }

}

@Composable
fun PostCardHistory(post: Post) {
    Row(
        Modifier.clickable(onClick = { })
            .padding(16.dp)
    ) {
            PostImage(
                post = post,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(Modifier.weight(1f)) {
                ProvideEmphasis(EmphasisAmbient.current.medium) {
                    Text(
                        text = "BASED ON YOUR HISTORY",
                        style = MaterialTheme.typography.overline
                    )
                }
                PostTitle(post = post)
                AuthorAndReadTime(
                    post = post,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            ProvideEmphasis(EmphasisAmbient.current.medium) {
                Icon(asset = Icons.Filled.MoreVert)
            }
        }

}

@Composable
fun BookmarkButton(//书签按钮
    isBookmarked: Boolean,
    onBookmark: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = onBookmark
    ) {
        modifier.fillMaxSize()
        if (isBookmarked) {
            Icon(
                asset = Icons.Filled.Bookmark,
                modifier = modifier
            )
        } else {
            Icon(
                asset = Icons.Filled.BookmarkBorder,
                modifier = modifier
            )
        }
    }
}



@Preview("Bookmark Button")
@Composable
fun BookmarkButtonPreview() {
    ThemedPreview {
        Surface {
            BookmarkButton(isBookmarked = false, onBookmark = { })
        }
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
fun BookmarkButtonBookmarkedPreview() {
    ThemedPreview {
        Surface {
            BookmarkButton(isBookmarked = true, onBookmark = { })
        }
    }
}

@Preview("Simple post card")
@Composable
fun SimplePostPreview() {
    ThemedPreview {
        PostCardSimple(post = post3)
    }
}

@Preview("Post History card")
@Composable
fun HistoryPostPreview() {
    ThemedPreview {
        PostCardHistory(post = post3)
    }
}

@Preview("Simple post card dark theme")
@Composable
fun SimplePostDarkPreview() {
    ThemedPreview(darkTheme = true) {
        PostCardSimple(post = post3)
    }
}
