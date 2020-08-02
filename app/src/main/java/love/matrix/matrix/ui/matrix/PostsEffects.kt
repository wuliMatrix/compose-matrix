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

package love.matrix.matrix.ui.matrix

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.compose.state
import love.matrix.matrix.data.Post
import love.matrix.matrix.repository.UiState
import love.matrix.matrix.repository.posts.PostsRepository
import love.matrix.matrix.repository.Result

/**
 * Effect that interacts with the repository to obtain a post with postId to display on the screen.
 * To load asynchronous data, effects are better pattern than using @Model classes since
 * effects are Compose lifecycle aware.
 * 与存储库交互以获得具有postId的帖子的效果，以显示在屏幕上。
 * 要加载异步数据，效果比使用@Model类更好，因为效果是Compose生命周期感知的
 *
 * FIXME: Is it possible to reuse uiStateFrom for this use case?
 * FIXME：是否可以在此用例中重用uiStateFrom？
 */
@Composable
fun fetchPost(postId: String, postsRepository: PostsRepository): UiState<Post> {

    var postState: UiState<Post> by state<UiState<Post>> { UiState.Loading }

    // Whenever this effect is used in a composable function, it'll load data from the repository
    // when the first composition is applied
    // 每当在可组合函数中使用此效果时，将在应用第一个组合时从存储库加载数据
    onCommit(postId, postsRepository) {
        postsRepository.getPost(postId) { result ->
            postState = when (result) {
                is Result.Success -> {
                    if (result.data != null) {
                        UiState.Success(result.data)
                    } else {
                        UiState.Error(Exception("postId doesn't exist"))
                    }
                }
                is Result.Error -> UiState.Error(result.exception)
            }
        }
    }

    return postState
}
