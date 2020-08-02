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
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredSize
import androidx.ui.material.Card
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.ripple.ripple
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import love.matrix.matrix.R
import love.matrix.matrix.data.Post
import love.matrix.matrix.data.PostAuthor
import love.matrix.matrix.fakeData.post1
import love.matrix.matrix.ui.common.ThemedPreview

@Composable
fun PostCardPopular(post: Post, modifier: Modifier = Modifier) {//热门明信片
    //卡片是[Surface]，用于显示单个主题的内容和操作。
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.preferredSize(280.dp, 240.dp)
            .clickable(onClick = { })
    ) {
            Column {
                val image = post.image ?: imageResource(R.drawable.placeholder_4_3)
                Image(
                    asset = image,
                    contentScale = ContentScale.Crop,//保持宽高比
                    modifier = Modifier
                        .preferredHeight(100.dp)
                        .fillMaxSize())

                Column(modifier = Modifier.padding(16.dp)) {
                    val emphasisLevels = EmphasisAmbient.current
                    ProvideEmphasis(emphasisLevels.high) {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.h6,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = post.metadata.author.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.body2
                        )
                    }
                    ProvideEmphasis(emphasisLevels.high) {
                        Text(
                            text = "${post.metadata.date} - " +
                                    "${post.metadata.readTimeMinutes} min read",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

}

@Preview("Regular colors")
@Composable
fun PreviewPostCardPopular() {
    ThemedPreview {
        PostCardPopular(post = post1)
    }
}

@Preview("Dark colors")
@Composable
fun PreviewPostCardPopularDark() {
    ThemedPreview(darkTheme = true) {
        PostCardPopular(post = post1)
    }
}

@Preview("Regular colors, long text")
@Composable
fun PreviewPostCardPopularLongText() {
    val loremIpsum = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras ullamcorper pharetra massa,
        sed suscipit nunc mollis in. Sed tincidunt orci lacus, vel ullamcorper nibh congue quis.
        Etiam imperdiet facilisis ligula id facilisis. Suspendisse potenti. Cras vehicula neque sed
        nulla auctor scelerisque. Vestibulum at congue risus, vel aliquet eros. In arcu mauris,
        facilisis eget magna quis, rhoncus volutpat mi. Phasellus vel sollicitudin quam, eu
        consectetur dolor. Proin lobortis venenatis sem, in vestibulum est. Duis ac nibh interdum,
    """.trimIndent()
    ThemedPreview {
        PostCardPopular(
            post = post1.copy(
                title = "Title$loremIpsum",
                metadata = post1.metadata.copy(
                    author = PostAuthor("Author: $loremIpsum"),
                    readTimeMinutes = Int.MAX_VALUE
                )
            )
        )
    }
}
