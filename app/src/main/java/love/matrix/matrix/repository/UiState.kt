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

package love.matrix.matrix.repository

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onActive
import androidx.compose.setValue
import androidx.compose.state

typealias RepositoryCall<T> = ((Result<T>) -> Unit) -> Unit//两层回调，实际实现代码要有两层 "->"

//out g表示只能从Source中读取的对象
sealed class UiState<out T> {
    // Nothing 还有一个作用：如果上一行代码返回了 null，就可以保证下一行代码不会被执行到，不必额外再检查一次返回值了。
    // 所以说，不需要在调用之前添加空检查
    // https://zhuanlan.zhihu.com/p/26890263
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val exception: Exception) : UiState<Nothing>()
}

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>() //成功拿到数据

    data class Error(val exception: Exception) : Result<Nothing>()//获取数据出现异常

}

/**
 *
 * 加载异步数据，效果比使用@Model类更好，因为效果是Compose生命周期感知的。
 */
@Composable
fun <T> uiStateFrom(
    repositoryCall: RepositoryCall<T>
): UiState<T> {

    var state: UiState<T> by state<UiState<T>> { UiState.Loading }


    //用于观察组合物生命周期的效果。 [callback]将在第一个合成后最初执行一次应用，然后不会再次触发。
    // [callback]将在具有一个[onDispose] [CommitScope.onDispose]方法
    // 可用于安排在效果消失时执行一次回调
    //onActive效果本质上是onCommit（true）{...}`的便利效果。
    //
    //在合成第一次提交并变为活动状态时要执行的lambda。

    //加载数据
    onActive {
        repositoryCall { result ->
            state = when (result) {
                is Result.Success -> UiState.Success(result.data)
                is Result.Error -> UiState.Error(result.exception)
            }
        }
    }

    return state
}




/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 * 如果[Result]的类型为[Success]，并且持有非null的[Success.data]，则为true。
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null //确实拿到数据，用get(）

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback//可能没拿到数据，用successor,分别处理
}




/**
 * Helper function that loads data from a repository call. Only use in Previews!
 * 从存储库调用中加载数据。仅在Previews中使用！
 */
@Composable
fun <T> previewDataFrom(
    repositoryCall: RepositoryCall<T>
): T {
    var state: T? = null
    repositoryCall { result ->
        state = (result as Result.Success).data
    }
    return state!!
}
