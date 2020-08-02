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

package love.matrix.matrix.data

import androidx.ui.graphics.ImageAsset

data class Post(//帖子
    val id: String,
    val title: String,
    val subtitle: String? = null,//副标题
    val url: String,
    val publication: Publication? = null,//出版物
    val metadata: Metadata,//元数据
    val paragraphs: List<Paragraph> = emptyList(),//段落
    val imageId: Int,
    val imageThumbId: Int,//小图
    val image: ImageAsset? = null,//图片信息（大小等）
    val imageThumb: ImageAsset? = null//封面图片
)

data class Metadata(
    val author: PostAuthor,
    val date: String,
    val readTimeMinutes: Int//大概需要多少时间阅读完
)

data class PostAuthor(
    val name: String,
    val url: String? = null
)

data class Publication(
    val name: String,
    val logoUrl: String
)

data class Paragraph(
    val type: ParagraphType,
    val text: String,
    val markups: List<Markup> = emptyList()//标记、页贴
)

data class Markup(//标记
    val type: MarkupType,
    val start: Int,
    val end: Int,
    val href: String? = null
)

enum class MarkupType {
    Link,
    Code,
    Italic,//斜体
    Bold,//粗体
}

enum class ParagraphType {
    Title,
    Caption,//字幕
    Header,
    Subhead,//副标题
    Text,
    CodeBlock,//代码块
    Quote,//引用
    Bullet,//子弹
}
